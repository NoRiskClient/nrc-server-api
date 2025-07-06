import com.google.gson.Gson
import gg.norisk.core.payloads.HandshakePayload
import gg.norisk.core.payloads.AbstractPayload
import java.util.*

 interface PacketListener<T> {
    fun onMessage(sender: UUID, packet: T)
}

data class PacketWrapper(val packetClassName: String, val payloadJson: String)

object ChannelApi {
    private val gson = Gson()
    private val listeners = mutableMapOf<Class<*>, MutableList<PacketListener<*>>>()
    private val allowedPacketClasses = mapOf(
        "HandshakePayload" to HandshakePayload::class.java,
    )

    fun <T : Any> registerListener(packetType: Class<T>, listener: Any) {
        println("Registering listener for packet type "+packetType.simpleName)
        val listenersList = listeners.computeIfAbsent(packetType) {
            mutableListOf()
        }
        listenersList.add(listener as PacketListener<*>)
    }

    fun send(packet: Any): ByteArray {
        val type = when (packet) {
            is AbstractPayload -> packet.type
            else -> packet::class.java.simpleName
        }
        val payloadJson = gson.toJson(packet)
        val wrapper = PacketWrapper(packet::class.java.name, payloadJson)
        val wrapperJson = gson.toJson(wrapper)
        return wrapperJson.toByteArray(Charsets.UTF_8)
    }

    fun receive(sender: UUID, message: ByteArray) {
        val rawString = String(message, Charsets.UTF_8)
        val jsonStart = rawString.indexOf('{')
        if (jsonStart == -1) {
            return
        }
        val wrapperJson = rawString.substring(jsonStart)
        try {
            val wrapper = gson.fromJson(wrapperJson, PacketWrapper::class.java)
            val simpleName = wrapper.packetClassName.substringAfterLast('.')
            val mappedClass = allowedPacketClasses[simpleName]
            if (mappedClass == null) {
                return
            }
            val classListeners = listeners[mappedClass] ?: run {
                println("No listeners found for packet type ${mappedClass.simpleName}")
                return
            }
            val packetPayload = gson.fromJson(wrapper.payloadJson, mappedClass)
            classListeners.forEach { listener ->
                @Suppress("UNCHECKED_CAST")
                (listener as PacketListener<Any>).onMessage(sender, packetPayload)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
