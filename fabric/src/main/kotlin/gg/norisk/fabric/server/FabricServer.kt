package gg.norisk.fabric.server

import gg.norisk.core.common.NoRiskServerApi
import gg.norisk.core.manager.InputbarPayloadManager
import gg.norisk.core.payloads.Payloads
import gg.norisk.fabric.api.NrcCustomPayload
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity

class FabricServer : DedicatedServerModInitializer {
    val api = NoRiskServerApi()

    override fun onInitializeServer() {
        println("Initializing NoRiskClient-Server-API Fabric-Server Module...")

        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            onPlayerJoin(handler.player)
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            onPlayerLeave(handler.player)
        }

        println("NoRiskClient-Server-API Fabric-Server Module is ready!")
    }

    private fun onPlayerJoin(player: ServerPlayerEntity) {
        Payloads.sendHandshake(player.uuid) { uuid, data ->
            ServerPlayNetworking.send(player, NrcCustomPayload(data))
        }
    }

    private fun onPlayerLeave(player: ServerPlayerEntity) {
        Payloads.onPlayerLeave(player.uuid)
        InputbarPayloadManager.unregisterSession(player.uuid)
    }
}