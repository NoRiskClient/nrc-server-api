package gg.norisk.paper.listener;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.PacketHandler;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.in.HandshakePayload;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.UUID;

@RequiredArgsConstructor
public class JoinListener implements PacketListener {

    private final CoreAPI coreAPI;

    @PacketHandler
    public void onPlayerJoin(UUID uuid, HandshakePayload payload) {
        Bukkit.broadcastMessage(uuid + " joined the game!");
        coreAPI.registerPlayer(uuid);
    }

}
