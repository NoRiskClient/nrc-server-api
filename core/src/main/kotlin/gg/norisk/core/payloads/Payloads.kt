package gg.norisk.core.payloads

import com.google.gson.Gson
import gg.norisk.core.channel.internal.NRCHandshakePayload
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

object Payloads {
    private val payloadTypes = mutableMapOf<String, Class<out AbstractPayload>>()
    private val handshakeDone = ConcurrentHashMap<UUID, Boolean>()
    private val queuedPayloads = ConcurrentHashMap<UUID, ConcurrentLinkedQueue<Triple<AbstractPayload, (UUID, ByteArray) -> Unit, Boolean>>>()
    private val gson = Gson()

    fun register(type: String, clazz: Class<out AbstractPayload>) {
        payloadTypes[type] = clazz
    }

    fun get(type: String): Class<out AbstractPayload>? = payloadTypes[type]

    fun all(): Map<String, Class<out AbstractPayload>> = payloadTypes.toMap()

    fun registerDefaults() {
        register("toast", AbstractToastPayload::class.java)
        register("module_deactivate", AbstractModuleDeactivatePayload::class.java)
    }

    fun sendHandshake(uuid: UUID, sendToClient: (UUID, ByteArray) -> Unit) {
        val handshake = NRCHandshakePayload()
        val data = ChannelApi.send(handshake)
        sendToClient(uuid, data)
    }

    fun send(uuid: UUID, payload: AbstractPayload, sendToClient: (UUID, ByteArray) -> Unit) {
        if (handshakeDone[uuid] == true) {
            val data = ChannelApi.send(payload)
            sendToClient(uuid, data)
        } else {
            queuedPayloads.computeIfAbsent(uuid) { ConcurrentLinkedQueue() }
                .add(Triple(payload, sendToClient, false))
        }
    }

    fun receive(uuid: UUID, message: ByteArray) {
        var rawString = String(message, Charsets.UTF_8)
        println("[DEBUG] Payloads.receive: uuid=$uuid, message=$rawString")
        if (rawString.startsWith("r{")) rawString = rawString.substring(1)
        try {
            val wrapper = gson.fromJson(rawString, PacketWrapper::class.java)
            if (wrapper.packetClassName.endsWith("NRCHandshakePayload")) {
                val payloadJson = wrapper.payloadJson
                if (payloadJson.contains("\"type\":\"handshake\"")) {
                    println("[DEBUG] Handshake payload received for $uuid, queue will be flushed.")
                    println("Player $uuid is a NRC client.")
                    onHandshakeReceived(uuid)
                }
            }
        } catch (e: Exception) {
            println("[DEBUG] Failed to parse incoming payload: ${e.message}")
        }
    }

    private fun onHandshakeReceived(uuid: UUID) {
        handshakeDone[uuid] = true
        val queue = queuedPayloads.remove(uuid)
        if (queue != null) {
            for ((payload, sendToClient, _) in queue) {
                val data = ChannelApi.send(payload)
                println("[DEBUG] Sending queued payload of type: ${payload.type} to $uuid after handshake.")
                sendToClient(uuid, data)
            }
        }
    }

    fun onPlayerLeave(uuid: UUID) {
        handshakeDone.remove(uuid)
        queuedPayloads.remove(uuid)
    }
}
