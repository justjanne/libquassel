package de.justjanne.libquassel.protocol.models

import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import org.threeten.bp.Instant

sealed class SignalProxyMessage {
  data class Sync(
    val className: String,
    val objectName: String,
    val slotName: String,
    val params: QVariantList
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "SyncMessage::$className($objectName):$slotName(${params.size})"
    }
  }

  data class RpcCall(
    val slotName: String,
    val params: QVariantList
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "RpcCall::$slotName(${params.size})"
    }
  }

  data class InitRequest(
    val className: String,
    val objectName: String
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "InitRequest::$className($objectName)"
    }
  }

  data class InitData(
    val className: String,
    val objectName: String,
    val initData: QVariantMap
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "InitData::$className($objectName)"
    }
  }

  data class HeartBeat(
    val timestamp: Instant
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "HeartBeat::$timestamp"
    }
  }

  data class HeartBeatReply(
    val timestamp: Instant
  ) : SignalProxyMessage() {
    override fun toString(): String {
      return "HeartBeatReply::$timestamp"
    }
  }
}
