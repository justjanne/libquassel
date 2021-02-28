/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.variant

import de.justjanne.libquassel.protocol.models.types.QtType

/**
 * Simple alias for a generic QVariantMap type
 */
typealias QVariantMap = Map<String, QVariant_>

/**
 * Transform a QVariantMap into a QVariantList of interleaved keys and values
 */
fun QVariantMap.toVariantList(): QVariantList =
  flatMap { (key, value) ->
    listOf(qVariant(key, QtType.QString), value)
  }
