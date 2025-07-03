package gg.norisk.velocity

import NRC_CHANNEL
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.PluginMessageEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import gg.norisk.core.common.NoRiskServerApi
import gg.norisk.core.manager.InputbarPayloadManager
import gg.norisk.core.payloads.Payloads
import gg.norisk.velocity.api.NrcChannelRegister
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "noriskclient-server-api",
    name = "NoRiskClient Server API",
    version = "0.1.0",
    description = "API to interact with the NoRisk Client.",
    authors = ["S42"]
)
class Velocity @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {
    val api = NoRiskServerApi()

    @Subscribe
    fun onProxyInitialization(event: com.velocitypowered.api.event.proxy.ProxyInitializeEvent) {
        logger.info("NoRiskClient-Server-API Velocity module is starting...")
        NrcChannelRegister.register(server, logger)
        logger.info("NoRiskClient-Server-API Velocity module is ready!")
    }

    @Subscribe
    fun onPlayerJoin(event: PostLoginEvent) {
        val player = event.player
        Payloads.sendHandshake(player.uniqueId) { uuid, data ->
            player.currentServer.ifPresent { serverConnection ->
                serverConnection.sendPluginMessage(MinecraftChannelIdentifier.from(NRC_CHANNEL), data)
            }
        }
    }

    @Subscribe
    fun onPlayerQuit(event: DisconnectEvent) {
        Payloads.onPlayerLeave(event.player.uniqueId)
        InputbarPayloadManager.unregisterSession(event.player.uniqueId)
    }

    @Subscribe
    fun onPluginMessage(event: PluginMessageEvent) {
        if (event.identifier.id == NRC_CHANNEL) {
            val source = event.source
            if (source is com.velocitypowered.api.proxy.Player) {
                Payloads.receive(source.uniqueId, event.data)
            }
        }
    }
}
