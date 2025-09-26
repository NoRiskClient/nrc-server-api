package gg.norisk.velocity.api;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.velocity.Velocity;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;

public record ServerAPI(CoreAPI coreAPI, ProxyServer server, Logger logger,
                        MinecraftChannelIdentifier channelIdentifier) implements NoRiskServerAPI {

  public static ServerAPI create(Plugin plugin) {
    try {
      if (Velocity.getInstance() == null) {
        System.err.println(
            "NoRiskClient-Server-API Velocity module is not loaded! Make sure it's in your plugins folder.");
        return null;
      }

      if (Velocity.getCoreApi() == null) {
        System.err.println(
            "NoRiskClient-Server-API Velocity module failed to initialize properly.");
        return null;
      }
      Velocity instance = Velocity.getInstance();

      return new ServerAPI(Velocity.getCoreApi(), instance.getServer(), instance.getLogger(),
          instance.getChannelIdentifier());
    } catch (Exception e) {
      System.err.println("Failed to initialize NoRisk API: " + e.getMessage());
      return null;
    }
  }

  public static boolean isAvailable() {
    try {
      return Velocity.getInstance() != null && Velocity.getCoreApi() != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void sendPacket(UUID uuid, OutPayload packet) {
    NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
    if (nrcPlayer == null) {
      return;
    }

    nrcPlayer.sendPayload(packet);
  }

  @Override
  public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request,
      Consumer<R> callback) {
    NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);

    if (nrcPlayer == null) {
      return;
    }

    nrcPlayer.sendRequest(coreAPI.getPluginChannel(), request, callback);
  }

  @Override
  public void registerListener(PacketListener listener) {
    coreAPI.getEventManager().registerListener(listener);
  }
} 