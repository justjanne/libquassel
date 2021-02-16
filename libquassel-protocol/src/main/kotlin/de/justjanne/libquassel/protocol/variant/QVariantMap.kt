package de.justjanne.libquassel.protocol.variant

import de.justjanne.libquassel.protocol.models.types.QtType

/**
 * Simple alias for a generic QVariantMap type
 */
typealias QVariantMap = Map<String, QVariant_>

fun QVariantMap.toVariantList(): QVariantList =
  this.toList().flatMap { (key, value) ->
    listOf(qVariant(key, QtType.QString), value)
  }
