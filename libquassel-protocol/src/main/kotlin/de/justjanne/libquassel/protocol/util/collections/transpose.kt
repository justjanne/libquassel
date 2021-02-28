/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_
import de.justjanne.libquassel.protocol.variant.qVariant

fun List<QVariantMap>.transpose(): QVariantMap =
  flatMap { it.keys }.toSet().map { key ->
    Pair(key, qVariant(map { it[key] as QVariant_ }, QtType.QVariantList))
  }.toMap()
