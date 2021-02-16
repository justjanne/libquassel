/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.util

import java.security.cert.Certificate
import java.security.cert.X509Certificate
import javax.net.ssl.SSLSession

/**
 * Model representing the metadata of a negotiated TLS session
 */
data class TlsInfo(
  /**
   * Name of the TLS protocol, e.g. TLSv1.3
   */
  val protocol: String,
  /**
   * Negotiated cipher suite
   */
  val cipherSuite: String,
  /**
   * Negotiated key exchange mechanism if applicable
   * Deprecated in TLSv1.3
   */
  val keyExchangeMechanism: String?,
  /**
   * Peer certificate chain
   */
  val certificateChain: List<X509Certificate>,
) {

  override fun toString(): String {
    return "TlsInfo(protocol='$protocol', cipherSuite='$cipherSuite', keyExchangeMechanism=$keyExchangeMechanism)"
  }

  companion object {
    private val cipherSuiteRegex13 = "TLS_(.*)".toRegex()
    private val cipherSuiteRegex12 = "TLS_(.*)_WITH_(.*)".toRegex()

    private fun cipherSuiteRegex(protocol: String): Regex =
      if (protocol == "TLSv1.3") cipherSuiteRegex13
      else cipherSuiteRegex12

    private fun parseCipherSuite(protocol: String, cipherSuite: String): Pair<String, String?>? {
      val match = cipherSuiteRegex(protocol)
        .matchEntire(cipherSuite)
        ?: return null

      return if (protocol == "TLSv1.3") {
        Pair(match.groupValues[1], null)
      } else {
        Pair(match.groupValues[1], match.groupValues.getOrNull(2))
      }
    }

    /**
     * Obtain the TLS metadata of an existing [SSLSession]
     */
    fun ofSession(session: SSLSession): TlsInfo? {
      val (cipherSuite, keyExchangeMechanism) = parseCipherSuite(
        session.protocol,
        session.cipherSuite,
      ) ?: return null

      return TlsInfo(
        session.protocol,
        cipherSuite,
        keyExchangeMechanism,
        session.peerCertificates.map(Certificate::toX509)
      )
    }
  }
}
