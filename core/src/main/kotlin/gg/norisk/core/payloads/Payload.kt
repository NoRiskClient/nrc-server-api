package gg.norisk.core.payloads

import com.google.gson.Gson
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

object Payloads {
    private val payloadTypes = mutableMapOf<String, Class<out AbstractPayload>>()
    private val handshakeDone = ConcurrentHashMap<UUID, Boolean>()
    private val queuedPayloads = ConcurrentHashMap<UUID, ConcurrentLinkedQueue<Triple<AbstractPayload, (UUID, ByteArray) -> Unit, Boolean>>>()
    private val gson = Gson()
    private val waitingForAck = ConcurrentHashMap<UUID, Boolean>()

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
        val handshake = HandshakePayload()
        val data = ChannelApi.send(handshake)
        sendToClient(uuid, data)
    }

    fun send(uuid: UUID, payload: AbstractPayload, sendToClient: (UUID, ByteArray) -> Unit) {
        queuedPayloads.computeIfAbsent(uuid) { ConcurrentLinkedQueue() }
            .add(Triple(payload, sendToClient, false))
        trySendNext(uuid)
    }

    private fun trySendNext(uuid: UUID) {
        if (handshakeDone[uuid] == true && waitingForAck[uuid] != true) {
            val queue = queuedPayloads[uuid] ?: return
            val next = queue.poll() ?: return
            val (payload, sendToClient, _) = next
            val data = ChannelApi.send(payload)
            println("[DEBUG] Sending payload of type: ${payload.type} to $uuid (waiting for ack)")
            sendToClient(uuid, data)
            waitingForAck[uuid] = true
        }
    }

    fun receive(uuid: UUID, message: ByteArray) {
        var rawString = String(message, Charsets.UTF_8)
        println("[DEBUG] Payloads.receive: uuid=$uuid, message=$rawString")
        val jsonStart = rawString.indexOf('{')
        if (jsonStart != -1) {
            rawString = rawString.substring(jsonStart)
        }
        try {
            val wrapper = gson.fromJson(rawString, PacketWrapper::class.java)
            if (wrapper.packetClassName.endsWith("HandshakePayload")) {
                val payloadJson = wrapper.payloadJson
                if (payloadJson.contains("\"type\":\"handshake\"")) {
                    println("[DEBUG] Handshake payload received for $uuid, queue will be flushed.")
                    println("Player $uuid is a NRC client.")
                    onHandshakeReceived(uuid)
                }
            } else if (
                wrapper.packetClassName.endsWith("AckPayload") && wrapper.payloadJson.contains("\"type\":\"ack\"")
            ) {
                println("[DEBUG] AckPayload received from $uuid, sending next payload if available.")
                trySendNext(uuid)
            }
        } catch (e: Exception) {
            println("[DEBUG] Failed to parse incoming payload: ${e.message}")
        }
    }

    private fun onHandshakeReceived(uuid: UUID) {
        handshakeDone[uuid] = true
        waitingForAck[uuid] = false
        trySendNext(uuid)
    }

    fun onPlayerLeave(uuid: UUID) {
        handshakeDone.remove(uuid)
        queuedPayloads.remove(uuid)
        waitingForAck.remove(uuid)
    }
}