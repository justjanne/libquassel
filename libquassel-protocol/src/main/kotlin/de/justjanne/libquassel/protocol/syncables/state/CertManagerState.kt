/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import java.security.PrivateKey
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

data class CertManagerState(
  val identityId: IdentityId,
  val privateKeyPem: String = "",
  val certificatePem: String = "",
) {
  val privateKey = readPrivateKey(privateKeyPem)
  val certificate = readCertificate(certificatePem)

  fun identifier() = "${identityId.id}"

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
}
