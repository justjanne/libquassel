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
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantMapSerializer
import de.justjanne.libquassel.protocol.syncables.IrcUser
import de.justjanne.libquassel.protocol.variant.QVariantMap
import java.nio.ByteBuffer

/**
 * Serializer for [QVariantMap], with custom name for [IrcUser]
 */
object IrcUserSerializer : PrimitiveSerializer<QVariantMap> {

  @Suppress("UNCHECKED_CAST")
  override val javaType: Class<out QVariantMap> = Map::class.java as Class<QVariantMap>

  override fun serialize(buffer: ChainedByteBuffer, data: QVariantMap, featureSet: FeatureSet) =
    QVariantMapSerializer.serialize(buffer, data, featureSet)

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): QVariantMap =
    QVariantMapSerializer.deserialize(buffer, featureSet)
}
