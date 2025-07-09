package gg.norisk.paper.api;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.paper.Paper;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;
    private final Paper paper;

    @Override
    public void sendPacket(UUID uuid, OutPayload packet) {
        Player player = paper.getServer().getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (!coreAPI.isNrcPlayer(uuid)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                String json = coreAPI.serializePacket(packet);
                player.sendPluginMessage(paper, coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
                Bukkit.getLogger().info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
            }
        }.runTaskLater(paper, 10L);
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        Player player = paper.getServer().getPlayer(uuid);

        if (player == null) {
            paper.getLogger().warning("Player " + uuid + " is not online!");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                String json = null;
                try {
                    json = coreAPI.prepareRequest(uuid, request, callback);
                } catch (NoNrcPlayer e) {
                    throw new RuntimeException(e);
                }
                player.sendPluginMessage(paper, coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
                Bukkit.getLogger().info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
            }
        }.runTaskLater(paper, 10L);
    }


    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
}
