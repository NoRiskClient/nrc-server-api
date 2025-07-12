package gg.norisk.fabric.listener;

import gg.norisk.core.common.CoreAPI;
import net.minecraft.server.network.ServerPlayerEntity;

public class QuitListener {
    private final CoreAPI api;
    
    public QuitListener(CoreAPI api) {
        this.api = api;
    }
    
    public void onPlayerQuit(ServerPlayerEntity player) {
        api.getPlayerManager().removePlayer(player.getUuid());
    }
} 