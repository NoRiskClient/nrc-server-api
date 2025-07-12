package gg.norisk.spigot.api;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;
    private final JavaPlugin plugin;

    public ServerAPI(CoreAPI coreAPI, JavaPlugin plugin) {
        this.coreAPI = coreAPI;
        this.plugin = plugin;
    }

    @Override
    public void sendPacket(UUID uuid, OutPayload packet) {
        Player player = plugin.getServer().getPlayer(uuid);
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
                player.sendPluginMessage(plugin, coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
                Bukkit.getLogger().info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
            }
        };
        if (packet instanceof gg.norisk.core.payloads.in.HandshakePayload) {
            runnable.runTaskLater(plugin, 10L);
        } else {
            runnable.runTask(plugin);
        }
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) {
            plugin.getLogger().warning("Player " + uuid + " is not online!");
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
                Bukkit.getLogger().info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
            }
        }.runTaskLater(plugin, 10L);
    }

    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
} 