package gg.norisk.fabric.server;

import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.fabric.api.ServerAPI;
import gg.norisk.fabric.listener.JoinListener;
import gg.norisk.fabric.listener.QuitListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricServer implements DedicatedServerModInitializer {
    private ServerAPI api;
    private CoreAPIImpl coreApi;

    @Override
    public void onInitializeServer() {
        System.out.println("Initializing NoRiskClient-Server-API Fabric-Server Module...");
        
        coreApi = new CoreAPIImpl();
        this.api = new ServerAPI(coreApi);
        
        api.registerListener(new JoinListener(coreApi));
        
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            onPlayerJoin(handler.getPlayer());
        });
        
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            onPlayerLeave(handler.getPlayer());
        });
        
        ServerPlayNetworking.registerGlobalReceiver(gg.norisk.fabric.api.NrcCustomPayload.ID_OBJ, (payload, context) -> {
            onPluginMessage(context.player(), payload);
        });
        
        System.out.println("NoRiskClient-Server-API Fabric-Server Module is ready!");
    }
    
    private void onPlayerJoin(ServerPlayerEntity player) {        
        coreApi.getPlayerManager().setNrcPlayer(player.getUuid(), true);
     }
    
    private void onPlayerLeave(ServerPlayerEntity player) {
        QuitListener quitListener = new QuitListener(coreApi);
        quitListener.onPlayerQuit(player);
    }
    
    private void onPluginMessage(ServerPlayerEntity player, gg.norisk.fabric.api.NrcCustomPayload payload) {
        System.out.println("Received packet from " + player.getName().getString());
        try {
            String json = payload.getJson();
            if (json != null) {
                byte[] messageBytes = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                PacketWrapper packet = coreApi.serializePacketWrapper(messageBytes);
                System.out.println("Packet: " + packet.payloadJson());
                InPayload responsePacket = coreApi.deserialize(packet);
                System.out.println("Response packet: " + responsePacket);
                System.out.println("Packet ID: " + packet.packetId());
                
                if (packet.packetId() != null && coreApi.getCallbackManager().waitingFor(packet.packetId())) {
                    System.out.println("Received response for packet ID " + packet.packetId());
                    coreApi.getCallbackManager().completeCallback(packet.packetId(), responsePacket);
                } else {
                    System.out.println("Received response for unknown packet ID " + packet.packetId());
                    coreApi.getEventManager().callEvent(player.getUuid(), responsePacket);
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to deserialize packet: " + e.getMessage());
        }
    }

    public ServerAPI getApi() {
        return api;
    }
    
    public CoreAPIImpl getCoreApi() {
        return coreApi;
    }
} 