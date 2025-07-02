package gg.norisk.paper

import NRC_CHANNEL
import gg.norisk.core.common.NoRiskServerApi
import gg.norisk.core.manager.InputbarPayloadManager
import gg.norisk.core.payloads.Payloads
import gg.norisk.paper.api.NrcChannelRegistrar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener

class Paper : JavaPlugin(), Listener, PluginMessageListener {
    val api = NoRiskServerApi()

    override fun onEnable() {
        logger.info("NoRiskClient-Server-API Paper module is starting...")
        NrcChannelRegistrar.register(this)
        server.pluginManager.registerEvents(this, this)
        logger.info("NoRiskClient-Server-API Paper module is ready!")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        Payloads.sendHandshake(player.uniqueId) { uuid, data ->
            player.sendPluginMessage(this, NRC_CHANNEL, data)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Payloads.onPlayerLeave(event.player.uniqueId)
        InputbarPayloadManager.unregisterSession(event.player.uniqueId)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel == NRC_CHANNEL) {
            Payloads.receive(player.uniqueId, message)
        }
    }
}