/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.features

import de.justjanne.bitflags.Flag
import de.justjanne.bitflags.Flags
import de.justjanne.bitflags.toEnumSet

/**
 * List of optional features in a quassel connection.
 *
 * Replaced by [QuasselFeature] negotiation if both sides of the connection are
 * new enough
 */
enum class LegacyFeature(
  override val value: UInt,
  /**
   * Modern feature corresponding to this legacy feature
   */
  val feature: QuasselFeature,
) : Flag<UInt> {
  /**
   * Support for a synced marker line/last read position
   */
  SynchronizedMarkerLine(0x0001u, QuasselFeature.SynchronizedMarkerLine),

  /**
   * Support for configurable SASL authentication to replace NickServ
   */
  SaslAuthentication(0x0002u, QuasselFeature.SaslAuthentication),

  /**
   * Support for certificate based SASL EXTERNAL authentication
   */
  SaslExternal(0x0004u, QuasselFeature.SaslExternal),

  /**
   * Support for hiding inactive/disconnected networks in a chat list
   */
  HideInactiveNetworks(0x0008u, QuasselFeature.HideInactiveNetworks),

  /**
   * Support for changing the password through the client
   */
  PasswordChange(0x0010u, QuasselFeature.PasswordChange),

  /**
   * Support for IRCv3 capabilities and the IRCv3 account tag
   */
  CapNegotiation(0x0020u, QuasselFeature.CapNegotiation),

  /**
   *  Support for validating the TLS connection to IRC servers
   */
  VerifyServerSSL(0x0040u, QuasselFeature.VerifyServerSSL),

  /**
   *  Support for custom rate limits for connections to IRC servers
   */
  CustomRateLimits(0x0080u, QuasselFeature.CustomRateLimits),
  /**
   *  Experimental support for (X)DCC transfers
   */
  DccFileTransfer(0x0100u, QuasselFeature.DccFileTransfer),

  /**
   * Support for time formatting in away messages using QDateTime format
   * surrounded by %%h:m:s ap%%
   *
   * See [QDateTime Documentation](https://doc.qt.io/qt-5/qdatetime.html#toString)
   */
  AwayFormatTimestamp(0x0200u, QuasselFeature.AwayFormatTimestamp),

  /**
   * Support for customizable authentication backends
   */
  Authenticators(0x0400u, QuasselFeature.Authenticators),

  /**
   * Support for syncing the unread state of buffers
   */
  BufferActivitySync(0x0800u, QuasselFeature.BufferActivitySync),

  /**
   * Support for handling and configuring highlights via the core
   */
  CoreSideHighlights(0x1000u, QuasselFeature.CoreSideHighlights),

  /**
   * Support for showing sender prefix modes in backlog messages
   */
  SenderPrefixes(0x2000u, QuasselFeature.SenderPrefixes),

  /**
   * Support for remotely disconnecting other clients of the same user via
   * disconnectFromCore
   */
  RemoteDisconnect(0x4000u, QuasselFeature.RemoteDisconnect),

  /**
   * Support for feature negotiation via a list of strings
   */
  ExtendedFeatures(0x8000u, QuasselFeature.ExtendedFeatures);

  companion object : Flags<UInt, LegacyFeature> {
    private val features = values().associateBy(LegacyFeature::feature)

    /**
     * Obtain by modern feature corresponding to this feature
     */
    fun get(feature: QuasselFeature) = features[feature]

    private val values = values().associateBy(LegacyFeature::value)
    override val all: LegacyFeatures = values.values.toEnumSet()
  }
}

/**
 * Model representing a bitfield of [LegacyFeature] flags
 */
typealias LegacyFeatures = Set<LegacyFeature>
