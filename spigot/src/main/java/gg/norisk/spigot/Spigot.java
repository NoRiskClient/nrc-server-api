package gg.norisk.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import gg.norisk.spigot.api.ServerAPI;
import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.common.PacketListener;
import gg.norisk.spigot.listener.QuitListener;
import gg.norisk.spigot.listener.JoinListener;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import lombok.Getter;

public class Spigot extends JavaPlugin implements Listener, PluginMessageListener {
    @Getter
    private static ServerAPI api;
    
    @Getter
    private static CoreAPIImpl coreApi;
    
    @Getter
    private static Spigot instance;

    @Override
    public void onEnable() {
        getLogger().info("NoRiskClient-Server-API Spigot module is starting...");
        
        instance = this;
        coreApi = new CoreAPIImpl();
        Spigot.api = new ServerAPI(coreApi, this);
        
        getServer().getPluginManager().registerEvents(new QuitListener(coreApi), this);
        api.registerListener(new JoinListener(coreApi));
        
        getServer().getMessenger().registerOutgoingPluginChannel(this, coreApi.getPluginChannel());
        getServer().getMessenger().registerIncomingPluginChannel(this, coreApi.getPluginChannel(), this);
        
        getLogger().info("NoRiskClient-Server-API Spigot module is ready!");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(coreApi.getPluginChannel())) return;
        getLogger().info("Received packet from " + player.getName());
        try {
            PacketWrapper packet = coreApi.serializePacketWrapper(message);
            getLogger().info("Packet: " + packet.payloadJson());
            InPayload responsePacket = coreApi.deserialize(packet);
            getLogger().info("Response packet: " + responsePacket);
            getLogger().info("Packet ID: " + packet.packetId());
            if (packet.packetId() != null && coreApi.getCallbackManager().waitingFor(packet.packetId())) {
                getLogger().info("Received response for packet ID " + packet.packetId());
                coreApi.getCallbackManager().completeCallback(packet.packetId(), responsePacket);
            } else {
                getLogger().info("Received response for unknown packet ID " + packet.packetId());
                coreApi.getEventManager().callEvent(player.getUniqueId(), responsePacket);
            }
        } catch (Exception e) {
            getLogger().severe("Unable to deserialize packet: " + e.getMessage());
        }
    }
} 