/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client

import de.justjanne.libquassel.client.session.ClientSession
import de.justjanne.libquassel.client.testutil.QuasselCoreContainer
import de.justjanne.libquassel.client.testutil.TestX509TrustManager
import de.justjanne.libquassel.protocol.connection.ProtocolFeature
import de.justjanne.libquassel.protocol.connection.ProtocolMeta
import de.justjanne.libquassel.protocol.connection.ProtocolVersion
import de.justjanne.libquassel.protocol.exceptions.HandshakeException
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.CoroutineChannel
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.session.CoreState
import de.justjanne.testcontainersci.api.providedContainer
import de.justjanne.testcontainersci.extension.CiContainers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import javax.net.ssl.SSLContext
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@CiContainers
class ClientTest {
  private val quassel = providedContainer("QUASSEL_CONTAINER") {
    QuasselCoreContainer()
  }

  private val logger = LoggerFactory.getLogger(ClientTest::class.java)

  private val username = "AzureDiamond"
  private val password = "hunter2"

  @Test
  fun testConnect(): Unit = runBlocking {
    val channel = CoroutineChannel()

    channel.connect(
      InetSocketAddress(
        quassel.address,
        quassel.getMappedPort(4242)
      )
    )

    val session = ClientSession(
      channel, ProtocolFeature.all,
      listOf(
        ProtocolMeta(
          ProtocolVersion.Datastream,
          0x0000u
        )
      ),
      SSLContext.getInstance("TLSv1.3").apply {
        init(null, arrayOf(TestX509TrustManager), null)
      }
    )
    val coreState: CoreState = session.handshakeHandler.init(
      "Quasseltest v0.1",
      "2021-06-06",
      FeatureSet.all()
    )
    assertTrue(coreState is CoreState.Unconfigured)
    assertThrows<HandshakeException.SetupException> {
      session.handshakeHandler.configureCore(
        username,
        password,
        "MongoDB",
        emptyMap(),
        "OAuth2",
        emptyMap()
      )
    }
    session.handshakeHandler.configureCore(
      username,
      password,
      "SQLite",
      emptyMap(),
      "Database",
      emptyMap()
    )
    assertThrows<HandshakeException.LoginException> {
      session.handshakeHandler.login("acidburn", "ineverweardresses")
    }
    session.handshakeHandler.login(username, password)
    session.baseInitHandler.waitForInitDone()
    logger.trace("Init Done")
    withTimeout(5_000L) {
      assertEquals(
        emptyList(),
        session.backlogManager.backlog(bufferId = BufferId(1), limit = 5)
      )
      logger.trace("Backlog Test #1 Done")
      assertEquals(
        emptyList(),
        session.backlogManager.backlogAll(limit = 5)
      )
      logger.trace("Backlog Test #2 Done")
      assertEquals(
        emptyList(),
        session.backlogManager.backlogForward(bufferId = BufferId(1), limit = 5)
      )
      logger.trace("Backlog Test #3 Done")
    }
    channel.close()
  }
}
