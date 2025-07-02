package gg.norisk.client.network.serverapi.payload

import gg.norisk.core.payloads.AbstractPayload
import com.google.gson.Gson
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

class NrcCustomPayload : CustomPayload {
    val payloadBytes: ByteArray?
    val payloadObj: AbstractPayload?
    val json: String?

    constructor(payloadBytes: ByteArray) {
        this.payloadBytes = payloadBytes
        this.payloadObj = null
        this.json = null
    }

    constructor(payloadObj: AbstractPayload) {
        this.payloadObj = payloadObj
        this.payloadBytes = null
        this.json = gson.toJson(payloadObj)
    }

    constructor(json: String) {
        this.json = json
        this.payloadObj = null
        this.payloadBytes = null
    }

    override fun getId(): CustomPayload.Id<NrcCustomPayload> = ID_OBJ

    fun write(buf: PacketByteBuf) {
        buf.writeString(json ?: gson.toJson(payloadObj))
    }

    companion object {
        val ID: Identifier = Identifier.of("norisk", "main")
        val ID_OBJ = CustomPayload.Id<NrcCustomPayload>(ID)
        private val gson = Gson()
        private val logger = LoggerFactory.getLogger("ClientChannelApi")
        val CODEC: PacketCodec<PacketByteBuf, NrcCustomPayload> = PacketCodec.of(
            { payload, buf ->
                val jsonString = payload.json ?: gson.toJson(payload.payloadObj)
                buf.writeString(jsonString)
            },
            { buf ->
                val readerIndex = buf.readerIndex()
                return@of try {
                    val json = buf.readString(buf.readableBytes())
                    NrcCustomPayload(json)
                } catch (e: Exception) {
                    buf.readerIndex(readerIndex)
                    val remaining = buf.readableBytes()
                    val bytes = ByteArray(remaining)
                    buf.readBytes(bytes)
                    val json = String(bytes, Charsets.UTF_8)
                    NrcCustomPayload(json)
                }
            }
        )
        fun read(buf: PacketByteBuf): NrcCustomPayload {
            val json = buf.readString()
            return NrcCustomPayload(json)
        }
    }
}