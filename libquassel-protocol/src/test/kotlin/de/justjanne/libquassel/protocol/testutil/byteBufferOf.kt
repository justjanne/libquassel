/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.testutil

import java.nio.ByteBuffer

@Suppress("NOTHING_TO_INLINE")
inline fun byteBufferOf(
  vararg elements: Byte
): ByteBuffer = ByteBuffer.wrap(byteArrayOf(*elements))

@Suppress("NOTHING_TO_INLINE")
inline fun byteBufferOf(
  vararg elements: UByte
): ByteBuffer = ByteBuffer.wrap(ubyteArrayOf(*elements).toByteArray())

@Suppress("NOTHING_TO_INLINE")
inline fun byteBufferOf(): ByteBuffer =
  ByteBuffer.allocateDirect(0)
