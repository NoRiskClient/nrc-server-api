package gg.norisk.fabric.api

import gg.norisk.core.common.Constants.Companion.NRC_CHANNEL
import gg.norisk.core.payloads.Payloads
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object NrcChannelRegistrar {
    private val logger = LoggerFactory.getLogger("NrcChannelRegistrar")

    fun register() {
        logger.info("Registering '$NRC_CHANNEL' channel...")

        val identifier = Identifier.of("norisk", "main")

        ServerPlayNetworking.registerGlobalReceiver(NrcCustomPayload.ID_OBJ) { payload, context ->
            val player = context.player()
            val json = payload.json

            if (json != null) {
                logger.debug("Received message from player ${player.uuid}: $json")
                val messageBytes = json.toByteArray(Charsets.UTF_8)
                Payloads.receive(player.uuid, messageBytes)
            }
        }

        logger.info("Channel registration complete.")
    }
}
