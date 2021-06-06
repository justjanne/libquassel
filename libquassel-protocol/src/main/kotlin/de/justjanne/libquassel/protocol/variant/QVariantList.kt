/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.variant

import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.util.collections.pairs
import java.nio.ByteBuffer

/**
 * Simple alias for a generic QVariantList type
 */
typealias QVariantList = List<QVariant_>

/**
 * Transform a QVariantList of interleaved keys and values into a QVariantMap
 */
fun QVariantList.toVariantMap(byteBuffer: Boolean = false): QVariantMap =
  pairs { key, value ->
    if (byteBuffer) {
      Pair(StringSerializerUtf8.deserializeRaw(key.into<ByteBuffer>()), value)
    } else {
      Pair(key.into(""), value)
    }
  }.toMap()
