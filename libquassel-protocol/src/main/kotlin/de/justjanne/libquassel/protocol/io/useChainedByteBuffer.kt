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
