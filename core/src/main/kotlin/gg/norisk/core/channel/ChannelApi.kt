import com.google.gson.Gson
import gg.norisk.core.channel.internal.NRCHandshakeManager
import gg.norisk.core.channel.internal.NRCHandshakePayload
import gg.norisk.core.payloads.AbstractPayload
import java.util.*

const val NRC_CHANNEL = "norisk:main"

 interface PacketListener<T> {
    fun onMessage(sender: UUID, packet: T)
}

data class PacketWrapper(val packetClassName: String, val payloadJson: String)

object ChannelApi {
    private val gson = Gson()
    private val listeners = mutableMapOf<Class<*>, MutableList<PacketListener<*>>>()
    private val allowedPacketClasses = mapOf(
        "NRCHandshakePayload" to NRCHandshakePayload::class.java,
    )

    fun <T : Any> registerListener(packetType: Class<T>, listener: Any) {
        println("Registering listener for packet type "+packetType.simpleName)
        val listenersList = listeners.computeIfAbsent(packetType) {
            println("Creating new listener list for packet type "+packetType.simpleName)
            mutableListOf()
        }
        listenersList.add(listener as PacketListener<*>)
        println("Listener registered successfully. Total listeners for "+packetType.simpleName+": "+listenersList.size)
    }

    fun send(packet: Any): ByteArray {
        val type = when (packet) {
            is AbstractPayload -> packet.type
            else -> packet::class.java.simpleName
        }
        println("Preparing to send packet of type $type")
        val payloadJson = gson.toJson(packet)
        val wrapper = PacketWrapper(packet::class.java.name, payloadJson)
        val wrapperJson = gson.toJson(wrapper)
        println("Packet of type $type serialized and ready to send")
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
}
