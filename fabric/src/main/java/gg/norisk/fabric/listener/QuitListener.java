package gg.norisk.fabric.listener;

import gg.norisk.core.common.CoreAPI;
import net.minecraft.server.network.ServerPlayerEntity;

public record QuitListener(CoreAPI api) {

  public void onPlayerQuit(ServerPlayerEntity player) {
    api.getPlayerManager().removePlayer(player.getUuid());
  }
} 