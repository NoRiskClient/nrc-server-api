package gg.norisk.fabric

import NRC_CHANNEL
import gg.norisk.client.network.serverapi.payload.NrcCustomPayload
import gg.norisk.core.common.NoRiskServerApi
import gg.norisk.core.manager.InputbarPayloadManager
import gg.norisk.core.payloads.Modules
import gg.norisk.core.payloads.Payloads
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

        val wheelPayload = api.createWheelPayload(
            name = "Test",
            command = "/say Hello, ${player.gameProfile.name}!"
        )
        Payloads.send(player.uuid, wheelPayload) { uuid, data ->
            ServerPlayNetworking.send(player, NrcCustomPayload(data))
        }

        val wheelPayload2 = api.createWheelPayload(
            name = "Hii Enricoe",
            command = "/say Hello, ${player.gameProfile.name}!"
        )
        Payloads.send(player.uuid, wheelPayload2) { uuid, data ->
            ServerPlayNetworking.send(player, NrcCustomPayload(data))
        }

        val inputbarPayload = api.createInputbarPayload(
            input = "Gib deinen Namen ein:",
            placeholder = "Dein Name...",
            maxLength = 50
        )

        api.requestInput(
            playerUuid = player.uuid,
            inputbarPayload = inputbarPayload,
            sendToClient = { uuid, data ->
                ServerPlayNetworking.send(player, NrcCustomPayload(data))
            },
            onResponse = { input ->
                player.sendMessage(net.minecraft.text.Text.literal("Du hast eingegeben: $input"))
                val message = "Hallo $input! Willkommen auf dem Server!"
                player.server.playerManager.broadcast(net.minecraft.text.Text.literal(message), false)
            },
            onCancel = {
                player.sendMessage(net.minecraft.text.Text.literal("§cDu hast die Eingabe abgebrochen!"))
                InputbarPayloadManager.resendInputbar(player.uuid)
            }
        )

        val moduleDisablePayload = api.createModuleDeactivatePayload(
            modules = listOf(
                Modules.FullBrightModule,
                Modules.ZoomModule
            )
        )
        Payloads.send(player.uuid, moduleDisablePayload) { uuid, data ->
            ServerPlayNetworking.send(player, NrcCustomPayload(data))
        }
    }

    private fun onPlayerLeave(player: ServerPlayerEntity) {
        Payloads.onPlayerLeave(player.uuid)
        InputbarPayloadManager.unregisterSession(player.uuid)
    }
}