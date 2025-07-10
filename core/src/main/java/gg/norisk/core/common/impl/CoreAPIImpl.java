package gg.norisk.core.common.impl;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.manager.CallbackManager;
import gg.norisk.core.manager.PacketEventManager;
import gg.norisk.core.manager.PacketManager;
import gg.norisk.core.manager.PlayerManager;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;

public class CoreAPIImpl implements CoreAPI {

    private final PlayerManager playerManager;
    private final PacketEventManager packetEventManager;
    private final CallbackManager callbackManager;
    private final PacketManager packetManager;
    private NoRiskServerAPI serverAPI;

    public CoreAPIImpl() {
        this.playerManager = new PlayerManager();
        this.packetEventManager = new PacketEventManager();
        this.callbackManager = new CallbackManager();
        this.packetManager = new PacketManager();
    }

    public void setServerAPI(NoRiskServerAPI serverAPI) {
        this.serverAPI = serverAPI;
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
        return packetManager.serializePacketWrapper(packetManager.serializeOutPayload(payload));
    }

    @Override
    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    @Override
    public InPayload deserialize(PacketWrapper wrapper) {
        return packetManager.deserializeInPayload(wrapper);
    }

    @Override
    public PacketWrapper serializePacketWrapper(byte[] bytes) {
        return packetManager.deserializePacketWrapper(bytes);
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

    @Override
    public <R extends InPayload> String prepareRequest(UUID uuid, OutPayload request, Consumer<R> callback) throws NoNrcPlayer, ClassCastException {
        if (!isNrcPlayer(uuid)) {
            throw new NoNrcPlayer();
        }

        UUID requestId = UUID.randomUUID();

        getCallbackManager().addCallback(requestId, response -> {
            try {
                // Cast the response to the expected type
                @SuppressWarnings("unchecked")
                R typedResponse = (R) response;
                callback.accept(typedResponse);
            } catch (ClassCastException e) {
                throw e;
            }
        });

        return serializePacket(request);
    }

    @Override
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}