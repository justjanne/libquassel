/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.features

import de.justjanne.libquassel.annotations.Generated

/**
 * Inline class encapsulating a quassel feature name
 */
@Generated
inline class QuasselFeatureName(
  /**
   * Standardized name of the feature
   */
  val name: String,
)
