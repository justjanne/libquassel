/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import java.security.PrivateKey
import java.security.cert.Certificate

data class CertManagerState(
  val identityId: IdentityId,
  val privateKeyPem: String = "",
  val certificatePem: String = "",
  val privateKey: PrivateKey? = null,
  val certificate: Certificate? = null
)
