/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.NetworkInfo
import de.justjanne.libquassel.protocol.models.NetworkServer
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantMapSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

object NetworkInfoSerializer : PrimitiveSerializer<NetworkInfo> {
  override val javaType: Class<NetworkInfo> = NetworkInfo::class.java
  override fun serialize(buffer: ChainedByteBuffer, data: NetworkInfo, featureSet: FeatureSet) {
    QVariantMapSerializer.serialize(buffer, serializeMap(data), featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): NetworkInfo {
    return deserializeMap(QVariantMapSerializer.deserialize(buffer, featureSet))
  }

  fun serializeMap(data: NetworkInfo): QVariantMap = mapOf(
    "NetworkId" to qVariant(data.networkId, QuasselType.NetworkId),
    "NetworkName" to qVariant(data.networkName, QtType.QString),
    "Identity" to qVariant(data.identity, QuasselType.IdentityId),
    "UseCustomEncodings" to qVariant(data.useCustomEncodings, QtType.Bool),
    "CodecForServer" to qVariant(StringSerializerUtf8.serializeRaw(data.codecForServer), QtType.QByteArray),
    "CodecForEncoding" to qVariant(StringSerializerUtf8.serializeRaw(data.codecForEncoding), QtType.QByteArray),
    "CodecForDecoding" to qVariant(StringSerializerUtf8.serializeRaw(data.codecForDecoding), QtType.QByteArray),
    "ServerList" to qVariant(data.serverList, QtType.QVariantList),
    "UseRandomServer" to qVariant(data.useRandomServer, QtType.Bool),
    "Perform" to qVariant(data.perform, QtType.QStringList),
    "UseAutoIdentify" to qVariant(data.useAutoIdentify, QtType.Bool),
    "AutoIdentifyService" to qVariant(data.autoIdentifyService, QtType.QString),
    "AutoIdentifyPassword" to qVariant(data.autoIdentifyPassword, QtType.QString),
    "UseSasl" to qVariant(data.useSasl, QtType.Bool),
    "SaslAccount" to qVariant(data.saslAccount, QtType.QString),
    "SaslPassword" to qVariant(data.saslPassword, QtType.QString),
    "UseAutoReconnect" to qVariant(data.useAutoReconnect, QtType.Bool),
    "AutoReconnectInterval" to qVariant(data.autoReconnectInterval, QtType.UInt),
    "AutoReconnectRetries" to qVariant(data.autoReconnectRetries, QtType.UShort),
    "UnlimitedReconnectRetries" to qVariant(data.unlimitedReconnectRetries, QtType.Bool),
    "RejoinChannels" to qVariant(data.rejoinChannels, QtType.Bool),
    "UseCustomMessageRate" to qVariant(data.useCustomMessageRate, QtType.Bool),
    "MessageRateBurstSize" to qVariant(data.messageRateBurstSize, QtType.UInt),
    "MessageRateDelay" to qVariant(data.messageRateDelay, QtType.UInt),
    "UnlimitedMessageRate" to qVariant(data.unlimitedMessageRate, QtType.Bool)
  )

  fun deserializeMap(data: QVariantMap) = NetworkInfo(
    networkId = data["NetworkId"].into(NetworkId(-1)),
    networkName = data["NetworkName"].into(""),
    identity = data["Identity"].into(IdentityId(-1)),
    useCustomEncodings = data["UseCustomEncodings"].into(false),
    codecForServer = data["CodecForServer"].into("UTF_8"),
    codecForEncoding = data["CodecForEncoding"].into("UTF_8"),
    codecForDecoding = data["CodecForDecoding"].into("UTF_8"),
    serverList = data["ServerList"].into<List<NetworkServer>>().orEmpty(),
    useRandomServer = data["UseRandomServer"].into(false),
    perform = data["Perform"].into<QStringList>().orEmpty().filterNotNull(),
    useAutoIdentify = data["UseAutoIdentify"].into(false),
    autoIdentifyService = data["AutoIdentifyService"].into(""),
    autoIdentifyPassword = data["AutoIdentifyPassword"].into(""),
    useSasl = data["UseSasl"].into(false),
    saslAccount = data["SaslAccount"].into(""),
    saslPassword = data["SaslPassword"].into(""),
    useAutoReconnect = data["UseAutoReconnect"].into(true),
    autoReconnectInterval = data["AutoReconnectInterval"].into(0u),
    autoReconnectRetries = data["AutoReconnectRetries"].into(0u),
    unlimitedReconnectRetries = data["UnlimitedReconnectRetries"].into(true),
    rejoinChannels = data["RejoinChannels"].into(true),
    useCustomMessageRate = data["UseCustomMessageRate"].into(false),
    messageRateBurstSize = data["MessageRateBurstSize"].into(0u),
    messageRateDelay = data["MessageRateDelay"].into(0u),
    unlimitedMessageRate = data["UnlimitedMessageRate"].into(false)
  )
}
