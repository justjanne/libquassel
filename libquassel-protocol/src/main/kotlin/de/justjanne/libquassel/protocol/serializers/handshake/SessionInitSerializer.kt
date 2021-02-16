/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.HandshakeSerializer
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

/**
 * Serializer for [HandshakeMessage.SessionInit]
 */
object SessionInitSerializer : HandshakeSerializer<HandshakeMessage.SessionInit> {
  override val type: String = "SessionInit"

  override fun serialize(data: HandshakeMessage.SessionInit) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
    "SessionState" to qVariant(
      mapOf(
        "BufferInfos" to qVariant<QVariantList>(
          data.bufferInfos.map {
            qVariant(it, QuasselType.BufferInfo)
          },
          QtType.QVariantList
        ),
        "NetworkIds" to qVariant<QVariantList>(
          data.networkIds.map {
            qVariant(it, QuasselType.NetworkId)
          },
          QtType.QVariantList
        ),
        "Identities" to qVariant(data.identities, QtType.QVariantList),
      ),
      QtType.QVariantMap
    )
  )

  override fun deserialize(data: QVariantMap) = data["SessionState"].into<QVariantMap>().let {
    HandshakeMessage.SessionInit(
      bufferInfos = it?.get("BufferInfos").into<QVariantList>()?.mapNotNull {
        it.into<BufferInfo>()
      }.orEmpty(),
      networkIds = it?.get("NetworkIds").into<QVariantList>()?.mapNotNull {
        it.into<NetworkId>()
      }.orEmpty(),
      identities = it?.get("Identities").into(),
    )
  }
}
