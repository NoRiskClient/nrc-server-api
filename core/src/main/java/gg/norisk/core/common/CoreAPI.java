package gg.norisk.core.common;

import gg.norisk.core.manager.CallbackManager;
import gg.norisk.core.manager.PacketEventManager;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;

import java.util.UUID;

/**
 * Main API class for creating and managing NoRisk Client payloads
 * This class provides convenient methods to create various types of payloads
 * that can be sent to NoRisk Client users.
 */
public interface CoreAPI {
    String getPluginChannel();

    boolean isNrcPlayer(UUID uuid);

    String serializePacket(OutPayload payload);

    CallbackManager getCallbackManager();

    InPayload deserialize(String json);

    PacketWrapper serializePacketWrapper(byte[] bytes);

    PacketEventManager getEventManager();

    void registerPlayer(UUID uniqueId);

    void unregisterPlayer(UUID uniqueId);
}