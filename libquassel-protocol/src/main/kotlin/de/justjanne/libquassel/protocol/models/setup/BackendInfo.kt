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

/**
 * Model for storage/authenticator backend configuration
 */
data class BackendInfo(
  /**
   * Configuration entries
   */
  val entries: List<SetupEntry>,
  /**
   * Whether or not this backend is default. Newer quassel clients use the first
   * entry in the list instead of checking this field
   */
  val isDefault: Boolean,
  /**
   * User-visible name of the backend
   */
  val displayName: String,
  /**
   * User-visible description of the backend
   */
  val description: String,
  /**
   * ID
   */
  val backendId: String,
)
