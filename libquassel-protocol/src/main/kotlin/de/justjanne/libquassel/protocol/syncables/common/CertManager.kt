/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.common

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableObject
import de.justjanne.libquassel.protocol.syncables.state.CertManagerState
import de.justjanne.libquassel.protocol.syncables.stubs.CertManagerStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

open class CertManager(
  session: Session? = null,
  state: CertManagerState
) : StatefulSyncableObject<CertManagerState>(session, "CertManager", state),
  CertManagerStub {
  init {
    renameObject(state().identifier())
  }

  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        privateKeyPem = StringSerializerUtf8.deserializeRaw(properties["sslKey"].into()),
        certificatePem = StringSerializerUtf8.deserializeRaw(properties["sslCert"].into())
      )
    }
    renameObject(state().identifier())
    initialized = true
  }

  override fun toVariantMap() = mapOf(
    "sslKey" to qVariant(StringSerializerUtf8.serializeRaw(state().privateKeyPem), QtType.QByteArray),
    "sslCert" to qVariant(StringSerializerUtf8.serializeRaw(state().certificatePem), QtType.QByteArray)
  )

  override fun setSslKey(encoded: ByteBuffer) {
    val pem = StringSerializerUtf8.deserializeRaw(encoded)

    state.update {
      copy(privateKeyPem = pem)
    }
    super.setSslKey(encoded)
  }

  override fun setSslCert(encoded: ByteBuffer) {
    val pem = StringSerializerUtf8.deserializeRaw(encoded)

    state.update {
      copy(certificatePem = pem)
    }
    super.setSslCert(encoded)
  }

  fun privateKey() = state().privateKey
  fun privateKeyPem() = state().privateKeyPem

  fun certificate() = state().certificate
  fun certificatePem() = state().certificatePem
}
