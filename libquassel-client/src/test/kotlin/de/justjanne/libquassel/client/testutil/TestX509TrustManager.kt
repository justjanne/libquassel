/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.testutil

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

object TestX509TrustManager : X509TrustManager {
  override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    // FIXME: accept everything
  }

  override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    // FIXME: accept everything
  }

  override fun getAcceptedIssuers(): Array<X509Certificate> {
    // FIXME: accept nothing
    return emptyArray()
  }
}
