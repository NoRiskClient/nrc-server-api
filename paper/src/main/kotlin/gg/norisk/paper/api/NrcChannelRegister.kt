package gg.norisk.paper.api

import gg.norisk.core.common.Constants.Companion.NRC_CHANNEL
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener

object NrcChannelRegistrar {
    fun register(plugin: JavaPlugin) {
        plugin.logger.info("Registering '$NRC_CHANNEL' channel...")
        val server = plugin.server
        server.messenger.registerOutgoingPluginChannel(plugin, NRC_CHANNEL)
        server.messenger.registerIncomingPluginChannel(plugin, NRC_CHANNEL, plugin as PluginMessageListener)
        plugin.logger.info("Channel registration complete.")
    }
}