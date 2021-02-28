/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.alias.Alias
import de.justjanne.libquassel.protocol.models.alias.Command
import de.justjanne.libquassel.protocol.util.expansion.Expansion

data class AliasManagerState(
  val aliases: List<Alias> = emptyList()
) {
  fun indexOf(name: String?) = aliases.map(Alias::name).indexOf(name)

  fun contains(name: String?) = aliases.map(Alias::name).contains(name)

  fun processInput(
    info: BufferInfo,
    networkState: NetworkState?,
    message: String
  ) = mutableListOf<Command>().also {
    processInput(info, networkState, message, it)
  }

  fun processInput(
    info: BufferInfo,
    networkState: NetworkState?,
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
        expand(found.expansion ?: "", info, networkState, arguments, previousCommands)
      } else {
        previousCommands.add(Command(info, message))
      }
    }
  }

  fun expand(
    expansion: String,
    bufferInfo: BufferInfo,
    networkState: NetworkState?,
    arguments: String,
    previousCommands: MutableList<Command>
  ) {
    val params = arguments.split(' ')
    previousCommands.add(
      Command(
        bufferInfo,
        expansion.split(';')
          .map(String::trimStart)
          .map(Expansion.Companion::parse)
          .map {
            it.map {
              when (it) {
                is Expansion.Constant -> when (it.field) {
                  Expansion.ConstantField.CHANNEL ->
                    bufferInfo.bufferName
                  Expansion.ConstantField.NICK ->
                    networkState?.myNick
                  Expansion.ConstantField.NETWORK ->
                    networkState?.networkName
                }
                is Expansion.Parameter -> when (it.field) {
                  Expansion.ParameterField.HOSTNAME ->
                    networkState?.ircUser(params[it.index])?.host() ?: "*"
                  Expansion.ParameterField.VERIFIED_IDENT ->
                    params.getOrNull(it.index)?.let { param ->
                      networkState?.ircUser(param)?.verifiedUser() ?: "*"
                    }
                  Expansion.ParameterField.IDENT ->
                    params.getOrNull(it.index)?.let { param ->
                      networkState?.ircUser(param)?.user() ?: "*"
                    }
                  Expansion.ParameterField.ACCOUNT ->
                    params.getOrNull(it.index)?.let { param ->
                      networkState?.ircUser(param)?.account() ?: "*"
                    }
                  null -> params.getOrNull(it.index) ?: it.source
                }
                is Expansion.ParameterRange ->
                  params.subList(it.from, it.to ?: params.size)
                    .joinToString(" ")
                is Expansion.Text ->
                  it.source
              } ?: it.source
            }
          }.joinToString(";")
      )
    )
  }

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
