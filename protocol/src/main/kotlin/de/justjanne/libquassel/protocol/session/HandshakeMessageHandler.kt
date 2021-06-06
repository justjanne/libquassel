/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.session

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.variant.QVariantMap

interface HandshakeMessageHandler {
  /**
   * Register client and start connection
   */
  suspend fun init(
    /**
     * Human readable (HTML formatted) version of the client
     */
    clientVersion: String,
    /**
     * Build timestamp of the client
     */
    buildDate: String,
    /**
     * Enabled client features for this connection
     */
    featureSet: FeatureSet
  )

  /**
   * Login to core with authentication data
   */
  suspend fun login(
    /**
     * Username of the core account
     */
    username: String,
    /**
     * Password of the core account
     */
    password: String
  )

  /**
   * Configure core for the first time
   */
  suspend fun configureCore(
    /**
     * Username of a new core account to be created
     */
    adminUsername: String,
    /**
     * Password of a new core account to be created
     */
    adminPassword: String,
    /**
     * Chosen storage backend id
     */
    backend: String,
    /**
     * Storage backend configuration data
     */
    backendConfiguration: QVariantMap,
    /**
     * Chosen authenticator backend id
     */
    authenticator: String,
    /**
     * Authenticator backend configuration data
     */
    authenticatorConfiguration: QVariantMap
  )
}
