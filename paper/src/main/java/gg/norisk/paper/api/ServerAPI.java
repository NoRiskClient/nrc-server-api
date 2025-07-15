package gg.norisk.paper.api;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.paper.Paper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;
    private final Paper paper;

    public ServerAPI(CoreAPI coreAPI, Paper paper) {
        this.coreAPI = coreAPI;
        this.paper = paper;
    }

    public ServerAPI create(Plugin plugin) {
        try {
            if (Paper.getInstance() == null) {
                plugin.getLogger().severe("NoRiskClient-Server-API Paper module is not loaded! Make sure it's in your plugins folder.");
                return null;
            }
            
            if (Paper.getCoreApi() == null) {
                plugin.getLogger().severe("NoRiskClient-Server-API Paper module failed to initialize properly.");
                return null;
            }
            
            plugin.getLogger().info("NoRiskClient-Server-API initialized successfully");
            return new ServerAPI(Paper.getCoreApi(), Paper.getInstance());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize NoRisk API: " + e.getMessage());
            return null;
        }
    }

    public static boolean isAvailable() {
        try {
            return Paper.getInstance() != null && Paper.getCoreApi() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void sendPacket(UUID uuid, OutPayload packet) {
        Player player = paper.getServer().getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (!coreAPI.isNrcPlayer(uuid)) {
            return;
        }
        String json = coreAPI.serializePacket(packet);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendPluginMessage(paper, coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
                Bukkit.getLogger().info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") successfully sent to " + uuid + ".");
            }
        };
        if (packet instanceof gg.norisk.core.payloads.in.HandshakePayload) {
            runnable.runTaskLater(paper, 10L);
        } else {
            runnable.runTask(paper);
        }
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        Player player = paper.getServer().getPlayer(uuid);

        if (player == null) {
            paper.getLogger().warning("Player " + uuid + " is not online!");
            return;
        }

        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);

        if (nrcPlayer == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                String json = null;
                try {
                    json = coreAPI.prepareRequest(uuid, request, callback);
                } catch (NoNrcPlayer e) {
                    throw new RuntimeException(e);
                }
                nrcPlayer.sendPayload(coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
                Bukkit.getLogger().info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") successfully sent to " + uuid + ".");
            }
        }.runTaskLater(paper, 10L);
    }

    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
}
