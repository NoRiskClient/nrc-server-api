package gg.norisk.velocity.listener;

import gg.norisk.core.common.CoreAPI;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

public class QuitListener {
    private final CoreAPI api;
    public QuitListener(CoreAPI api) {
        this.api = api;
    }
    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        api.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
    }
} 