package gg.norisk.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.velocity.api.ServerAPI;
import gg.norisk.velocity.listener.JoinListener;
import gg.norisk.velocity.listener.QuitListener;
import java.nio.file.Path;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
    id = "noriskclient-server-api",
    name = "NoRiskClient Server API",
    version = "0.1.0",
    description = "API to interact with the NoRisk Client.",
    authors = {"S42"}
)
public class Velocity {

  @Getter
  private final ProxyServer server;
  @Getter
  private final Logger logger;
  private final Path dataDirectory;
  private final CoreAPIImpl coreApi = new CoreAPIImpl();
  @Getter
  private final MinecraftChannelIdentifier channelIdentifier = MinecraftChannelIdentifier.from(
      "norisk:main");

  @Getter
  private static ServerAPI api;

  @Getter
  private static CoreAPIImpl coreApiStatic;

  @Getter
  private static Velocity instance;

  @Inject
  public Velocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
    this.server = server;
    this.logger = logger;
    this.dataDirectory = dataDirectory;
    
    instance = this;
    coreApiStatic = coreApi;

    Velocity.api = new ServerAPI(coreApi, server, logger, channelIdentifier);
    coreApi.setServerAPI(Velocity.api);
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    logger.info("NoRiskClient-Server-API Velocity module is starting...");

    server.getChannelRegistrar().register(channelIdentifier);
    server.getEventManager().register(this, new QuitListener(coreApi));

    api.registerListener(new JoinListener(coreApi));
    logger.info("NoRiskClient-Server-API Velocity module is ready!");
  }

  @Subscribe
  public void onPluginMessage(PluginMessageEvent event) {
    if (!channelIdentifier.getId().equals(event.getIdentifier().getId())) {
      return;
    }

    if (event.getSource() instanceof Player player) {
      try {
        PacketWrapper packet = coreApi.serializePacketWrapper(event.getData());
        InPayload responsePacket = coreApi.deserialize(packet);

        if (packet.packetId() != null && coreApi.getCallbackManager()
            .waitingFor(packet.packetId())) {
          coreApi.getCallbackManager().completeCallback(packet.packetId(), responsePacket);
        } else {
          coreApi.getEventManager().callEvent(player.getUniqueId(), responsePacket);
        }
      } catch (Exception e) {
        logger.error("Unable to deserialize packet: " + e.getMessage());
      }
    }
  }

  public static CoreAPIImpl getCoreApi() {
    return coreApiStatic;
  }
}