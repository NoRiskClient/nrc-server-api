package gg.norisk.paper.listener;

import gg.norisk.core.common.CoreAPI;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitListener implements Listener {

    private final CoreAPI api;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
    }
}
