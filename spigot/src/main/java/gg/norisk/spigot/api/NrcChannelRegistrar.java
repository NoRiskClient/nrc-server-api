package gg.norisk.spigot.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import gg.norisk.core.common.Constants;

public class NrcChannelRegistrar {
    public static void register(JavaPlugin plugin) {
        plugin.getLogger().info("Registering '" + Constants.NRC_CHANNEL + "' channel...");
        var server = plugin.getServer();
        server.getMessenger().registerOutgoingPluginChannel(plugin, Constants.NRC_CHANNEL);
        server.getMessenger().registerIncomingPluginChannel(plugin, Constants.NRC_CHANNEL, (PluginMessageListener) plugin);
        plugin.getLogger().info("Channel registration complete.");
    }
} 