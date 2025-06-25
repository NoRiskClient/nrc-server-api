package gg.norisk.paper

import NRC_CHANNEL
import gg.norisk.paper.api.NrcChannelRegistrar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener

class Paper : JavaPlugin(), Listener, PluginMessageListener {

    override fun onEnable() {
        logger.info("Initializing NoRiskClient-Server-API Paper Module...")
        NrcChannelRegistrar.register(this)
        server.pluginManager.registerEvents(this, this)
        logger.info("Channel and event listeners are now active!")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        ChannelApi.onPlayerJoin(player.uniqueId) { channel, data ->
            player.sendPluginMessage(this, channel, data)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        ChannelApi.onPlayerLeave(event.player.uniqueId)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel == NRC_CHANNEL) {
            ChannelApi.receive(player.uniqueId, message)
        }
    }
}