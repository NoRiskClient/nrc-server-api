package gg.norisk.velocity.api;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;
    private final ProxyServer server;
    private final Logger logger;
    private final MinecraftChannelIdentifier channelIdentifier;

    public ServerAPI(CoreAPI coreAPI, ProxyServer server, Logger logger, MinecraftChannelIdentifier channelIdentifier) {
        this.coreAPI = coreAPI;
        this.server = server;
        this.logger = logger;
        this.channelIdentifier = channelIdentifier;
    }

    @Override
    public void sendPacket(UUID uuid, OutPayload packet) {
        Player player = server.getPlayer(uuid).orElse(null);
        if (player == null) {
            return;
        }
        if (!coreAPI.isNrcPlayer(uuid)) {
            return;
        }
        String json = coreAPI.serializePacket(packet);
        player.getCurrentServer().ifPresent(serverConnection -> {
            serverConnection.sendPluginMessage(channelIdentifier, json.getBytes(StandardCharsets.UTF_8));
            logger.info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
        });
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        Player player = server.getPlayer(uuid).orElse(null);
        if (player == null) {
            logger.warn("Player " + uuid + " is not online!");
            return;
        }
        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
        if (nrcPlayer == null) return;
        String json;
        try {
            json = coreAPI.prepareRequest(uuid, request, callback);
        } catch (NoNrcPlayer e) {
            throw new RuntimeException(e);
        }
        player.getCurrentServer().ifPresent(serverConnection -> {
            serverConnection.sendPluginMessage(channelIdentifier, json.getBytes(StandardCharsets.UTF_8));
            logger.info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
        });
    }

    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
} 