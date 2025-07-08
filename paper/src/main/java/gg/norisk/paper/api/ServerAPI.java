package gg.norisk.paper.api;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.paper.Paper;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

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

        String json = coreAPI.serializePacket(packet);
        player.sendPluginMessage(paper, coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        Player player = paper.getServer().getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (!coreAPI.isNrcPlayer(uuid)) {
            return;
        }

        UUID requestId = UUID.randomUUID();

        coreAPI.getCallbackManager().addCallback(requestId, response -> {
            if (callback.getClass().isInstance(response)) {
                callback.accept((R) response);
            }
        });

        String json = coreAPI.serializePacket(request);
        player.sendPluginMessage(paper, coreAPI.getPluginChannel(), json.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
}
