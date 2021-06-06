/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import java.io.OutputStream
import java.net.SocketException
import java.util.zip.DeflaterOutputStream

/**
 * Wrapper class around a [DeflaterOutputStream] correctly handling closing of
 * the current stream
 */
class FixedDeflaterOutputStream(
  stream: OutputStream
) : DeflaterOutputStream(stream, true) {
  /**
   * Close the underlying stream and deflater
   */
  override fun close() {
    try {
      super.close()
    } catch (e: SocketException) {
      // ignored
    } finally {
      def.end()
    }
  }
}
