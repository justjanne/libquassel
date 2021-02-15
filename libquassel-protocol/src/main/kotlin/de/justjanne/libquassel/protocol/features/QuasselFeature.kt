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
   * IRCv3 capability negotiation, account tracking
   */
  CapNegotiation,

  /**
   * IRC server SSL validation
   */
  VerifyServerSSL,

  /**
   * IRC server custom message rate limits
   */
  CustomRateLimits,

  /**
   *  (X)DCC direct file transfers (EXPERIMENTAL)
   */
  DccFileTransfer,

  /**
   * Timestamp formatting in away (e.g. %%hh:mm%%)
   */
  AwayFormatTimestamp,

  /**
   * Whether or not the core supports auth backends.
   */
  Authenticators,

  /**
   * Sync buffer activity status
   */
  BufferActivitySync,

  /**
   * Core-Side highlight configuration and matching
   */
  CoreSideHighlights,

  /**
   * Show prefixes for senders in backlog
   */
  SenderPrefixes,

  /**
   * Supports RPC call disconnectFromCore to remotely disconnect a client
   */
  RemoteDisconnect,

  /**
   * Transmit features as list of strings
   */
  ExtendedFeatures,

  /**
   * Serialize message time as 64-bit
   */
  LongTime,

  /**
   * Real Name and Avatar URL in backlog
   */
  RichMessages,

  /**
   * Backlogmanager supports filtering backlog by messagetype
   */
  BacklogFilterType,

  /**
   * ECDSA keys for CertFP in identities
   */
  EcdsaCertfpKeys,

  /**
   * 64-bit IDs for messages
   */
  LongMessageId,

  /**
   * CoreInfo dynamically updated using signals
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
