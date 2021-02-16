/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.features

import de.justjanne.bitflags.of

/**
 * Model representing the set of negotiated or supported features for a
 * connection or library
 */
data class FeatureSet internal constructor(
  private val features: Set<QuasselFeature>,
  private val additional: Set<QuasselFeatureName> = emptySet()
) {
  /**
   * Check whether a certain feature is supported
   */
  fun hasFeature(feature: QuasselFeature) = features.contains(feature)

  /**
   * List of features with their name, for serialization
   */
  fun featureList(): List<QuasselFeatureName> =
    features.map(QuasselFeature::feature) + additional

  /**
   * Set of supported [LegacyFeature]s
   */
  fun legacyFeatures(): LegacyFeatures =
    LegacyFeature.of(features.mapNotNull(LegacyFeature.Companion::get))

  companion object {
    /**
     * Build a [FeatureSet] from a collection of feature names and a set of
     * legacy features
     */
    fun build(
      legacy: LegacyFeatures,
      features: Collection<QuasselFeatureName>
    ) = FeatureSet(
      features = parseFeatures(legacy) + parseFeatures(features),
      additional = unknownFeatures(features)
    )

    /**
     * Build a [FeatureSet] directly from features
     */
    fun build(vararg features: QuasselFeature) = FeatureSet(features.toSet())

    /**
     * Build a [FeatureSet] directly from features
     */
    fun build(features: Set<QuasselFeature>) = FeatureSet(features)

    /**
     * Model with all possible features enabled
     */
    fun all() = build(*QuasselFeature.values())

    /**
     * Model with no features enabled
     */
    fun none() = build()

    private fun parseFeatures(features: LegacyFeatures) =
      features.map(LegacyFeature::feature).toSet()

    private fun parseFeatures(features: Collection<QuasselFeatureName>) =
      features.mapNotNull(QuasselFeature.Companion::valueOf).toSet()

    private fun unknownFeatures(features: Collection<QuasselFeatureName>) =
      features.filter { QuasselFeature.valueOf(it) == null }.toSet()
  }
}
