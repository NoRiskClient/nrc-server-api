package gg.norisk.core.manager

import gg.norisk.core.payloads.AbstractPayload
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

object PayloadManager {
    private val playerPayloads = ConcurrentHashMap<UUID, ConcurrentLinkedQueue<AbstractPayload>>()
    private val registeredPayloads = ConcurrentHashMap<String, Class<out AbstractPayload>>()
    private val payloadSendAllowedAt = ConcurrentHashMap<UUID, Long>()

    private const val PAYLOAD_DELAY_MS = 5000L

    private var sendToClientCallback: ((UUID, ByteArray) -> Unit)? = null

    fun registerPayloadType(type: String, clazz: Class<out AbstractPayload>) {
        registeredPayloads[type] = clazz
    }

    fun registerPlayer(uuid: UUID) {
        playerPayloads.putIfAbsent(uuid, ConcurrentLinkedQueue())
    }

    fun unregisterPlayer(uuid: UUID) {
        playerPayloads.remove(uuid)
    }

    fun queuePayload(uuid: UUID, payload: AbstractPayload) {
        playerPayloads.computeIfAbsent(uuid) { ConcurrentLinkedQueue() }.add(payload)
    }

    fun sendToPlayer(uuid: UUID, payload: AbstractPayload, sendToClient: (UUID, ByteArray) -> Unit) {
        val allowedAt = payloadSendAllowedAt[uuid]
        if (allowedAt != null && System.currentTimeMillis() < allowedAt) {
            queuePayload(uuid, payload)
            return
        }
        val data = ChannelApi.send(payload)
        sendToClient(uuid, data)
    }

    fun sendQueuedPayloads(uuid: UUID, sendToClient: (UUID, ByteArray) -> Unit) {
        val allowedAt = payloadSendAllowedAt[uuid]
        if (allowedAt != null && System.currentTimeMillis() < allowedAt) {
            return
        }
        val queue = playerPayloads[uuid] ?: return
        while (queue.isNotEmpty()) {
            val payload = queue.poll()
            if (payload != null) sendToPlayer(uuid, payload, sendToClient)
        }
    }

    fun broadcast(payload: AbstractPayload, sendToClient: (UUID, ByteArray) -> Unit) {
        playerPayloads.keys.forEach { uuid ->
            sendToPlayer(uuid, payload, sendToClient)
        }
    }

    fun setSendToClientCallback(callback: (UUID, ByteArray) -> Unit) {
        sendToClientCallback = callback
    }

    fun onPayloadReceived(sender: UUID, payloadJson: String) {
        if (payloadJson.contains("\"type\":\"handshake\"")) {
            payloadSendAllowedAt[sender] = System.currentTimeMillis() + PAYLOAD_DELAY_MS
            val uuid = sender
            Thread {
                Thread.sleep(PAYLOAD_DELAY_MS)
                payloadSendAllowedAt.remove(uuid)
                val callback = sendToClientCallback
                if (callback != null) {
                    sendQueuedPayloads(uuid, callback)
                } else {
                    System.err.println("[PayloadManager] sendToClientCallback is not set!")
                }
            }.start()
        }
    }
}