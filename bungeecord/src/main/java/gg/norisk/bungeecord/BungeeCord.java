package gg.norisk.bungeecord;

import gg.norisk.bungeecord.api.ServerAPI;
import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.bungeecord.listener.QuitListener;
import gg.norisk.bungeecord.listener.JoinListener;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeeCord extends Plugin implements Listener {
    @Getter
    private static ServerAPI api;
    
    @Getter
    private static CoreAPIImpl coreApi;
    
    @Getter
    private static BungeeCord instance;
    
    @Override
    public void onEnable() {
        getLogger().info("NoRiskClient-Server-API BungeeCord module is starting...");
        
        instance = this;
        coreApi = new CoreAPIImpl();

        BungeeCord.api = new ServerAPI(BungeeCord.coreApi, this);

        coreApi.setServerAPI(BungeeCord.api);

        getProxy().registerChannel(coreApi.getPluginChannel());
        getProxy().getPluginManager().registerListener(this, new QuitListener(coreApi));

        api.registerListener(new JoinListener(coreApi));
        
        getLogger().info("NoRiskClient-Server-API BungeeCord module is ready!");
    }
    
    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {

        if (!coreApi.getPluginChannel().equals(event.getTag())) {
            return;
        }

        if (event.getSender() instanceof ProxiedPlayer player) {
            try {
                PacketWrapper packet = coreApi.serializePacketWrapper(event.getData());
                InPayload responsePacket = coreApi.deserialize(packet);

                if (packet.packetId() != null && coreApi.getCallbackManager().waitingFor(packet.packetId())) {
                    coreApi.getCallbackManager().completeCallback(packet.packetId(), responsePacket);
                } else {
                    coreApi.getEventManager().callEvent(player.getUniqueId(), responsePacket);
                }
            } catch (Exception e) {
                getLogger().severe("Unable to deserialize packet: " + e.getMessage());
            }
        }
    }
} 