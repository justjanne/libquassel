package de.justjanne.libquassel.irc.extensions

import de.justjanne.libquassel.irc.backport.StringJoiner

internal inline fun joinString(
  delimiter: String = "",
  prefix: String = "",
  suffix: String = "",
  builderAction: StringJoiner.() -> Unit
): String {
  return StringJoiner(delimiter, prefix, suffix).apply(builderAction).toString()
}
