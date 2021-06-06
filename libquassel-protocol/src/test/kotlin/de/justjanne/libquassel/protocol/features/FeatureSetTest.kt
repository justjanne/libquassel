/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.features

import de.justjanne.bitflags.none
import de.justjanne.bitflags.of
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FeatureSetTest {
  @Test
  fun testParse() {
    assertEquals(
      emptyList<QuasselFeatureName>(),
      FeatureSet.build(
        LegacyFeature.none(),
        emptyList()
      ).featureList()
    )

    assertEquals(
      listOf(
        QuasselFeature.SynchronizedMarkerLine.feature,
        QuasselFeature.ExtendedFeatures.feature,
        QuasselFeatureName("_unknownFeature")
      ),
      FeatureSet.build(
        LegacyFeature.of(
          LegacyFeature.SynchronizedMarkerLine
        ),
        listOf(
          QuasselFeature.ExtendedFeatures.feature,
          QuasselFeatureName("_unknownFeature")
        )
      ).featureList()
    )
  }

  @Test
  fun testBuild() {
    assertEquals(
      emptyList<QuasselFeatureName>(),
      FeatureSet.build(emptySet()).featureList()
    )

    assertEquals(
      listOf(
        QuasselFeature.SynchronizedMarkerLine.feature,
        QuasselFeature.ExtendedFeatures.feature
      ),
      FeatureSet.build(
        setOf(
          QuasselFeature.SynchronizedMarkerLine,
          QuasselFeature.ExtendedFeatures
        )
      ).featureList()
    )
  }
}
