/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.Alias
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.Command
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.stubs.AliasManagerStub
import de.justjanne.libquassel.protocol.util.expansion.Expansion
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import kotlin.math.exp

class AliasManager constructor(
  session: Session
) : SyncableObject(session, "AliasManager"), AliasManagerStub {
  override fun toVariantMap(): QVariantMap = mapOf(
    "Aliases" to qVariant(initAliases(), QtType.QVariantMap)
  )

  override fun fromVariantMap(properties: QVariantMap) {
    initSetAliases(properties["Aliases"].into<QVariantMap>().orEmpty())
  }

  private fun initAliases(): QVariantMap = mapOf(
    "names" to qVariant(aliases.map(Alias::name), QtType.QStringList),
    "expansions" to qVariant(aliases.map(Alias::expansion), QtType.QStringList)
  )

  private fun initSetAliases(aliases: QVariantMap) {
    val names = aliases["names"].into<QStringList>().orEmpty()
    val expansions = aliases["expansions"].into<List<String>>().orEmpty()

    require(names.size == expansions.size) {
      "Sizes do not match: names=${names.size}, expansions=${expansions.size}"
    }

    this.aliases = names.zip(expansions, ::Alias)
  }

  override fun addAlias(name: String, expansion: String) {
    if (contains(name)) {
      return
    }

    aliases += Alias(name, expansion)
    super.addAlias(name, expansion)
  }

  fun indexOf(name: String?) = aliases.map(Alias::name).indexOf(name)

  fun contains(name: String?) = aliases.map(Alias::name).contains(name)

  fun processInput(
    info: BufferInfo,
    message: String
  ) = mutableListOf<Command>().also {
    processInput(info, message, it)
  }

  fun processInput(
    info: BufferInfo,
    message: String,
    previousCommands: MutableList<Command>
  ) {
    val (command, arguments) = determineMessageCommand(message)
    if (command == null) {
      // If no command is found, this means the message should be treated as
      // pure text. To ensure this won’t be unescaped twice it’s sent with /SAY.
      previousCommands.add(Command(info, "/SAY $arguments"))
    } else {
      val found = aliases.firstOrNull { it.name.equals(command, true) }
      if (found != null) {
        expand(found.expansion ?: "", info, arguments, previousCommands)
      } else {
        previousCommands.add(Command(info, message))
      }
    }
  }

  fun expand(
    expansion: String,
    bufferInfo: BufferInfo,
    arguments: String,
    previousCommands: MutableList<Command>
  ) {
    /*
    val params = arguments.split(' ')
    expansion.split(';')
      .map(String::trimStart)
      .map(Expansion.Companion::parse)
      .map {
        it.map {
          when (it) {
            is Expansion.Constant -> TODO()
            is Expansion.Parameter -> TODO()
            is Expansion.ParameterRange ->
              params.subList(it.from, it.to ?: params.size)
                .joinToString(" ")
            is Expansion.Text ->
              it.value
          }
        }
      }
    */
  }

  fun copy() = AliasManager(session).also {
    it.fromVariantMap(toVariantMap())
  }

  var aliases = listOf<Alias>()

  companion object {
    private fun determineMessageCommand(message: String) = when {
      // Only messages starting with a forward slash are commands
      !message.startsWith("/") ->
        Pair(null, message)
      // If a message starts with //, we consider that an escaped slash
      message.startsWith("//") ->
        Pair(null, message.substring(1))
      // If the first word of a message contains more than one slash, it is
      // usually a regex of format /[a-z][a-z0-9]*/g, or a path of format
      // /usr/bin/powerline-go. In that case we also pass it right through
      message.startsWith("/") &&
        message.substringBefore(' ').indexOf('/', 1) != -1
      -> Pair(null, message)
      // If the first word is purely a /, we won’t consider it a command either
      message.substringBefore(' ') == "/" ->
        Pair(null, message)
      // Otherwise we treat the first word as a command, and all further words as
      // arguments
      else -> Pair(
        message.trimStart('/').substringBefore(' '),
        message.substringAfter(' ')
      )
    }
  }
}
