package gg.norisk.spigot.listener;

import gg.norisk.core.common.CoreAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record QuitListener(CoreAPI api) implements Listener {

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    api.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
  }
} 