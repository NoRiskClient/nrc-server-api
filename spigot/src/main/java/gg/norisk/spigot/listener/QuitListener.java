package gg.norisk.spigot.listener;

import gg.norisk.core.common.CoreAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    private final CoreAPI api;
    public QuitListener(CoreAPI api) {
        this.api = api;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
    }
} 