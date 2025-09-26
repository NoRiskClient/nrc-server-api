package gg.norisk.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import gg.norisk.core.common.CoreAPI;

public record QuitListener(CoreAPI api) {

  @Subscribe
  public void onDisconnect(DisconnectEvent event) {
    api.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
  }
} 