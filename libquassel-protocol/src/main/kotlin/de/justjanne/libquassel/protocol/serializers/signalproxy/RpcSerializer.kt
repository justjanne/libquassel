/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.signalproxy

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.SignalProxySerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

/**
 * Serializer for [SignalProxyMessage.Rpc]
 */
object RpcSerializer : SignalProxySerializer<SignalProxyMessage.Rpc> {
  override val type: Int = 2

  override fun serialize(data: SignalProxyMessage.Rpc) = listOf(
    qVariant(type, QtType.Int),
    qVariant(StringSerializerUtf8.serializeRaw(data.slotName), QtType.QByteArray)
  ) + data.params

  override fun deserialize(data: QVariantList) = SignalProxyMessage.Rpc(
    StringSerializerUtf8.deserializeRaw(data.getOrNull(1).into<ByteBuffer>()),
    data.drop(2)
  )
}
