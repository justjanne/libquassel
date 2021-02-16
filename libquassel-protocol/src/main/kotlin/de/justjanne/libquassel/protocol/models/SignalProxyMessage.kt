package de.justjanne.libquassel.protocol.models

import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import org.threeten.bp.Instant

/**
 * Model classes for messages exchanged with the signal proxy
 */
sealed class SignalProxyMessage {
  /**
   * Sync message, representing an RPC call on a specific object
   */
  data class Sync(
    /**
     * Type of the specified object
     */
    val className: String,
    /**
     * Name/ID of the specified object
     */
    val objectName: String,
    /**
     * Name of the function/slot to be called on the object
     */
    val slotName: String,
    /**
     * Parameters to the function call
     */
    val params: QVariantList
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "SyncMessage::$className($objectName):$slotName(${params.size})"
    }
  }

  /**
   * RPC message, representing an RPC call on the global client object
   */
  data class Rpc(
    /**
     * Name of the function/slot to be called on the object
     */
    val slotName: String,
    /**
     * Parameters to the function call
     */
    val params: QVariantList
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "RpcCall::$slotName(${params.size})"
    }
  }

  /**
   * Init request message, representing a request to get the current state of a
   * specified object
   */
  data class InitRequest(
    /**
     * Type of the specified object
     */
    val className: String,
    /**
     * Name/ID of the specified object
     */
    val objectName: String
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "InitRequest::$className($objectName)"
    }
  }

  /**
   * Init data message, representing a message with the current state of a
   * specified object
   */
  data class InitData(
    /**
     * Type of the specified object
     */
    val className: String,
    /**
     * Name/ID of the specified object
     */
    val objectName: String,
    /**
     * Current state of the specified object
     */
    val initData: QVariantMap
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "InitData::$className($objectName)"
    }
  }

  /**
   * Heart beat message to keep the connection alive and measure latency
   */
  data class HeartBeat(
    /**
     * Local timestamp of the moment the message was sent
     */
    val timestamp: Instant
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "HeartBeat::$timestamp"
    }
  }

  /**
   * Heart beat reply message as response to [HeartBeat]
   */
  data class HeartBeatReply(
    /**
     * Local timestamp of the moment the original heartbeat was sent
     */
    val timestamp: Instant
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "HeartBeatReply::$timestamp"
    }
  }
}
