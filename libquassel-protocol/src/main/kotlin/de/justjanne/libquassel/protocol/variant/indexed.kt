/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.variant

@Suppress("NOTHING_TO_INLINE")
internal inline fun QVariant_?.indexed(index: Int?) = this?.let {
  index?.let { i ->
    it.into<QVariantList>()?.get(i)
  } ?: it
}
