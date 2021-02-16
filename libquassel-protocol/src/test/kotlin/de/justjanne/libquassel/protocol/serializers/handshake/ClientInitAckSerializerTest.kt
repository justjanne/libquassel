/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.BackendInfo
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.SetupEntry
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.testutil.handshakeSerializerTest
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Test

class ClientInitAckSerializerTest {
  @Test
  fun testSimple() = handshakeSerializerTest(
    HandshakeMessage.ClientInitAck(
      coreConfigured = false,
      backendInfo = emptyList(),
      authenticatorInfo = emptyList(),
      featureSet = FeatureSet.none()
    ),
    // byteBufferOf(0x00u)
  )

  @Test
  fun testRealistic() = handshakeSerializerTest(
    HandshakeMessage.ClientInitAck(
      coreConfigured = false,
      backendInfo = listOf(
        BackendInfo(
          entries = emptyList(),
          isDefault = true,
          displayName = "SQLite",
          description = "SQLite is a file-based database engine that does not " +
            "require any setup. It is suitable for small and medium-sized " +
            "databases that do not require access via network. Use SQLite if " +
            "your Quassel Core should store its data on the same machine it is " +
            "running on, and if you only expect a few users to use your core.",
          backendId = "SQLite"
        ),
        BackendInfo(
          entries = listOf(
            SetupEntry(
              "Username",
              "Username",
              qVariant("quassel", QtType.QString)
            ),
            SetupEntry(
              "Password",
              "Password",
              qVariant<String?>(null, QtType.QString)
            ),
            SetupEntry(
              "Hostname",
              "Hostname",
              qVariant("localhost", QtType.QString)
            ),
            SetupEntry(
              "Port",
              "Port",
              qVariant(5432, QtType.Int)
            ),
            SetupEntry(
              "Database",
              "Database",
              qVariant("quassel", QtType.QString)
            )
          ),
          isDefault = false,
          displayName = "PostgreSQL",
          description = "PostgreSQL Turbo Bomber HD!",
          backendId = "PostgreSQL"
        )
      ),
      authenticatorInfo = listOf(
        BackendInfo(
          entries = emptyList(),
          isDefault = true,
          displayName = "Database",
          description = "Do not authenticate against any remote service, but " +
            "instead save a hashed and salted password in the database " +
            "selected in the next step.",
          backendId = "Database"
        ),
        BackendInfo(
          entries = listOf(
            SetupEntry(
              "Hostname",
              "Hostname",
              qVariant("ldap://localhost", QtType.QString),
            ),
            SetupEntry(
              "Port",
              "Port",
              qVariant(389, QtType.Int),
            ),
            SetupEntry(
              "BindDN",
              "Bind DN",
              qVariant<String?>(null, QtType.QString),
            ),
            SetupEntry(
              "BindPassword",
              "Bind Password",
              qVariant<String?>(null, QtType.QString),
            ),
            SetupEntry(
              "BaseDN",
              "Base DN",
              qVariant<String?>(null, QtType.QString),
            ),
            SetupEntry(
              "Filter",
              "Filter",
              qVariant<String?>(null, QtType.QString),
            ),
            SetupEntry(
              "UidAttribute",
              "UID Attribute",
              qVariant("uid", QtType.QString),
            )
          ),
          isDefault = false,
          displayName = "LDAP",
          description = "Authenticate users using an LDAP server.",
          backendId = "LDAP"
        )
      ),
      featureSet = FeatureSet.none()
    ),
    // byteBufferOf(0x00u)
  )
}
