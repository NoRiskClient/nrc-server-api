package gg.norisk.core.manager;

import gg.norisk.core.payloads.InPayload;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class CallbackManager {
    private final Map<UUID, Consumer<InPayload>> pendingRequests = new ConcurrentHashMap<>();

    public void addCallback(UUID requestId, Consumer<InPayload> callback) {
        pendingRequests.put(requestId, callback);
        // TODO: Timeout
    }

    public void completeCallback(UUID packetId, InPayload response) {
        Consumer<InPayload> callback = pendingRequests.remove(packetId);
        if (callback != null) {
            callback.accept(response);
        }
    }

    public boolean waitingFor(UUID packetId) {
        return pendingRequests.containsKey(packetId);
    }
}