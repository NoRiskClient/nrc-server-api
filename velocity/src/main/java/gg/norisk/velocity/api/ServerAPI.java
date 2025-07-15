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
import gg.norisk.velocity.Velocity;
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

    public static ServerAPI create(com.velocitypowered.api.plugin.Plugin plugin) {
        try {
            if (Velocity.getInstance() == null) {
                System.err.println("NoRiskClient-Server-API Velocity module is not loaded! Make sure it's in your plugins folder.");
                return null;
            }
            
            if (Velocity.getCoreApi() == null) {
                System.err.println("NoRiskClient-Server-API Velocity module failed to initialize properly.");
                return null;
            }
            
            System.out.println("NoRiskClient-Server-API initialized successfully");
            // Create a new instance for this plugin instead of sharing
            Velocity instance = Velocity.getInstance();
            return new ServerAPI(Velocity.getCoreApi(), instance.getServer(), instance.getLogger(), instance.getChannelIdentifier());
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
        
        // Use NrcPlayer to send the payload
        nrcPlayer.sendPayload(packet);
        logger.info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") successfully sent to " + uuid + ".");
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
        if (nrcPlayer == null) {
            logger.warn("Player " + uuid + " is not online or not a NoRisk player!");
            return;
        }

        // Use NrcPlayer to send the request
        nrcPlayer.sendRequest(coreAPI.getPluginChannel(), request, callback);
        logger.info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") successfully sent to " + uuid + ".");
    }

    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
} 