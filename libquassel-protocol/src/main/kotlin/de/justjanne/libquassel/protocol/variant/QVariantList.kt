package de.justjanne.libquassel.protocol.variant

/**
 * Simple alias for a generic QVariantList type
 */
typealias QVariantList = List<QVariant_>

fun QVariantList.toVariantMap(): QVariantMap =
  this.zipWithNext().map { (key, value) ->
    Pair(key.into(""), value)
  }.toMap()
