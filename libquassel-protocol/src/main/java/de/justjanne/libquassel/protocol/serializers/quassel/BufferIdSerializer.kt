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

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.QuasselSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.types.BufferId
import de.justjanne.libquassel.protocol.variant.QuasselType
import java.nio.ByteBuffer

object BufferIdSerializer : QuasselSerializer<BufferId> {
  override val quasselType: QuasselType = QuasselType.BufferId
  override val javaType: Class<out BufferId> = BufferId::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: BufferId, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data.id, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): BufferId {
    return BufferId(IntSerializer.deserialize(buffer, featureSet))
  }
}
