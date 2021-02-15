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
   * IRCv3 capability negotiation, account tracking
   */
  CapNegotiation(0x0020u, QuasselFeature.CapNegotiation),

  /**
   *  IRC server SSL validation
   */
  VerifyServerSSL(0x0040u, QuasselFeature.VerifyServerSSL),

  /**
   *  IRC server custom message rate limits
   */
  CustomRateLimits(0x0080u, QuasselFeature.CustomRateLimits),
  /**
   *  (X)DCC direct file transfers (EXPERIMENTAL)
   */
  DccFileTransfer(0x0100u, QuasselFeature.DccFileTransfer),

  /**
   * Timestamp formatting in away (e.g. %%hh:mm%%)
   */
  AwayFormatTimestamp(0x0200u, QuasselFeature.AwayFormatTimestamp),

  /**
   * Whether or not the core supports auth backends.
   */
  Authenticators(0x0400u, QuasselFeature.Authenticators),

  /**
   * Sync buffer activity status
   */
  BufferActivitySync(0x0800u, QuasselFeature.BufferActivitySync),

  /**
   * Core-Side highlight configuration and matching
   */
  CoreSideHighlights(0x1000u, QuasselFeature.CoreSideHighlights),

  /**
   * Show prefixes for senders in backlog
   */
  SenderPrefixes(0x2000u, QuasselFeature.SenderPrefixes),

  /**
   * Supports RPC call disconnectFromCore to remotely disconnect a client
   */
  RemoteDisconnect(0x4000u, QuasselFeature.RemoteDisconnect),

  /**
   * Transmit features as list of strings
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
