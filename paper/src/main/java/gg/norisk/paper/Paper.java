package gg.norisk.paper;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.impl.CoreAPIImpl;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.paper.api.ServerAPI;
import gg.norisk.paper.listener.JoinListener;
import gg.norisk.paper.listener.QuitListener;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class Paper extends JavaPlugin implements Listener, PluginMessageListener {
    @Getter
    private static ServerAPI api;

    private CoreAPI coreApi;

    @Override
    public void onEnable() {
        getLogger().info("NoRiskClient-Server-API Paper module is starting...");

        coreApi = new CoreAPIImpl();
        Paper.api = new ServerAPI(coreApi, this);

        getServer().getPluginManager().registerEvents(new QuitListener(coreApi), this);

        api.registerListener(new JoinListener(coreApi));

        getServer().getMessenger().registerOutgoingPluginChannel(this, coreApi.getPluginChannel());
        getServer().getMessenger().registerIncomingPluginChannel(this, coreApi.getPluginChannel(), this);

        getLogger().info("NoRiskClient-Server-API Paper module is ready!");
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(coreApi.getPluginChannel())) return;

        getLogger().info("Received packet from " + player.getName());

        try {
            PacketWrapper packet = coreApi.serializePacketWrapper(message);
            getLogger().info("Packet: " + packet.payloadJson());
            InPayload responsePacket = coreApi.deserialize(packet.payloadJson());
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