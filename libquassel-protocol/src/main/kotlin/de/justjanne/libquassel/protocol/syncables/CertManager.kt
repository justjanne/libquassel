/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.syncables.state.CertManagerState
import de.justjanne.libquassel.protocol.syncables.stubs.CertManagerStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import java.nio.ByteBuffer
import java.security.PrivateKey
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

open class CertManager(
  session: Session? = null,
  state: CertManagerState
) : StatefulSyncableObject<CertManagerState>(session, "CertManager", state),
  CertManagerStub {
  override fun fromVariantMap(properties: QVariantMap) {
    val privateKeyPem = properties["sslKey"].into("")
    val certPem = properties["sslCert"].into("")

    state.update {
      copy(
        privateKeyPem = privateKeyPem,
        certificatePem = certPem,
        privateKey = readPrivateKey(privateKeyPem),
        certificate = readCertificate(certPem)
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "sslKey" to qVariant(StringSerializerUtf8.serializeRaw(state().certificatePem), QtType.QByteArray),
    "sslCert" to qVariant(StringSerializerUtf8.serializeRaw(state().privateKeyPem), QtType.QByteArray)
  )

  private fun readPrivateKey(pem: String): PrivateKey? {
    if (pem.isBlank()) {
      return null
    }

    try {
      val keyPair = PEMParser(pem.reader()).readObject() as? PEMKeyPair
        ?: return null
      return JcaPEMKeyConverter().getPrivateKey(keyPair.privateKeyInfo)
    } catch (t: Throwable) {
      return null
    }
  }

  private fun readCertificate(pem: String): Certificate? {
    if (pem.isBlank()) {
      return null
    }

    try {
      val certificate = PEMParser(pem.reader()).readObject() as? X509CertificateHolder
        ?: return null
      return CertificateFactory.getInstance("X.509")
        .generateCertificate(certificate.encoded.inputStream())
    } catch (t: Throwable) {
      return null
    }
  }

  override fun setSslKey(encoded: ByteBuffer) {
    val pem = StringSerializerUtf8.deserializeRaw(encoded)

    state.update {
      copy(
        privateKeyPem = pem,
        privateKey = readPrivateKey(pem)
      )
    }
    super.setSslKey(encoded)
  }

  override fun setSslCert(encoded: ByteBuffer) {
    val pem = StringSerializerUtf8.deserializeRaw(encoded)

    state.update {
      copy(
        certificatePem = pem,
        certificate = readCertificate(pem)
      )
    }
    super.setSslCert(encoded)
  }

  fun privateKey() = state().privateKey
  fun privateKeyPem() = state().privateKeyPem

  fun certificate() = state().certificate
  fun certificatePem() = state().certificatePem
}
