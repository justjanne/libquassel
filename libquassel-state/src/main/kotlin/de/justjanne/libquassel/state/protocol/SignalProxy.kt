/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.state.protocol

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.variant.QVariantList

interface SignalProxy {
  val protocolSide: ProtocolSide

  fun sync(
    target: ProtocolSide,
    className: String,
    objectName: String,
    function: String,
    arguments: QVariantList
  ) {
    if (target != protocolSide) {
      emit(
        SignalProxyMessage.Sync(
          className,
          objectName,
          function,
          arguments
        )
      )
    }
  }

  fun rpc(
    target: ProtocolSide,
    function: String,
    arguments: QVariantList
  ) {
    if (target != protocolSide) {
      emit(
        SignalProxyMessage.Rpc(
          function,
          arguments
        )
      )
    }
  }

  fun emit(message: SignalProxyMessage)
}
