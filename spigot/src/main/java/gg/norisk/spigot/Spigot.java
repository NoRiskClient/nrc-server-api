package gg.norisk.spigot;

import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.spigot.api.ServerAPI;
import gg.norisk.spigot.listener.JoinListener;
import gg.norisk.spigot.listener.QuitListener;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class Spigot extends JavaPlugin implements Listener, PluginMessageListener {

  @Getter
  private static ServerAPI api;

  @Getter
  private static CoreAPIImpl coreApi;

  @Getter
  private static Spigot instance;

  @Override
  public void onEnable() {
    getLogger().info("NoRiskClient-Server-API Spigot module is starting...");

    instance = this;
    coreApi = new CoreAPIImpl();
    Spigot.api = new ServerAPI(coreApi, this);

    coreApi.setServerAPI(Spigot.api);

    getServer().getPluginManager().registerEvents(new QuitListener(coreApi), this);
    api.registerListener(new JoinListener(coreApi));

    getServer().getMessenger().registerOutgoingPluginChannel(this, coreApi.getPluginChannel());
    getServer().getMessenger()
        .registerIncomingPluginChannel(this, coreApi.getPluginChannel(), this);

    getLogger().info("NoRiskClient-Server-API Spigot module is ready!");
  }

  @Override
  public void onPluginMessageReceived(String channel, @NotNull Player player,
      @NotNull byte[] message) {
    if (!channel.equals(coreApi.getPluginChannel())) {
      return;
    }

    try {
      PacketWrapper packet = coreApi.serializePacketWrapper(message);
      InPayload responsePacket = coreApi.deserialize(packet);

      if (packet.packetId() != null && coreApi.getCallbackManager().waitingFor(packet.packetId())) {
        coreApi.getCallbackManager().completeCallback(packet.packetId(), responsePacket);
      } else {
        coreApi.getEventManager().callEvent(player.getUniqueId(), responsePacket);
      }
    } catch (Exception e) {
      getLogger().severe("Unable to deserialize packet: " + e.getMessage());
    }
  }
} 