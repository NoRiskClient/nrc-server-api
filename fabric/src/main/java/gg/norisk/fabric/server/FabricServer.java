package gg.norisk.fabric.server;

import gg.norisk.core.common.NoRiskServerApi;
import gg.norisk.core.manager.InputbarPayloadManager;
import gg.norisk.core.payloads.Payloads;
import gg.norisk.fabric.api.NrcCustomPayload;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricServer implements DedicatedServerModInitializer {
    private final NoRiskServerApi api = new NoRiskServerApi();
    
    @Override
    public void onInitializeServer() {
        System.out.println("Initializing NoRiskClient-Server-API Fabric-Server Module...");
        
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            onPlayerJoin(handler.getPlayer());
        });
        
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            onPlayerLeave(handler.getPlayer());
        });
        
        System.out.println("NoRiskClient-Server-API Fabric-Server Module is ready!");
    }
    
    private void onPlayerJoin(ServerPlayerEntity player) {
        Payloads.sendHandshake(player.getUuid(), (uuid, data) -> {
            ServerPlayNetworking.send(player, new NrcCustomPayload(data));
        });
    }
    
    private void onPlayerLeave(ServerPlayerEntity player) {
        Payloads.onPlayerLeave(player.getUuid());
        InputbarPayloadManager.unregisterSession(player.getUuid());
    }
    
    public NoRiskServerApi getApi() {
        return api;
    }
} 