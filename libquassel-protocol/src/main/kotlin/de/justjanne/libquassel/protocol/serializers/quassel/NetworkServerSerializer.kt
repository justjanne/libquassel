/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.network.NetworkProxy
import de.justjanne.libquassel.protocol.models.network.NetworkServer
import de.justjanne.libquassel.protocol.models.network.PortDefaults
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantMapSerializer
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

object NetworkServerSerializer : PrimitiveSerializer<NetworkServer> {
  override val javaType: Class<NetworkServer> = NetworkServer::class.java
  override fun serialize(buffer: ChainedByteBuffer, data: NetworkServer, featureSet: FeatureSet) {
    QVariantMapSerializer.serialize(buffer, serializeMap(data), featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): NetworkServer {
    return deserializeMap(QVariantMapSerializer.deserialize(buffer, featureSet))
  }

  fun serializeMap(data: NetworkServer): QVariantMap = mapOf(
    "Host" to qVariant(data.host, QtType.QString),
    "Port" to qVariant(data.port, QtType.UInt),
    "Password" to qVariant(data.password, QtType.QString),
    "UseSSL" to qVariant(data.useSsl, QtType.Bool),
    "sslVerify" to qVariant(data.sslVerify, QtType.Bool),
    "sslVersion" to qVariant(data.sslVersion, QtType.Int),
    "UseProxy" to qVariant(data.useProxy, QtType.Bool),
    "ProxyType" to qVariant(data.proxyType, QtType.Int),
    "ProxyHost" to qVariant(data.proxyHost, QtType.QString),
    "ProxyPort" to qVariant(data.proxyPort, QtType.UInt),
    "ProxyUser" to qVariant(data.proxyUser, QtType.QString),
    "ProxyPass" to qVariant(data.proxyPass, QtType.QString)
  )

  fun deserializeMap(data: QVariantMap) = NetworkServer(
    host = data["Host"].into(""),
    port = data["Port"].into(PortDefaults.PORT_PLAINTEXT.port),
    password = data["Password"].into(""),
    useSsl = data["UseSSL"].into(false),
    sslVerify = data["sslVerify"].into(false),
    sslVersion = data["sslVersion"].into(0),
    useProxy = data["UseProxy"].into(false),
    proxyType = NetworkProxy.of(data["ProxyType"].into(NetworkProxy.Socks5Proxy.value))
      ?: NetworkProxy.Socks5Proxy,
    proxyHost = data["ProxyHost"].into("localhost"),
    proxyPort = data["ProxyPort"].into(8080u),
    proxyUser = data["ProxyUser"].into(""),
    proxyPass = data["ProxyPass"].into("")
  )
}
