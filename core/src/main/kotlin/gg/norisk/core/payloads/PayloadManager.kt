package gg.norisk.core.payloads

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

object PayloadManager {
    private val playerPayloads = ConcurrentHashMap<UUID, ConcurrentLinkedQueue<AbstractPayload>>()
    private val registeredPayloads = ConcurrentHashMap<String, Class<out AbstractPayload>>()

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
        val data = ChannelApi.send(payload)
        sendToClient(uuid, data)
    }

    fun sendQueuedPayloads(uuid: UUID, sendToClient: (UUID, ByteArray) -> Unit) {
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

    fun onPayloadReceived(sender: UUID, payloadJson: String) {
    }
}
