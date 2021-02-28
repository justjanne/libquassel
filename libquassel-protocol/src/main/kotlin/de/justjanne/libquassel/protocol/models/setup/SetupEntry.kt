/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.setup

import de.justjanne.libquassel.protocol.variant.QVariant_

/**
 * Model of a backend configuration entry
 */
data class SetupEntry(
  /**
   * Key for the configuration field
   */
  val key: String,
  /**
   * User-visible display name
   */
  val displayName: String,
  /**
   * Default value. The type of this value also determines the UI widget used.
   */
  val defaultValue: QVariant_,
)
