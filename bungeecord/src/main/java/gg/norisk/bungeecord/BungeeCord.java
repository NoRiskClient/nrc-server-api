package gg.norisk.bungeecord;

import gg.norisk.bungeecord.api.NrcChannelRegister;
import gg.norisk.core.common.Constants;
import gg.norisk.core.common.NoRiskServerApi;
import gg.norisk.core.manager.InputbarPayloadManager;
import gg.norisk.core.payloads.Payloads;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeeCord extends Plugin implements Listener {
    private final NoRiskServerApi api = new NoRiskServerApi();
    
    @Override
    public void onEnable() {
        getLogger().info("NoRiskClient-Server-API BungeeCord module is starting...");
        NrcChannelRegister.register(this);
        getProxy().getPluginManager().registerListener(this, this);
        getLogger().info("NoRiskClient-Server-API BungeeCord module is ready!");
    }
    
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Payloads.sendHandshake(player.getUniqueId(), (uuid, data) -> {
            if (player.getServer() != null) {
                player.getServer().sendData(Constants.NRC_CHANNEL, data);
            }
        });
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        Payloads.onPlayerLeave(event.getPlayer().getUniqueId());
        InputbarPayloadManager.unregisterSession(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (Constants.NRC_CHANNEL.equals(event.getTag())) {
            var sender = event.getSender();
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                Payloads.receive(player.getUniqueId(), event.getData());
            }
        }
    }
    
    public NoRiskServerApi getApi() {
        return api;
    }
} 