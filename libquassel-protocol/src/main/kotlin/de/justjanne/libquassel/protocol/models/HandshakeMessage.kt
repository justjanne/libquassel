/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap

/**
 * Model classes for messages exchanged during handshake
 */
sealed class HandshakeMessage {
  /**
   * Client registration message containing metadata about the connecting
   * client.
   *
   * Core should respond with either [ClientInitAck] or [ClientInitReject].
   */
  data class ClientInit(
    /**
     * Human readable (HTML formatted) version of the client
     */
    val clientVersion: String?,
    /**
     * Build timestamp of the client
     */
    val buildDate: String?,
    /**
     * Enabled client features for this connection
     */
    val featureSet: FeatureSet,
  ) : HandshakeMessage()

  /**
   * Message representing a successful client registration attempt. Contains
   * metadata about the core.
   *
   * Client should proceed with either [ClientLogin] or [CoreSetupData].
   */
  data class ClientInitAck(
    /**
     * Whether or not the core needs to be configured via the quassel protocol
     */
    val coreConfigured: Boolean?,
    /**
     * If the core needs to be configured, this contains metadata about the
     * supported storage backends
     */
    val backendInfo: List<BackendInfo>,
    /**
     * If the core needs to be configured, this contains metadata about the
     * supported authentication backends
     */
    val authenticatorInfo: List<BackendInfo>,
    /**
     * Enabled features for this connection
     */
    val featureSet: FeatureSet,
  ) : HandshakeMessage()

  /**
   * Message representing a failed client registration attempt.
   *
   * Client should abort the connection.
   */
  data class ClientInitReject(
    /**
     * HTML-formatted error message
     */
    val errorString: String?
  ) : HandshakeMessage()

  /**
   * Client login message containing authentication data.
   *
   * Core should respond with either [ClientLoginAck] or [ClientLoginReject].
   */
  data class ClientLogin(
    /**
     * Username of the core account
     */
    val user: String?,
    /**
     * Password of the core account
     */
    val password: String?
  ) : HandshakeMessage()

  /**
   * Message representing a successful client login attempt.
   *
   * Client will receive [SessionInit] immediately afterwards.
   */
  object ClientLoginAck : HandshakeMessage() {
    override fun toString(): String {
      return "ClientLoginAck"
    }
  }

  /**
   * Message representing a failed client login attempt.
   *
   * Client should retry with different [ClientLogin] or abort the connection.
   */
  data class ClientLoginReject(
    /**
     * HTML-formatted error message
     */
    val errorString: String?
  ) : HandshakeMessage()

  /**
   * Message representing a successful core configuration attempt.
   *
   * Client should proceed with [ClientLogin].
   */
  object CoreSetupAck : HandshakeMessage() {
    override fun toString(): String {
      return "CoreSetupAck"
    }
  }

  /**
   * Core configuration message containing initial configuration properties.
   * Configuration has to happen before login.
   *
   * Core should respond with either [CoreSetupAck] or [CoreSetupReject].
   */
  data class CoreSetupData(
    /**
     * Username of a new core account to be created
     */
    val adminUser: String?,
    /**
     * Password of a new core account to be created
     */
    val adminPassword: String?,
    /**
     * Chosen storage backend id
     */
    val backend: String?,
    /**
     * Storage backend configuration data
     */
    val setupData: QVariantMap,
    /**
     * Chosen authenticator backend id
     */
    val authenticator: String?,
    /**
     * Authenticator backend configuration data
     */
    val authSetupData: QVariantMap
  ) : HandshakeMessage()

  /**
   * Message representing a failed core configuration attempt.
   *
   * Client should retry with different [CoreSetupData] or abort the connection.
   */
  data class CoreSetupReject(
    /**
     * HTML-formatted error message
     */
    val errorString: String?
  ) : HandshakeMessage()

  /**
   * Initial session data for the client
   */
  data class SessionInit(
    /**
     * List of Identity sync objects existing at the current time.
     *
     * Identity objects created or modified after [SessionInit] will be defined
     * via sync updates and RPC identity creation messages.
     */
    val identities: List<QVariantMap>,
    /**
     * List of existing buffers at the current time.
     *
     * Buffers created or deleted after [SessionInit] will be defined via RPC
     * messages
     */
    val bufferInfos: List<BufferInfo>,
    /**
     * List of Ids of Network sync objects existing at the current time.
     *
     * Network objects created or modified after [SessionInit] will be defined
     * via sync updates and RPC identity creation messages.
     */
    val networkIds: List<NetworkId>
  ) : HandshakeMessage()
}
