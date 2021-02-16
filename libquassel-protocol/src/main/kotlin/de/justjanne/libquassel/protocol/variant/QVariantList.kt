package de.justjanne.libquassel.protocol.variant

/**
 * Simple alias for a generic QVariantList type
 */
typealias QVariantList = List<QVariant_>

/**
 * Transform a QVariantList of interleaved keys and values into a QVariantMap
 */
fun QVariantList.toVariantMap(): QVariantMap =
  zipWithNext { key, value ->
    Pair(key.into(""), value)
  }.toMap()
