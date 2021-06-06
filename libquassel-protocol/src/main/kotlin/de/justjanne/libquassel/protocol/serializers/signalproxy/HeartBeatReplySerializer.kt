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
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset

/**
 * Serializer for [SignalProxyMessage.HeartBeatReply]
 */
object HeartBeatReplySerializer : SignalProxySerializer<SignalProxyMessage.HeartBeatReply> {
  override val type: Int = 6

  override fun serialize(data: SignalProxyMessage.HeartBeatReply) = listOf(
    qVariant(type, QtType.Int),
    qVariant(data.timestamp, QtType.QDateTime)
  )

  override fun deserialize(data: QVariantList) = SignalProxyMessage.HeartBeatReply(
    data.getOrNull(1).into(Instant.EPOCH.atOffset(ZoneOffset.UTC)).toInstant()
  )
}
