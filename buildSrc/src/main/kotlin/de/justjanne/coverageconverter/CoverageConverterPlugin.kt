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

import groovy.util.XmlSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File

class CoverageConverterPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val extension = project.extensions.create("coverage", CoverageConverterExtension::class.java)

    if (extension.autoConfigureCoverage) {
      val jacocoPluginExtension = project.extensions.findByType(JacocoPluginExtension::class.java)
      if (jacocoPluginExtension != null) {
        jacocoPluginExtension.toolVersion = "0.8.3"
      }
    }

    project.afterEvaluate {
      val testTask = tasks.getByName("test")

      val jacocoReportTask = tasks.getByName("jacocoTestReport") as? JacocoReport
      if (jacocoReportTask != null) {
        jacocoReportTask.dependsOn(testTask)
        if (extension.autoConfigureCoverage) {
          jacocoReportTask.sourceDirectories.from(fileTree("src/main/kotlin"))
          jacocoReportTask.classDirectories.from(fileTree("build/classes"))
          jacocoReportTask.reports {
            xml.destination = File("${buildDir}/reports/jacoco/report.xml")
            html.isEnabled = true
            xml.isEnabled = true
            csv.isEnabled = false
          }
        }

        tasks.register("coberturaTestReport") {
          dependsOn(jacocoReportTask)
          mustRunAfter(jacocoReportTask)
          group = "verification"

          doLast(CoverageConverterAction(jacocoReportTask))
        }
      }
    }
  }
}
