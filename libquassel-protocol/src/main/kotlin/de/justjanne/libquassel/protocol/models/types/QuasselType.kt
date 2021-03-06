/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.types

import de.justjanne.libquassel.protocol.serializers.NoSerializerForTypeException
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.BufferIdSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.BufferInfoSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.DccIpDetectionModeSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.DccPortSelectionModeSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.IdentityIdSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.IdentitySerializer
import de.justjanne.libquassel.protocol.serializers.quassel.IrcChannelSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.IrcUserSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.MessageSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.MsgIdSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.NetworkIdSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.NetworkInfoSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.NetworkServerSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.PeerPtrSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.QHostAddressSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.TransferDirectionSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.TransferIdListSerializer
import de.justjanne.libquassel.protocol.serializers.quassel.TransferStatusSerializer

/**
 * Supported quassel types for serialization
 */
enum class QuasselType(
  /**
   * Standardized name of the type
   */
  val typeName: String,
  /**
   * Serializer for data described by this type
   */
  @PublishedApi
  internal val serializer: PrimitiveSerializer<*>? = null,
  /**
   * Actual underlying serialization type
   */
  val qtType: QtType = QtType.UserType
) {
  /**
   * Type for [de.justjanne.libquassel.protocol.models.BufferId]
   */
  BufferId("BufferId", BufferIdSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.BufferInfo]
   */
  BufferInfo("BufferInfo", BufferInfoSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.DccIpDetectionMode]
   */
  DccConfigIpDetectionMode("DccConfig::IpDetectionMode", DccIpDetectionModeSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.DccPortSelectionMode]
   */
  DccConfigPortSelectionMode("DccConfig::PortSelectionMode", DccPortSelectionModeSerializer),

  /**
   * Type for IrcUser objects
   * Serialized as [de.justjanne.libquassel.protocol.variant.QVariantMap]
   */
  IrcUser("IrcUser", IrcUserSerializer),

  /**
   * Type for IrcChannel objects
   * Serialized as [de.justjanne.libquassel.protocol.variant.QVariantMap]
   */
  IrcChannel("IrcChannel", IrcChannelSerializer),

  /**
   * Type for Identity objects
   * Serialized as [de.justjanne.libquassel.protocol.variant.QVariantMap]
   */
  Identity("Identity", IdentitySerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.IdentityId]
   */
  IdentityId("IdentityId", IdentityIdSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.Message]
   */
  Message("Message", MessageSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.MsgId]
   */
  MsgId("MsgId", MsgIdSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.NetworkId]
   */
  NetworkId("NetworkId", NetworkIdSerializer),

  /**
   * Type for NetworkInfo objects
   * Serialized as [de.justjanne.libquassel.protocol.variant.QVariantMap]
   */
  NetworkInfo("NetworkInfo", NetworkInfoSerializer),

  /**
   * Type for NetworkServer objects
   * Serialized as [de.justjanne.libquassel.protocol.variant.QVariantMap]
   */
  NetworkServer("Network::Server", NetworkServerSerializer),

  /**
   * Type for [java.net.InetAddress]
   */
  QHostAddress("QHostAddress", QHostAddressSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.TransferDirection]
   */
  TransferDirection("Transfer::Direction", TransferDirectionSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.TransferIdList]
   */
  TransferIdList("Transfer::TransferIdList", TransferIdListSerializer),

  /**
   * Type for [de.justjanne.libquassel.protocol.models.TransferStatus]
   */
  TransferStatus("Transfer::Status", TransferStatusSerializer),

  /**
   * Serialization type for PeerPtr.
   *
   * This type is only used as placeholder for values that should only be used
   * internally. During serialization it is serialized as a zero-valued 64-bit
   * unsigned integer, during deserialization it is replaced by an object
   * representing the RPC interface of the remote peer.
   *
   * This is used in the core to return responses to the same client that made
   * a request.
   */
  PeerPtr("PeerPtr", PeerPtrSerializer);

  /**
   * Obtain a serializer for this type (type safe)
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T> serializer(): PrimitiveSerializer<T> {
    if (serializer?.javaType?.isAssignableFrom(T::class.java) == true) {
      return serializer as PrimitiveSerializer<T>
    } else {
      throw NoSerializerForTypeException.Quassel(this, T::class.java)
    }
  }

  companion object {
    private val values = enumValues<QuasselType>()
      .associateBy(QuasselType::typeName)

    /**
     * Obtain a QtType by its standardized name
     */
    fun of(typeName: String?): QuasselType? = values[typeName]
  }
}
