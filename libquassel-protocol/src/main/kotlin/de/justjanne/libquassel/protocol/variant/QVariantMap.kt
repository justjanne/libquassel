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
