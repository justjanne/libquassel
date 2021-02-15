/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.justjanne.libquassel.protocol.variant

/**
 * Supported quassel types for serialization
 */
enum class QuasselType(
  /**
   * Standardized name of the type
   */
  val typeName: String,
  /**
   * Actual underlying serialization type
   */
  val qtType: QtType = QtType.UserType,
) {
  /**
   * Type for [de.justjanne.libquassel.protocol.types.BufferId]
   */
  BufferId("BufferId"),

  /**
   * Type for [de.justjanne.libquassel.protocol.types.BufferInfo]
   */
  BufferInfo("BufferInfo"),

  /**
   * Type for [de.justjanne.libquassel.protocol.types.DccIpDetectionMode]
   */
  DccConfigIpDetectionMode("DccConfig::IpDetectionMode"),

  /**
   * Type for [de.justjanne.libquassel.protocol.types.DccPortSelectionMode]
   */
  DccConfigPortSelectionMode("DccConfig::PortSelectionMode"),

  /**
   * Type for IrcUser objects
   * Serialized as [QVariantMap]
   */
  IrcUser("IrcUser"),
  /**
   * Type for IrcChannel objects
   * Serialized as [QVariantMap]
   */
  IrcChannel("IrcChannel"),
  /**
   * Type for Identity objects
   * Serialized as [QVariantMap]
   */
  Identity("Identity"),
  /**
   * Type for [de.justjanne.libquassel.protocol.types.IdentityId]
   */
  IdentityId("IdentityId"),

  /**
   * Type for [de.justjanne.libquassel.protocol.types.Message]
   */
  Message("Message"),

  /**
   * Type for [de.justjanne.libquassel.protocol.types.MsgId]
   */
  MsgId("MsgId"),

  /**
   * Type for [de.justjanne.libquassel.protocol.types.NetworkId]
   */
  NetworkId("NetworkId"),
  /**
   * Type for NetworkInfo objects
   * Serialized as [QVariantMap]
   */
  NetworkInfo("NetworkInfo"),
  /**
   * Type for NetworkServer objects
   * Serialized as [QVariantMap]
   */
  NetworkServer("Network::Server"),

  /**
   * Type for [java.net.InetAddress]
   */
  QHostAddress("QHostAddress"),

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
  PeerPtr("PeerPtr");

  companion object {
    private val values = values().associateBy(QuasselType::typeName)
    /**
     * Obtain a QtType by its standardized name
     */
    fun of(typeName: String?): QuasselType? = values[typeName]
  }
}
