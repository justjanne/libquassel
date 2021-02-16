package de.justjanne.libquassel.protocol.util

internal infix fun <T> Class<*>?.subtype(other: Class<T>?): Boolean =
  this != null && other?.isAssignableFrom(this) == true
