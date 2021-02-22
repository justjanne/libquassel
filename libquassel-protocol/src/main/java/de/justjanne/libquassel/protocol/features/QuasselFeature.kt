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

import de.justjanne.libquassel.annotations.Generated

@Generated
inline class QuasselFeatureName(
  val name: String,
)

enum class QuasselFeature {
  SynchronizedMarkerLine,
  SaslAuthentication,
  SaslExternal,
  HideInactiveNetworks,
  PasswordChange,

  /** IRCv3 capability negotiation, account tracking */
  CapNegotiation,

  /** IRC server SSL validation */
  VerifyServerSSL,

  /** IRC server custom message rate limits */
  CustomRateLimits,

  // Currently not supported
  DccFileTransfer,

  /** Timestamp formatting in away (e.g. %%hh:mm%%) */
  AwayFormatTimestamp,

  /** Whether or not the core supports auth backends. */
  Authenticators,

  /** Sync buffer activity status */
  BufferActivitySync,

  /** Core-Side highlight configuration and matching */
  CoreSideHighlights,

  /** Show prefixes for senders in backlog */
  SenderPrefixes,

  /** Supports RPC call disconnectFromCore to remotely disconnect a client */
  RemoteDisconnect,

  /** Transmit features as list of strings */
  ExtendedFeatures,

  /** Serialize message time as 64-bit */
  LongTime,

  /** Real Name and Avatar URL in backlog */
  RichMessages,

  /** Backlogmanager supports filtering backlog by messagetype */
  BacklogFilterType,

  /** ECDSA keys for CertFP in identities */
  EcdsaCertfpKeys,

  /** 64-bit IDs for messages */
  LongMessageId,

  /** CoreInfo dynamically updated using signals */
  SyncedCoreInfo;

  val feature = QuasselFeatureName(name)

  companion object {
    private val values = values().associateBy(QuasselFeature::feature)

    @JvmStatic
    fun valueOf(name: QuasselFeatureName): QuasselFeature? = values[name]
  }
}
