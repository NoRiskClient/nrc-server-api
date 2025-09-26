package gg.norisk.fabric.server;

import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.fabric.api.ServerAPI;
import gg.norisk.fabric.listener.JoinListener;
import gg.norisk.fabric.listener.QuitListener;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricServer implements DedicatedServerModInitializer {

  @Getter
  private static ServerAPI api;

  @Getter
  private static CoreAPIImpl coreApi;

  @Getter
  private static FabricServer instance;

  private ServerAPI apiInstance;
  private CoreAPIImpl coreApiInstance;

  @Override
  public void onInitializeServer() {
    System.out.println("Initializing NoRiskClient-Server-API Fabric-Server Module...");

    instance = this;
    coreApiInstance = new CoreAPIImpl();
    coreApi = coreApiInstance;
    this.apiInstance = new ServerAPI(coreApiInstance);
    api = apiInstance;
    coreApi.setServerAPI(api);

    apiInstance.registerListener(new JoinListener(coreApiInstance));

    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      onPlayerJoin(handler.getPlayer());
    });

    ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
      onPlayerLeave(handler.getPlayer());
    });

    ServerPlayNetworking.registerGlobalReceiver(gg.norisk.fabric.api.NrcCustomPayload.ID_OBJ,
        (payload, context) -> {
          onPluginMessage(context.player(), payload);
        });

    System.out.println("NoRiskClient-Server-API Fabric-Server Module is ready!");
  }

  private void onPlayerJoin(ServerPlayerEntity player) {
    coreApiInstance.getPlayerManager().setNrcPlayer(player.getUuid(), true);
  }

  private void onPlayerLeave(ServerPlayerEntity player) {
    QuitListener quitListener = new QuitListener(coreApiInstance);
    quitListener.onPlayerQuit(player);
  }

  private void onPluginMessage(ServerPlayerEntity player,
      gg.norisk.fabric.api.NrcCustomPayload payload) {
    System.out.println("Received packet from " + player.getName().getString());
    try {
      String json = payload.getJson();
      if (json != null) {
        byte[] messageBytes = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        PacketWrapper packet = coreApiInstance.serializePacketWrapper(messageBytes);
        InPayload responsePacket = coreApiInstance.deserialize(packet);

        if (packet.packetId() != null && coreApiInstance.getCallbackManager()
            .waitingFor(packet.packetId())) {
          coreApiInstance.getCallbackManager().completeCallback(packet.packetId(), responsePacket);
        } else {
          coreApiInstance.getEventManager().callEvent(player.getUuid(), responsePacket);
        }
      }
    } catch (Exception e) {
      System.err.println("Unable to deserialize packet: " + e.getMessage());
    }
  }
} 