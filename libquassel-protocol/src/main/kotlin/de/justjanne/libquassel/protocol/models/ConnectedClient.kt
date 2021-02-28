/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.features.LegacyFeature
import de.justjanne.libquassel.protocol.features.QuasselFeatureName
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import org.threeten.bp.Instant

data class ConnectedClient(
  val id: Int,
  val remoteAddress: String,
  val location: String,
  val version: String,
  val versionDate: Instant?,
  val connectedSince: Instant,
  val secure: Boolean,
  val features: FeatureSet
) {
  fun toVariantMap() = mapOf(
    "id" to qVariant(id, QtType.Int),
    "remoteAddress" to qVariant(remoteAddress, QtType.QString),
    "location" to qVariant(location, QtType.QString),
    "clientVersion" to qVariant(version, QtType.QString),
    "clientVersionDate" to qVariant(versionDate?.epochSecond?.toString(), QtType.QString),
    "connectedSince" to qVariant(connectedSince, QtType.QDateTime),
    "secure" to qVariant(secure, QtType.Bool),
    "features" to qVariant(features.legacyFeatures().toBits(), QtType.UInt),
    "featureList" to qVariant(features.featureList().map(QuasselFeatureName::name), QtType.QStringList)
  )

  companion object {
    fun fromVariantMap(properties: QVariantMap) = ConnectedClient(
      id = properties["id"].into(-1),
      remoteAddress = properties["remoteAddress"].into(""),
      location = properties["location"].into(""),
      version = properties["clientVersion"].into(""),
      versionDate = properties["clientVersionDate"].into("")
        .toLongOrNull()
        ?.let(Instant::ofEpochSecond),
      connectedSince = properties["connectedSince"].into(Instant.EPOCH),
      secure = properties["secure"].into(false),
      features = FeatureSet.build(
        LegacyFeature.of(properties["features"].into()),
        properties["featureList"].into<QStringList>()
          ?.filterNotNull()
          ?.map(::QuasselFeatureName)
          .orEmpty()
      )
    )
  }
}
