/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.util

import java.io.ByteArrayInputStream
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

private val certificateFactory = CertificateFactory.getInstance("X.509")

/**
 * Utility function to extract, if available, an X509 Certificate from a given
 * certificate
 */
fun Certificate.toX509(): X509Certificate =
  certificateFactory.generateCertificate(ByteArrayInputStream(this.encoded))
    as X509Certificate
