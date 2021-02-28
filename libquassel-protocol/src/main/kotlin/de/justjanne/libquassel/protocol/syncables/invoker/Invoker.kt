/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.invoker

import de.justjanne.libquassel.protocol.exceptions.UnknownMethodException
import de.justjanne.libquassel.protocol.exceptions.WrongObjectTypeException
import de.justjanne.libquassel.protocol.variant.QVariantList

interface Invoker<out T> {
  val className: String

  @Throws(WrongObjectTypeException::class, UnknownMethodException::class)
  fun invoke(on: Any?, method: String, params: QVariantList)
}
