/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util

import java.nio.Buffer

/**
 * Utility function wrapping the [Buffer.position] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withPosition(position: Int): T {
  position(position)
  return this
}

/**
 * Utility function wrapping the [Buffer.limit] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withLimit(limit: Int): T {
  limit(limit)
  return this
}

/**
 * Utility function wrapping the [Buffer.mark] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withMark(): T {
  mark()
  return this
}

/**
 * Utility function wrapping the [Buffer.reset] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withReset(): T {
  reset()
  return this
}

/**
 * Utility function wrapping the [Buffer.clear] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withClear(): T {
  clear()
  return this
}

/**
 * Utility function wrapping the [Buffer.flip] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withFlip(): T {
  flip()
  return this
}

/**
 * Utility function wrapping the [Buffer.rewind] call on older JDKs where it
 * always returns a plain Buffer instead of the correct type
 */
inline fun <reified T : Buffer> T.withRewind(): T {
  rewind()
  return this
}
