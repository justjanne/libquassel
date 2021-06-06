/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.serializers.HandshakeMessageSerializer
import java.nio.ByteBuffer

object HandshakeFuzzTarget {
  @JvmStatic
  fun fuzzerTestOneInput(data: FuzzedDataProvider) {
    val featureSet = if (data.consumeBoolean()) FeatureSet.all() else FeatureSet.none()
    HandshakeMessageSerializer.deserialize(ByteBuffer.wrap(data.consumeRemainingAsBytes()), featureSet)
  }
}
