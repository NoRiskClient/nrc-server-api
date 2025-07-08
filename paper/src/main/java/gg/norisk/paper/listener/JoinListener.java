package gg.norisk.paper.listener;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.payloads.in.AckPayload;
import gg.norisk.core.payloads.out.HandshakePayload;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class JoinListener implements Listener {

    private final NoRiskServerAPI api;
    private final CoreAPI coreAPI;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        api.<AckPayload>sendRequest(player.getUniqueId(), new HandshakePayload(), packetResponse -> {
            Bukkit.broadcastMessage(player.getName() + " joined the game");
            coreAPI.registerPlayer(player.getUniqueId());
        });
    }
}
