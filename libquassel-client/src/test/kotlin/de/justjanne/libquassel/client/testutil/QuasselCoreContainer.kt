/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.testutil

import org.slf4j.LoggerFactory
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.DockerImageName

class QuasselCoreContainer : GenericContainer<QuasselCoreContainer>(
  DockerImageName.parse("k8r.eu/justjanne/quassel-docker:v0.14.0")
) {
  init {
    withExposedPorts(QUASSEL_PORT)
    withClasspathResourceMapping(
      "/quasseltest.crt",
      "/quasseltest.crt",
      BindMode.READ_WRITE
    )
    withEnv("SSL_CERT_FILE", "/quasseltest.crt")
    withClasspathResourceMapping(
      "/quasseltest.key",
      "/quasseltest.key",
      BindMode.READ_WRITE
    )
    withEnv("SSL_KEY_FILE", "/quasseltest.key")
    withEnv("SSL_REQUIRED", "true")
  }

  override fun start() {
    super.start()
    followOutput(Slf4jLogConsumer(logger))
  }

  companion object {
    @JvmStatic
    private val logger = LoggerFactory.getLogger(QuasselCoreContainer::class.java)

    const val QUASSEL_PORT = 4242
  }
}
