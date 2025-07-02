package gg.norisk.core.payloads

import NRC_CHANNEL
import com.google.gson.Gson
import gg.norisk.core.manager.InputbarPayloadManager
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.schedule
import java.util.Timer

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
        register("inputbar_response", InputbarResponsePayload::class.java)
    }

    private fun encodeVarInt(value: Int): ByteArray {
        var v = value
        val out = mutableListOf<Byte>()
        while (true) {
            if ((v and 0x7F.inv()) == 0) {
                out.add(v.toByte())
                break
            } else {
                out.add(((v and 0x7F) or 0x80).toByte())
                v = v ushr 7
            }
        }
        return out.toByteArray()
    }

    private fun wrapWithVarIntLength(data: ByteArray): ByteArray {
        val lengthPrefix = encodeVarInt(data.size)
        return lengthPrefix + data
    }

    fun sendHandshake(uuid: UUID, sendToClient: (UUID, ByteArray) -> Unit) {
        val handshake = HandshakePayload()
        val data = ChannelApi.send(handshake)
        val wrapped = wrapWithVarIntLength(data)
        sendToClient(uuid, wrapped)
    }

    fun send(uuid: UUID, payload: AbstractPayload, sendToClient: (UUID, ByteArray) -> Unit) {
        queuedPayloads.computeIfAbsent(uuid) { ConcurrentLinkedQueue() }
            .add(Triple(payload, sendToClient, false))
        trySendNext(uuid)
    }

    private fun trySendNext(uuid: UUID) {
        if (handshakeDone[uuid] == true) {
            val queue = queuedPayloads[uuid] ?: return

            while (queue.isNotEmpty()) {
                val next = queue.poll() ?: break
                val (payload, sendToClient, _) = next

                if (payload.type == "handshake") {
                    val data = ChannelApi.send(payload)
                    val wrapped = wrapWithVarIntLength(data)
                    sendToClient(uuid, wrapped)
                } else {
                    Timer().schedule(5000) {
                        val data = ChannelApi.send(payload)
                        val wrapped = wrapWithVarIntLength(data)
                        sendToClient(uuid, wrapped)
                    }
                }
            }
        }
    }

    fun receive(uuid: UUID, message: ByteArray) {
        val rawString = String(message, Charsets.UTF_8)

        val cleanedString = if (rawString.startsWith("+")) {
            rawString.substring(1)
        } else {
            rawString
        }

        val jsonStart = cleanedString.indexOf('{')
        if (jsonStart == -1) {
            return
        }

        val classNameStart = cleanedString.indexOf('(')
        val classNameEnd = cleanedString.indexOf('{')
        var packetClassName: String? = null
        if (classNameStart != -1 && classNameEnd != -1 && classNameEnd > classNameStart + 1) {
            packetClassName = cleanedString.substring(classNameStart + 1, classNameEnd).trim()
        }

        val payloadJson = cleanedString.substring(jsonStart)
        try {
            if (payloadJson.trim().startsWith("{\"type\":") || payloadJson.trim().startsWith("{\"input\":")) {
                if (payloadJson == "{\"type\":\"handshake\"}") {
                    println("Player $uuid is a NRC client.")
                    onHandshakeReceived(uuid)
                    return
                } else if (payloadJson == "{\"type\":\"ack\"}") {
                    trySendNext(uuid)
                    return
                } else if (payloadJson.contains("\"type\":\"inputbar_response\"") || payloadJson.contains("\"input\":")) {
                    try {
                        val payload = gson.fromJson(payloadJson, InputbarResponsePayload::class.java)
                        InputbarPayloadManager.handleInputbarResponse(uuid, payload)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return
                }
            }
            if (!packetClassName.isNullOrBlank()) {
                if ((packetClassName.endsWith("HandshakePayload") || packetClassName == "gg.norisk.core.payloads.HandshakePayload") && payloadJson == "{\"type\":\"handshake\"}") {
                    println("Player $uuid is a NRC client.")
                    onHandshakeReceived(uuid)
                } else if ((packetClassName.endsWith("AckPayload") || packetClassName == "gg.norisk.core.payloads.AckPayload") && payloadJson == "{\"type\":\"ack\"}") {
                    trySendNext(uuid)
                } else if ((packetClassName.endsWith("InputbarResponsePayload") || packetClassName == "gg.norisk.core.payloads.InputbarResponsePayload")) {
                    try {
                        val payload = gson.fromJson(payloadJson, InputbarResponsePayload::class.java)
                        InputbarPayloadManager.handleInputbarResponse(uuid, payload)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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