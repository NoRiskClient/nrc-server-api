package gg.norisk.bungeecord.api;

import gg.norisk.bungeecord.BungeeCord;
import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;
    private final BungeeCord bungeeCord;

    public static ServerAPI create(Plugin plugin) {
        try {
            if (BungeeCord.getInstance() == null) {
                plugin.getLogger().severe("NoRiskClient-Server-API BungeeCord module is not loaded! Make sure it's in your plugins folder.");
                return null;
            }
            
            if (BungeeCord.getCoreApi() == null) {
                plugin.getLogger().severe("NoRiskClient-Server-API BungeeCord module failed to initialize properly.");
                return null;
            }
            
            plugin.getLogger().info("NoRiskClient-Server-API initialized successfully");
            // Create a new instance for this plugin instead of sharing
            return new ServerAPI(BungeeCord.getCoreApi(), BungeeCord.getInstance());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize NoRisk API: " + e.getMessage());
            return null;
        }
    }

    public static boolean isAvailable() {
        try {
            return BungeeCord.getInstance() != null && BungeeCord.getCoreApi() != null;
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
        bungeeCord.getLogger().info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") successfully sent to " + uuid + ".");
    }

    public void sendPacket(UUID uuid, String channel, byte[] data) {
        ProxiedPlayer player = bungeeCord.getProxy().getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (!coreAPI.isNrcPlayer(uuid)) {
            return;
        }
        Runnable runnable = () -> {
            if (player.getServer() != null) {
                player.getServer().sendData(channel, data);
                bungeeCord.getLogger().info("[NoRiskClientServerAPI] Payload successfully sent to " + uuid + ".");
            }
        };

        bungeeCord.getProxy().getScheduler().schedule(bungeeCord, runnable, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
        if (nrcPlayer == null) {
            bungeeCord.getLogger().warning("Player " + uuid + " is not online or not a NoRisk player!");
            return;
        }

        // Use NrcPlayer to send the request
        nrcPlayer.sendRequest(coreAPI.getPluginChannel(), request, callback);
        bungeeCord.getLogger().info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") successfully sent to " + uuid + ".");
    }

    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
}
