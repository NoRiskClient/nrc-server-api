package gg.norisk.fabric.api;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NrcPlayerNotFoundExeption;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.fabric.server.FabricServer;
import java.util.UUID;
import java.util.function.Consumer;

public record ServerAPI(CoreAPI coreAPI) implements NoRiskServerAPI {

  public static ServerAPI create(Object plugin) {
    try {
      if (FabricServer.getInstance() == null) {
        System.err.println(
            "NoRiskClient-Server-API Fabric module is not loaded! Make sure it's in your mods folder.");
        return null;
      }

      if (FabricServer.getCoreApi() == null) {
        System.err.println(
            "NoRiskClient-Server-API Fabric module failed to initialize properly.");
        return null;
      }

      return new ServerAPI(FabricServer.getCoreApi());
    } catch (Exception e) {
      System.err.println("Failed to initialize NoRisk API: " + e.getMessage());
      return null;
    }
  }

  public static boolean isAvailable() {
    try {
      return FabricServer.getInstance() != null && FabricServer.getCoreApi() != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void sendPacket(UUID uuid, OutPayload packet) {
    coreAPI.serializePacket(packet);
  }

  @Override
  public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request,
      Consumer<R> callback) {
    NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
    if (nrcPlayer == null) {
      return;
    }

    try {
      coreAPI.prepareRequest(uuid, request, callback);
    } catch (NrcPlayerNotFoundExeption e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void registerListener(PacketListener listener) {
    coreAPI.getEventManager().registerListener(listener);
  }
} 