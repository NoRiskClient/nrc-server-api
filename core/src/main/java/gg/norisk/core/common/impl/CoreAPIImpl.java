package gg.norisk.core.common.impl;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.manager.CallbackManager;
import gg.norisk.core.manager.PacketEventManager;
import gg.norisk.core.manager.PacketManager;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.manager.PlayerManager;

import java.util.UUID;

public class CoreAPIImpl implements CoreAPI {

    private final PlayerManager playerManager;
    private final PacketEventManager packetEventManager;
    private final CallbackManager callbackManager;
    private final PacketManager packetManager;

    public CoreAPIImpl() {
        this.playerManager = new PlayerManager();
        this.packetEventManager = new PacketEventManager();
        this.callbackManager = new CallbackManager();
        this.packetManager = new PacketManager();
    }

    @Override
    public String getPluginChannel() {
        return "norisk:main";
    }

    @Override
    public boolean isNrcPlayer(UUID uuid) {
        return playerManager.isNrcPlayer(uuid);
    }

    @Override
    public String serializePacket(OutPayload payload) {
        return "";
    }

    @Override
    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    @Override
    public InPayload deserialize(String json) {
        return null;
    }

    @Override
    public PacketWrapper serializePacketWrapper(byte[] bytes) {
        return null;
    }

    @Override
    public PacketEventManager getEventManager() {
        return this.packetEventManager;
    }

    @Override
    public void registerPlayer(UUID uniqueId) {
        this.playerManager.setNrcPlayer(uniqueId, true);
    }

    @Override
    public void unregisterPlayer(UUID uniqueId) {
        this.playerManager.removePlayer(uniqueId);
    }
}