/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.coverageconverter

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.justjanne.coverageconverter.convertCounter
import de.justjanne.coverageconverter.jacoco.CounterTypeDto
import de.justjanne.coverageconverter.jacoco.ReportDto
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

  override fun execute(task: Task) {
    fun printTotal(data: ReportDto) {
      val instructionRate = convertCounter(data.counters, CounterTypeDto.INSTRUCTION).rate * 100
      val instructionMissed = convertCounter(data.counters, CounterTypeDto.INSTRUCTION).missed
      val branchRate = convertCounter(data.counters, CounterTypeDto.BRANCH).rate * 100
      val branchMissed = convertCounter(data.counters, CounterTypeDto.BRANCH).missed
      println("[JacocoPrinter] Instructions  $instructionRate%  (Missed $instructionMissed)")
      println("[JacocoPrinter] Branches      $branchRate%  (Missed $branchMissed)")
    }

    fun convertFile(input: File, output: File) {
      val mapper = XmlMapper(
        JacksonXmlModule().apply {
          setDefaultUseWrapper(false)
        }
      ).apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        enable(SerializationFeature.WRAP_ROOT_VALUE)
        registerModule(KotlinModule(strictNullChecks = true))
      }
      val data = mapper.readValue(input, ReportDto::class.java)
      val result = convertReport(data)
      printTotal(data)
      mapper.writeValue(output, result)
    }

    jacocoReportTask.reports.forEach {
      if (it.isEnabled && it.destination.extension == "xml") {
        val outputFile = findOutputFile(it.destination)
        if (outputFile != null) {
          convertFile(it.destination, outputFile)
        }
      }
    }
  }
}
