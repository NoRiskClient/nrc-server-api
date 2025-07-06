package gg.norisk.core.manager;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import gg.norisk.core.payloads.AbstractInputbarPayload;
import gg.norisk.core.payloads.InputbarResponsePayload;
import gg.norisk.core.payloads.Payloads;

public class InputbarPayloadManager {
    private static final ConcurrentHashMap<UUID, InputSession> sessions = new ConcurrentHashMap<>();
    
    private static class InputSession {
        private final AbstractInputbarPayload payload;
        private final BiConsumer<UUID, byte[]> sendToClient;
        private final Consumer<String> handler;
        private final Runnable onCancel;
        
        public InputSession(AbstractInputbarPayload payload, BiConsumer<UUID, byte[]> sendToClient, 
                          Consumer<String> handler, Runnable onCancel) {
            this.payload = payload;
            this.sendToClient = sendToClient;
            this.handler = handler;
            this.onCancel = onCancel;
        }
        
        public AbstractInputbarPayload getPayload() {
            return payload;
        }
        
        public BiConsumer<UUID, byte[]> getSendToClient() {
            return sendToClient;
        }
        
        public Consumer<String> getHandler() {
            return handler;
        }
        
        public Runnable getOnCancel() {
            return onCancel;
        }
    }
    
    public static void registerInputSession(UUID playerUuid, AbstractInputbarPayload payload, 
                                          BiConsumer<UUID, byte[]> sendToClient, Consumer<String> handler, 
                                          Runnable onCancel) {
        sessions.put(playerUuid, new InputSession(payload, sendToClient, handler, onCancel));
        Payloads.send(playerUuid, payload, sendToClient);
    }
    
    public static void registerInputSession(UUID playerUuid, AbstractInputbarPayload payload, 
                                          BiConsumer<UUID, byte[]> sendToClient, Consumer<String> handler) {
        registerInputSession(playerUuid, payload, sendToClient, handler, null);
    }
    
    public static void unregisterSession(UUID playerUuid) {
        sessions.remove(playerUuid);
    }
    
    public static void handleInputbarResponse(UUID playerUuid, InputbarResponsePayload payload) {
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
    
    public static void resendInputbar(UUID playerUuid) {
        InputSession session = sessions.get(playerUuid);
        if (session != null) {
            Payloads.send(playerUuid, session.getPayload(), session.getSendToClient());
        }
    }
} 