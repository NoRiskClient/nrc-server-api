package gg.norisk.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import gg.norisk.core.common.Constants;
import gg.norisk.core.common.NoRiskServerApi;
import gg.norisk.core.manager.InputbarPayloadManager;
import gg.norisk.core.payloads.Payloads;
import gg.norisk.spigot.api.NrcChannelRegistrar;

public class Spigot extends JavaPlugin implements Listener, PluginMessageListener {
    private final NoRiskServerApi api = new NoRiskServerApi();
    
    @Override
    public void onEnable() {
        getLogger().info("NoRiskClient-Server-API Spigot module is starting...");
        NrcChannelRegistrar.register(this);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoRiskClient-Server-API Spigot module is ready!");
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Payloads.sendHandshake(player.getUniqueId(), (uuid, data) -> {
            player.sendPluginMessage(this, Constants.NRC_CHANNEL, data);
        });
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Payloads.onPlayerLeave(event.getPlayer().getUniqueId());
        InputbarPayloadManager.unregisterSession(event.getPlayer().getUniqueId());
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (Constants.NRC_CHANNEL.equals(channel)) {
            Payloads.receive(player.getUniqueId(), message);
        }
    }
    
    public NoRiskServerApi getApi() {
        return api;
    }
} 