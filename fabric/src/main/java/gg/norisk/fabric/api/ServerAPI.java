package gg.norisk.fabric.api;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.exceptions.NoNrcPlayer;
import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;

import java.util.UUID;
import java.util.function.Consumer;

public class ServerAPI implements NoRiskServerAPI {
    private final CoreAPI coreAPI;

    public ServerAPI(CoreAPI coreAPI) {
        this.coreAPI = coreAPI;
    }

    @Override
    public void sendPacket(UUID uuid, OutPayload packet) {
        String json = coreAPI.serializePacket(packet);
    }

    @Override
    public <R extends InPayload> void sendRequest(UUID uuid, OutPayload request, Consumer<R> callback) {
        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
        if (nrcPlayer == null) return;
        
        String json;
        try {
            json = coreAPI.prepareRequest(uuid, request, callback);
        } catch (NoNrcPlayer e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerListener(PacketListener listener) {
        coreAPI.getEventManager().registerListener(listener);
    }
} 