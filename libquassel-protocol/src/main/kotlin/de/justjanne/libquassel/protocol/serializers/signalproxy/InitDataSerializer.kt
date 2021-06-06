/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
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
import de.justjanne.libquassel.protocol.variant.toVariantList
import de.justjanne.libquassel.protocol.variant.toVariantMap
import java.nio.ByteBuffer

/**
 * Serializer for [SignalProxyMessage.InitData]
 */
object InitDataSerializer : SignalProxySerializer<SignalProxyMessage.InitData> {
  override val type: Int = 4

  override fun serialize(data: SignalProxyMessage.InitData) = listOf(
    qVariant(type, QtType.Int),
    qVariant(StringSerializerUtf8.serializeRaw(data.className), QtType.QByteArray),
    qVariant(StringSerializerUtf8.serializeRaw(data.objectName), QtType.QByteArray),
  ) + data.initData.toVariantList()

  override fun deserialize(data: QVariantList) = SignalProxyMessage.InitData(
    StringSerializerUtf8.deserializeRaw(data.getOrNull(1).into<ByteBuffer>()),
    StringSerializerUtf8.deserializeRaw(data.getOrNull(2).into<ByteBuffer>()),
    data.drop(3).toVariantMap(byteBuffer = true)
  )
}
