import com.google.gson.Gson
import gg.norisk.core.channel.internal.NRCHandshakeManager
import gg.norisk.core.channel.internal.NRCHandshakePayload
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

const val NRC_CHANNEL = "norisk:main"

interface PacketListener<T> {
    fun onMessage(sender: UUID, packet: T)
}

data class PacketWrapper(val packetClassName: String, val payloadJson: String)

object ChannelApi {
    private val gson = Gson()
    private val listeners = mutableMapOf<Class<*>, MutableList<PacketListener<*>>>()
    private val handshakeManager = NRCHandshakeManager(
        onNrcClient = { player -> println("Player $player is a NRC client.") },
        onNonNrcClient = { player -> println("Player $player is NOT a NRC client.") },
        handshakeTimeoutSeconds = 20L
    )

    private val allowedPacketClasses = mapOf(
        "NRCHandshakePayload" to gg.norisk.core.channel.internal.NRCHandshakePayload::class.java,
    )

    fun <T : Any> registerListener(packetType: Class<T>, listener: PacketListener<T>) {
        println("Registering listener for packet type ${packetType.simpleName}")
        val listenersList = listeners.computeIfAbsent(packetType) {
            println("Creating new listener list for packet type ${packetType.simpleName}")
            mutableListOf()
        }
        listenersList.add(listener)
        println("Listener registered successfully. Total listeners for ${packetType.simpleName}: ${listenersList.size}")
    }

    fun send(packet: Any): ByteArray {
        println("Preparing to send packet of type ${packet::class.java.simpleName}")
        val payloadJson = gson.toJson(packet)
        val wrapper = PacketWrapper(packet::class.java.name, payloadJson)
        val wrapperJson = gson.toJson(wrapper)
        println("Packet of type ${packet::class.java.simpleName} serialized and ready to send")
        return wrapperJson.toByteArray(Charsets.UTF_8)
    }

    fun receive(sender: UUID, message: ByteArray) {
        println("Received message from player $sender on channel $NRC_CHANNEL")
        val rawString = String(message, Charsets.UTF_8)
        val jsonStart = rawString.indexOf('{')
        if (jsonStart == -1) {
            System.err.println("No JSON object found in received message: $rawString")
            return
        }
        val wrapperJson = rawString.substring(jsonStart)
        try {
            val wrapper = gson.fromJson(wrapperJson, PacketWrapper::class.java)
            val simpleName = wrapper.packetClassName.substringAfterLast('.')
            val mappedClass = allowedPacketClasses[simpleName]
            if (mappedClass == null) {
                System.err.println("Rejected unknown or unregistered packet type: ${wrapper.packetClassName}")
                return
            }
            if (mappedClass.simpleName == "NRCHandshakePayload") {
                handshakeManager.onClientHandshake(sender)
                return
            }
            println("Processing packet of type ${mappedClass.simpleName} from player $sender")
            val classListeners = listeners[mappedClass] ?: run {
                println("No listeners found for packet type ${mappedClass.simpleName}")
                return
            }
            val packetPayload = gson.fromJson(wrapper.payloadJson, mappedClass)
            println("Found ${classListeners.size} listener(s) for packet type ${mappedClass.simpleName}")
            classListeners.forEach { listener ->
                @Suppress("UNCHECKED_CAST")
                (listener as PacketListener<Any>).onMessage(sender, packetPayload)
            }
            println("Successfully processed packet of type ${mappedClass.simpleName} from player $sender")
        } catch (e: Exception) {
            System.err.println("Error processing packet from $sender: ${e.message}")
            System.err.println("Raw received JSON: $wrapperJson")
            e.printStackTrace()
        }
    }

    fun onPlayerJoin(player: UUID, sendToClient: (channel: String, data: ByteArray) -> Unit) {
        println("Player $player joined. Sending handshake packet...")
        handshakeManager.onPlayerJoin(player) {
            val handshakePacket = NRCHandshakePayload()
            sendToClient(NRC_CHANNEL, send(handshakePacket))
            println("Handshake packet sent to player $player on channel $NRC_CHANNEL")
        }
    }

    fun isNrcPlayer(player: UUID): Boolean = handshakeManager.isNrcPlayer(player)

    fun onPlayerLeave(player: UUID) {
        handshakeManager.onPlayerLeave(player)
    }
}
