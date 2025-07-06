package gg.norisk.bungeecord

import gg.norisk.core.common.NoRiskServerApi
import gg.norisk.core.manager.InputbarPayloadManager
import gg.norisk.core.payloads.Payloads
import gg.norisk.bungeecord.api.NrcChannelRegister
import gg.norisk.core.common.Constants.Companion.NRC_CHANNEL
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler

class BungeeCord : Plugin(), Listener {
    val api = NoRiskServerApi()

    override fun onEnable() {
        logger.info("NoRiskClient-Server-API BungeeCord module is starting...")
        NrcChannelRegister.register(this)
        proxy.pluginManager.registerListener(this, this)
        logger.info("NoRiskClient-Server-API BungeeCord module is ready!")
    }

    @EventHandler
    fun onPlayerJoin(event: PostLoginEvent) {
        val player = event.player
        Payloads.sendHandshake(player.uniqueId) { uuid, data ->
            player.server?.sendData(NRC_CHANNEL, data)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerDisconnectEvent) {
        Payloads.onPlayerLeave(event.player.uniqueId)
        InputbarPayloadManager.unregisterSession(event.player.uniqueId)
    }

    @EventHandler
    fun onPluginMessage(event: PluginMessageEvent) {
        if (event.tag == NRC_CHANNEL) {
            val sender = event.sender
            if (sender is ProxiedPlayer) {
                Payloads.receive(sender.uniqueId, event.data)
            }
        }
    }
}
