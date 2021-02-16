/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.util.triples
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

object BackendInfoSerializer {
  fun serialize(data: BackendInfo): QVariantMap = mapOf(
    "SetupKeys" to qVariant<QStringList>(
      data.entries.map(SetupEntry::key),
      QtType.QStringList
    ),
    "SetupDefaults" to qVariant<QVariantMap>(
      data.entries.map { it.key to it.defaultValue }.toMap<String, QVariant_>(),
      QtType.QVariantMap
    ),
    "SetupData" to qVariant<QVariantList>(
      data.entries.flatMap {
        listOf<QVariant_>(
          qVariant(it.key, QtType.QString),
          qVariant(it.displayName, QtType.QString),
          it.defaultValue
        )
      },
      QtType.QVariantList
    ),
    "IsDefault" to qVariant(
      data.isDefault,
      QtType.Bool
    ),
    "DisplayName" to qVariant(
      data.displayName,
      QtType.QString
    ),
    "Description" to qVariant(
      data.description,
      QtType.QString
    ),
    "BackendId" to qVariant(
      data.backendId,
      QtType.QString
    ),
  )

  private fun parseSetupEntries(
    data: QVariantList?,
    defaults: QVariantMap
  ): List<SetupEntry> = data?.triples { key, displayName, defaultValue ->
    SetupEntry(key.into(""), displayName.into(""), defaultValue)
  } ?: defaults.map { (key, value) -> SetupEntry(key, key, value) }

  fun deserialize(data: QVariantMap) = BackendInfo(
    entries = parseSetupEntries(
      data["SetupData"].into<QVariantList>(),
      data["SetupDefaults"].into<QVariantMap>().orEmpty()
    ),
    isDefault = data["IsDefault"].into<Boolean>(false),
    displayName = data["DisplayName"].into<String>(""),
    description = data["Description"].into<String>(""),
    backendId = data["BackendId"].into<String>("")
  )
}
