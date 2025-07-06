package gg.norisk.velocity.api

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import gg.norisk.core.common.Constants.Companion.NRC_CHANNEL
import org.slf4j.Logger

object NrcChannelRegister {
    fun register(server: ProxyServer, logger: Logger) {
        logger.info("Registering '$NRC_CHANNEL' channel...")
        val channelIdentifier = MinecraftChannelIdentifier.from(NRC_CHANNEL)
        server.channelRegistrar.register(channelIdentifier)
        logger.info("Channel registration complete.")
    }
}
