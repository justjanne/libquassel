/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.dcc

enum class TransferStatus(
  val value: Int,
) {
  New(0),
  Pending(1),
  Connecting(2),
  Transferring(3),
  Paused(4),
  Completed(5),
  Failed(6),
  Rejected(7);

  companion object {
    private val values = enumValues<TransferStatus>()
      .associateBy(TransferStatus::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: Int): TransferStatus? = values[value]
  }
}
