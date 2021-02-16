package de.justjanne.libquassel.protocol.util

internal infix fun <T> Any?.instanceof(other: Class<T>?): Boolean =
  other?.isInstance(this) != false
