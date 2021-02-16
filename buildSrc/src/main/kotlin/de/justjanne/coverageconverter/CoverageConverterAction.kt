/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.coverageconverter

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File

internal class CoverageConverterAction(
  private val jacocoReportTask: JacocoReport
) : Action<Task> {
  private fun findOutputFile(jacocoFile: File): File? {
    val actualFile = jacocoFile.absoluteFile
    if (actualFile.exists() && actualFile.parentFile.name == "jacoco") {
      val folder = File(actualFile.parentFile.parentFile, "cobertura")
      folder.mkdirs()
      return File(folder, actualFile.name)
    }

    return null
  }

  private fun createPythonScript(name: String, temporaryDir: File): File {
    val file = File(temporaryDir, name)
    if (file.exists()) {
      file.delete()
    }
    val source = CoverageConverterPlugin::class.java.getResourceAsStream("/coverageconverter/$name")
    file.writeBytes(source.readAllBytes())
    return file
  }

  override fun execute(task: Task) {
    val cover2coverScript = createPythonScript("cover2cover.py", task.temporaryDir)
    val source2filenameScript = createPythonScript("source2filename.py", task.temporaryDir)

    fun cover2cover(reportFile: File, outputFile: File, sourceDirectories: Iterable<File>) {
      task.project.exec {
        commandLine("python3")
        args(cover2coverScript.absolutePath)
        args(reportFile.absolutePath)
        args(sourceDirectories.map(File::getAbsolutePath))
        standardOutput = outputFile.outputStream()
      }
    }

    fun source2filename(reportFile: File) {
      task.project.exec {
        commandLine("python3")
        args(source2filenameScript.absolutePath)
        args(reportFile.absolutePath)
      }
    }

    jacocoReportTask.reports.forEach {
      if (it.isEnabled && it.destination.extension == "xml") {
        val outputFile = findOutputFile(it.destination)
        if (outputFile != null) {
          cover2cover(it.destination, outputFile, jacocoReportTask.sourceDirectories)
          source2filename(outputFile)
        }
      }
    }
  }
}
