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

enum class ConnectionState(
  /**
   * Underlying representation
   */
  val value: Int,
) {
  Disconnected(0),
  Connecting(1),
  Initializing(2),
  Initialized(3),
  Reconnecting(4),
  Disconnecting(5);

  companion object {
    private val values = enumValues<ConnectionState>()
      .associateBy(ConnectionState::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: Int): ConnectionState? = values[value]
  }
}
