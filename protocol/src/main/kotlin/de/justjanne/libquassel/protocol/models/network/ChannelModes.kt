/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.network

import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

data class ChannelModes(
  val a: Map<Char, Set<String>> = emptyMap(),
  val b: Map<Char, String> = emptyMap(),
  val c: Map<Char, String> = emptyMap(),
  val d: Set<Char> = emptySet()
) {
  fun modeString(): String {
    if (b.isEmpty() && c.isEmpty() && d.isEmpty()) {
      return ""
    }

    val buffer = StringBuilder("+")
    val params = mutableListOf<String>()

    d.joinTo(buffer)

    for ((mode, param) in c) {
      buffer.append(mode)
      params.add(param)
    }

    for ((mode, param) in b) {
      buffer.append(mode)
      params.add(param)
    }

    buffer.append(' ')
    params.joinTo(buffer, " ")
    return buffer.toString()
  }

  fun toVariantMap(): QVariantMap {
    return mapOf(
      "A" to qVariant(
        a.map { (key, value) ->
          key.toString() to qVariant(value.toList(), QtType.QStringList)
        }.toMap(),
        QtType.QVariantMap
      ),
      "B" to qVariant(
        b.map { (key, value) ->
          key.toString() to qVariant(value, QtType.QString)
        }.toMap(),
        QtType.QVariantMap
      ),
      "C" to qVariant(
        c.map { (key, value) ->
          key.toString() to qVariant(value, QtType.QString)
        }.toMap(),
        QtType.QVariantMap
      ),
      "D" to qVariant(d.joinToString(), QtType.QString),
    )
  }

  companion object {
    fun fromVariantMap(properties: QVariantMap) = ChannelModes(
      a = properties["A"].into<QVariantMap>()?.map { (key, value) ->
        key.first() to value.into<QStringList>()
          ?.filterNotNull()
          ?.toSet()
          .orEmpty()
      }?.toMap().orEmpty(),
      b = properties["B"].into<QVariantMap>()?.map { (key, value) ->
        key.first() to value.into<String>().orEmpty()
      }?.toMap().orEmpty(),
      c = properties["C"].into<QVariantMap>()?.map { (key, value) ->
        key.first() to value.into<String>().orEmpty()
      }?.toMap().orEmpty(),
      d = properties["D"].into<String>()?.toSet().orEmpty()
    )
  }
}
