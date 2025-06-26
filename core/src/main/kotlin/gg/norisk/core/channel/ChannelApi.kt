import com.google.gson.Gson
import java.util.*

const val NRC_CHANNEL = "norisk:main"

interface PacketListener<T> {
    fun onMessage(sender: UUID, packet: T)
}

data class PacketWrapper(val packetClassName: String, val payloadJson: String)

object ChannelApi {
    private val gson = Gson()
    private val listeners = mutableMapOf<Class<*>, MutableList<PacketListener<*>>>()
    private val nrcPlayers = mutableSetOf<UUID>()

    fun <T : Any> registerListener(packetType: Class<T>, listener: PacketListener<T>) {
        listeners.computeIfAbsent(packetType) { mutableListOf() }
            .add(listener)
    }

    fun send(packet: Any): ByteArray {
        val payloadJson = gson.toJson(packet)
        val wrapper = PacketWrapper(packet::class.java.name, payloadJson)
        val wrapperJson = gson.toJson(wrapper)
        return wrapperJson.toByteArray(Charsets.UTF_8)
    }

    fun receive(sender: UUID, message: ByteArray) {
        try {
            val wrapperJson = String(message, Charsets.UTF_8)
            val wrapper = gson.fromJson(wrapperJson, PacketWrapper::class.java)
            val packetClass = Class.forName(wrapper.packetClassName)
            val classListeners = listeners[packetClass] ?: return
            val packetPayload = gson.fromJson(wrapper.payloadJson, packetClass)
            classListeners.forEach { listener ->
                @Suppress("UNCHECKED_CAST")
                (listener as PacketListener<Any>).onMessage(sender, packetPayload)
            }
        } catch (e: Exception) {
            System.err.println("Error processing packet from $sender: ${e.message}")
        }
    }

    fun onPlayerJoin(player: UUID, sendToClient: (channel: String, data: ByteArray) -> Unit) {
        val handshakePacket = NRCHandshakePayload()
        sendToClient(NRC_CHANNEL, send(handshakePacket))
    }

    fun onClientHandshake(player: UUID) {
        nrcPlayers.add(player)
    }

    fun isNrcPlayer(player: UUID): Boolean = nrcPlayers.contains(player)

    fun onPlayerLeave(player: UUID) {
        nrcPlayers.remove(player)
    }
}

data class NRCHandshakePayload(val type: String = "handshake")