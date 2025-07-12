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

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;
    private final BungeeCord bungeeCord;

    @Override
    public void sendPacket(UUID uuid, OutPayload packet) {
        ProxiedPlayer player = bungeeCord.getProxy().getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (!coreAPI.isNrcPlayer(uuid)) {
            return;
        }
        String json = coreAPI.serializePacket(packet);
        Runnable runnable = () -> {
            if (player.getServer() != null) {
                player.getServer().sendData(coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
                bungeeCord.getLogger().info("[NoRiskClientServerAPI] Payload (" + packet.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
            }
        };

        if (packet instanceof gg.norisk.core.payloads.in.HandshakePayload) {
            bungeeCord.getProxy().getScheduler().schedule(bungeeCord, runnable, 10L, TimeUnit.MILLISECONDS);
        } else {
            bungeeCord.getProxy().getScheduler().schedule(bungeeCord, runnable, 0L, TimeUnit.MILLISECONDS);
        }
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
                bungeeCord.getLogger().info("[NoRiskClientServerAPI] Payload erfolgreich an " + uuid + " gesendet.");
            }
        };

        bungeeCord.getProxy().getScheduler().schedule(bungeeCord, runnable, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        ProxiedPlayer player = bungeeCord.getProxy().getPlayer(uuid);

        if (player == null) {
            bungeeCord.getLogger().warning("Player " + uuid + " is not online!");
            return;
        }

        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);

        if (nrcPlayer == null) return;

        bungeeCord.getProxy().getScheduler().schedule(bungeeCord, () -> {
            String json = null;
            try {
                json = coreAPI.prepareRequest(uuid, request, callback);
            } catch (NoNrcPlayer e) {
                throw new RuntimeException(e);
            }
            nrcPlayer.sendPayload(coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
            bungeeCord.getLogger().info("[NoRiskClientServerAPI] Request (" + request.getClass().getSimpleName() + ") erfolgreich an " + uuid + " gesendet.");
        }, 10L, TimeUnit.MILLISECONDS);
    }


    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
}
