package gg.norisk.velocity;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import gg.norisk.core.common.Constants;
import gg.norisk.core.common.NoRiskServerApi;
import gg.norisk.core.manager.InputbarPayloadManager;
import gg.norisk.core.payloads.Payloads;
import gg.norisk.velocity.api.NrcChannelRegister;

@Plugin(
    id = "noriskclient-server-api",
    name = "NoRiskClient Server API",
    version = "0.1.0",
    description = "API to interact with the NoRisk Client.",
    authors = {"S42"}
)
public class Velocity {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final NoRiskServerApi api = new NoRiskServerApi();
    private final MinecraftChannelIdentifier channelIdentifier = MinecraftChannelIdentifier.from(Constants.NRC_CHANNEL);
    
    @Inject
    public Velocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("NoRiskClient-Server-API Velocity module is starting...");
        NrcChannelRegister.register(server, logger);
        logger.info("NoRiskClient-Server-API Velocity module is ready!");
    }
    
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        var player = event.getPlayer();
        Payloads.sendHandshake(player.getUniqueId(), (uuid, data) -> {
            player.getCurrentServer().ifPresent(serverConnection -> {
                serverConnection.sendPluginMessage(MinecraftChannelIdentifier.from(Constants.NRC_CHANNEL), data);
            });
        });
    }
    
    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        Payloads.onPlayerLeave(event.getPlayer().getUniqueId());
        InputbarPayloadManager.unregisterSession(event.getPlayer().getUniqueId());
    }
    
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getIdentifier().getId().equals(Constants.NRC_CHANNEL)) {
            var source = event.getSource();
            if (source instanceof com.velocitypowered.api.proxy.Player) {
                com.velocitypowered.api.proxy.Player player = (com.velocitypowered.api.proxy.Player) source;
                Payloads.receive(player.getUniqueId(), event.getData());
            }
        }
    }
    
    public NoRiskServerApi getApi() {
        return api;
    }
    
    public MinecraftChannelIdentifier getChannelIdentifier() {
        return channelIdentifier;
    }
} 