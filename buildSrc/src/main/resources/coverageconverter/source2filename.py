#!/usr/bin/env python
import lxml.etree
import os.path
import sys


def convert_source(filename):
  # read input file
  root = lxml.etree.parse(filename)
  sources = root.find('sources')
  packages = root.find('packages')
  for package in packages:
    classes = package.find('classes')
    for clazz in classes:
      file_not_found = True
      for source in sources:
        full_filename = source.text + '/' + clazz.attrib['filename']
        if os.path.isfile(full_filename):
          clazz.attrib['filename'] = full_filename
          file_not_found = False
      if file_not_found:
        print("Warning: File {} not found in all sources; removing from sources.".format(clazz.attrib['filename']))
        clazz.getparent().remove(clazz)

  data = lxml.etree.tostring(root, pretty_print=True)
  # open the input file in write mode
  fin = open(filename, "wb")
  # overrite the input file with the resulting data
  fin.write(data)
  # close the file
  fin.close()


if __name__ == '__main__':
  if len(sys.argv) < 2:
    print("Usage: source2filename.py FILENAME")
    sys.exit(1)

  filename = sys.argv[1]

  convert_source(filename)
