package gg.norisk.bungeecord;

import gg.norisk.bungeecord.api.ServerAPI;
import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.common.PacketListener;
import gg.norisk.bungeecord.listener.QuitListener;
import gg.norisk.bungeecord.listener.JoinListener;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeeCord extends Plugin implements Listener {
    private static ServerAPI api;
    private CoreAPIImpl coreApi;
    
    @Override
    public void onEnable() {
        getLogger().info("NoRiskClient-Server-API BungeeCord module is starting...");
        coreApi = new CoreAPIImpl();
        BungeeCord.api = new ServerAPI(coreApi, this);
        getProxy().registerChannel(coreApi.getPluginChannel());
        getProxy().getPluginManager().registerListener(this, new QuitListener(coreApi));
        api.registerListener(new JoinListener(coreApi));
        getLogger().info("NoRiskClient-Server-API BungeeCord module is ready!");
    }
    
    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!coreApi.getPluginChannel().equals(event.getTag())) return;
        var sender = event.getSender();
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            getLogger().info("Received packet from " + player.getName());
            try {
                PacketWrapper packet = coreApi.serializePacketWrapper(event.getData());
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
    
    public static ServerAPI getApi() {
        return api;
    }
} 