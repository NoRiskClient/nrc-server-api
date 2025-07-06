package gg.norisk.bungeecord.api

import gg.norisk.core.common.Constants.Companion.NRC_CHANNEL
import net.md_5.bungee.api.plugin.Plugin

object NrcChannelRegister {
    fun register(plugin: Plugin) {
        plugin.logger.info("Registering '$NRC_CHANNEL' channel...")
        val proxy = plugin.proxy
        proxy.registerChannel(NRC_CHANNEL)
        plugin.logger.info("Channel registration complete.")
    }
}
