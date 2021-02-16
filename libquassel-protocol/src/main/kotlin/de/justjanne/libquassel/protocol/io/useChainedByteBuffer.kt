/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import java.nio.ByteBuffer

/**
 * Utility function to apply a closure to a chained byte buffer and return its data
 */
inline fun useChainedByteBuffer(
  function: (ChainedByteBuffer) -> Unit
): ByteBuffer {
  val buffer = ChainedByteBuffer()
  function(buffer)
  return buffer.toBuffer()
}
