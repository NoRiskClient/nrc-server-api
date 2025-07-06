package gg.norisk.core.manager;

import gg.norisk.core.manager.model.InputSession;
import gg.norisk.core.payloads.AbstractInputbarPayload;
import gg.norisk.core.payloads.InputbarResponsePayload;
import gg.norisk.core.payloads.Payloads;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InputbarPayloadManager {
    private final ConcurrentHashMap<UUID, InputSession> sessions = new ConcurrentHashMap<>();
    private final Payloads payloads;

    public InputbarPayloadManager() {
        this.payloads = new Payloads();
    }

    public void registerInputSession(UUID playerUuid, AbstractInputbarPayload payload,
                                     BiConsumer<UUID, byte[]> sendToClient, Consumer<String> handler,
                                     Runnable onCancel) {
        sessions.put(playerUuid, new InputSession(payload, sendToClient, handler, onCancel));
        payloads.send(playerUuid, payload, sendToClient);
    }

    public void registerInputSession(UUID playerUuid, AbstractInputbarPayload payload,
                                     BiConsumer<UUID, byte[]> sendToClient, Consumer<String> handler) {
        registerInputSession(playerUuid, payload, sendToClient, handler, null);
    }

    public void unregisterSession(UUID playerUuid) {
        sessions.remove(playerUuid);
    }

    public void handleInputbarResponse(UUID playerUuid, InputbarResponsePayload payload) {
        InputSession session = sessions.get(playerUuid);
        if (session == null) {
            return;
        }

        if (payload.isCanceled()) {
            if (session.getOnCancel() != null) {
                session.getOnCancel().run();
            }
        } else if (payload.getInput() != null) {
            session.getHandler().accept(payload.getInput());
            unregisterSession(playerUuid);
        }
    }

    public void resendInputbar(UUID playerUuid) {
        InputSession session = sessions.get(playerUuid);
        if (session != null) {
            payloads.send(playerUuid, session.getPayload(), session.getSendToClient());
        }
    }
} 