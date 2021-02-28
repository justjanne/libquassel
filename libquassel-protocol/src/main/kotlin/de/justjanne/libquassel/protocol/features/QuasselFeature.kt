/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.features

/**
 * List of optional features in a quassel connection.
 *
 * Replaces [LegacyFeature] negotiation if both sides of the connection are new
 * enough
 */
enum class QuasselFeature {
  /**
   * Support for a synced marker line/last read position
   */
  SynchronizedMarkerLine,

  /**
   * Support for configurable SASL authentication to replace NickServ
   */
  SaslAuthentication,

  /**
   * Support for certificate based SASL EXTERNAL authentication
   */
  SaslExternal,

  /**
   * Support for hiding inactive/disconnected networks in a chat list
   */
  HideInactiveNetworks,

  /**
   * Support for changing the password through the client
   */
  PasswordChange,

  /**
   * Support for IRCv3 capabilities and the IRCv3 account tag
   */
  CapNegotiation,

  /**
   * Support for validating the TLS connection to IRC servers
   */
  VerifyServerSSL,

  /**
   * Support for custom rate limits for connections to IRC servers
   */
  CustomRateLimits,

  /**
   *  Experimental support for (X)DCC transfers
   */
  DccFileTransfer,

  /**
   * Support for time formatting in away messages using QDateTime format
   * surrounded by %%h:m:s ap%%
   *
   * See [QDateTime Documentation](https://doc.qt.io/qt-5/qdatetime.html#toString)
   */
  AwayFormatTimestamp,

  /**
   * Support for customizable authentication backends
   */
  Authenticators,

  /**
   * Support for syncing the unread state of buffers
   */
  BufferActivitySync,

  /**
   * Support for handling and configuring highlights via the core
   */
  CoreSideHighlights,

  /**
   * Support for showing sender prefix modes in backlog messages
   */
  SenderPrefixes,

  /**
   * Support for remotely disconnecting other clients of the same user via
   * disconnectFromCore
   */
  RemoteDisconnect,

  /**
   * Support for feature negotiation via a list of strings
   */
  ExtendedFeatures,

  /**
   * Support for 64-bit message time
   */
  LongTime,

  /**
   * Support for sender avatar and realname in backlog messages
   */
  RichMessages,

  /**
   * Support for loading backlog filtered by
   * [de.justjanne.libquassel.protocol.models.flags.MessageType] and
   * [de.justjanne.libquassel.protocol.models.flags.MessageFlag]
   */
  BacklogFilterType,

  /**
   * Support for ECDSA keys for SASL EXTERNAL in identities
   */
  EcdsaCertfpKeys,

  /**
   * Support for 64-bit message ids
   */
  LongMessageId,

  /**
   * Support for dynamically updated core information
   */
  SyncedCoreInfo;

  /**
   * Get the standardized feature name
   */
  val feature = QuasselFeatureName(name)

  companion object {
    private val values = values().associateBy(QuasselFeature::feature)

    /**
     * Get a [QuasselFeature], if existing, for a certain feature name
     */
    @JvmStatic
    fun valueOf(name: QuasselFeatureName): QuasselFeature? = values[name]
  }
}
