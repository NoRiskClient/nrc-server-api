package gg.norisk.bungeecord.listener;

import gg.norisk.core.common.CoreAPI;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public record QuitListener(CoreAPI api) implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        api.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
    }
} 