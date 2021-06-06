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
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8

/**
 * Simple alias for a generic QVariantMap type
 */
typealias QVariantMap = Map<String, QVariant_>

/**
 * Transform a QVariantMap into a QVariantList of interleaved keys and values
 */
fun QVariantMap.toVariantList(byteBuffer: Boolean = false): QVariantList =
  flatMap { (key, value) ->
    if (byteBuffer) {
      listOf(qVariant(StringSerializerUtf8.serializeRaw(key), QtType.QByteArray), value)
    } else {
      listOf(qVariant(key, QtType.QString), value)
    }
  }
