package gg.norisk.bungeecord.api;

import gg.norisk.core.common.Constants;
import net.md_5.bungee.api.plugin.Plugin;

public class NrcChannelRegister {
    public static void register(Plugin plugin) {
        plugin.getLogger().info("Registering '" + Constants.NRC_CHANNEL + "' channel...");
        var proxy = plugin.getProxy();
        proxy.registerChannel(Constants.NRC_CHANNEL);
        plugin.getLogger().info("Channel registration complete.");
    }
} 