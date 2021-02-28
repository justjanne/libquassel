/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.alias.Alias
import de.justjanne.libquassel.protocol.models.alias.Command
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.AliasManagerState
import de.justjanne.libquassel.protocol.syncables.stubs.AliasManagerStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class AliasManager(
  session: Session? = null,
  state: AliasManagerState = AliasManagerState()
) : StatefulSyncableObject<AliasManagerState>(session, "AliasManager", state),
  AliasManagerStub {
  override fun toVariantMap(): QVariantMap = mapOf(
    "Aliases" to qVariant(
      mapOf(
        "names" to qVariant(aliases().map(Alias::name), QtType.QStringList),
        "expansions" to qVariant(aliases().map(Alias::expansion), QtType.QStringList)
      ),
      QtType.QVariantMap
    )
  )

  override fun fromVariantMap(properties: QVariantMap) {
    val aliases = properties["Aliases"].into<QVariantMap>().orEmpty()

    val names = aliases["names"].into<QStringList>().orEmpty()
    val expansions = aliases["expansions"].into<List<String>>().orEmpty()
    require(names.size == expansions.size) {
      "Sizes do not match: names=${names.size}, expansions=${expansions.size}"
    }

    state.update {
      copy(aliases = names.zip(expansions, ::Alias))
    }
  }

  override fun addAlias(name: String, expansion: String) {
    if (contains(name)) {
      return
    }

    state.update {
      copy(aliases = aliases + Alias(name, expansion))
    }
    super.addAlias(name, expansion)
  }

  fun aliases() = state().aliases

  fun indexOf(name: String?) = state().indexOf(name)

  fun contains(name: String?) = state().contains(name)

  fun processInput(
    info: BufferInfo,
    message: String
  ) = state().processInput(
    info,
    session?.network(info.networkId)?.state(),
    message
  )

  fun processInput(
    info: BufferInfo,
    message: String,
    previousCommands: MutableList<Command>
  ) = state().processInput(
    info,
    session?.network(info.networkId)?.state(),
    message,
    previousCommands
  )

  fun expand(
    expansion: String,
    bufferInfo: BufferInfo,
    arguments: String,
    previousCommands: MutableList<Command>
  ) = state().expand(
    expansion,
    bufferInfo,
    session?.network(bufferInfo.networkId)?.state(),
    arguments,
    previousCommands
  )
}
