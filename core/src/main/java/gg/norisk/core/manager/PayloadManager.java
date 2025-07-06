package gg.norisk.core.manager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

import gg.norisk.core.channel.ChannelApi;
import gg.norisk.core.payloads.AbstractPayload;

public class PayloadManager {
    private static final ConcurrentHashMap<UUID, ConcurrentLinkedQueue<AbstractPayload>> playerPayloads = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Class<? extends AbstractPayload>> registeredPayloads = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Long> payloadSendAllowedAt = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Timer> payloadTimeouts = new ConcurrentHashMap<>();
    
    private static final long PAYLOAD_DELAY_MS = 5000L;
    private static final long PAYLOAD_TIMEOUT_MS = 2000L;
    
    private static BiConsumer<UUID, byte[]> sendToClientCallback;
    
    public static void registerPayloadType(String type, Class<? extends AbstractPayload> clazz) {
        registeredPayloads.put(type, clazz);
    }
    
    public static void registerPlayer(UUID uuid) {
        playerPayloads.putIfAbsent(uuid, new ConcurrentLinkedQueue<>());
    }
    
    public static void unregisterPlayer(UUID uuid) {
        playerPayloads.remove(uuid);
    }
    
    public static void queuePayload(UUID uuid, AbstractPayload payload) {
        playerPayloads.computeIfAbsent(uuid, k -> new ConcurrentLinkedQueue<>()).add(payload);
    }
    
    public static void sendToPlayer(UUID uuid, AbstractPayload payload, BiConsumer<UUID, byte[]> sendToClient) {
        Long allowedAt = payloadSendAllowedAt.get(uuid);
        if (allowedAt != null && System.currentTimeMillis() < allowedAt) {
            queuePayload(uuid, payload);
            return;
        }
        
        byte[] data = ChannelApi.send(payload);
        sendToClient.accept(uuid, data);
        
        String key = uuid + ":" + payload.getType();
        Timer existingTimer = payloadTimeouts.remove(key);
        if (existingTimer != null) {
            existingTimer.cancel();
        }
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                payloadTimeouts.remove(key);
            }
        }, PAYLOAD_TIMEOUT_MS);
        payloadTimeouts.put(key, timer);
    }
    
    public static void onPayloadAck(UUID uuid, String payloadType) {
        String key = uuid + ":" + payloadType;
        Timer timer = payloadTimeouts.remove(key);
        if (timer != null) {
            timer.cancel();
        }
    }
    
    public static void sendQueuedPayloads(UUID uuid, BiConsumer<UUID, byte[]> sendToClient) {
        Long allowedAt = payloadSendAllowedAt.get(uuid);
        if (allowedAt != null && System.currentTimeMillis() < allowedAt) {
            return;
        }
        
        ConcurrentLinkedQueue<AbstractPayload> queue = playerPayloads.get(uuid);
        if (queue == null) return;
        
        while (!queue.isEmpty()) {
            AbstractPayload payload = queue.poll();
            if (payload != null) {
                sendToPlayer(uuid, payload, sendToClient);
            }
        }
    }
    
    public static void broadcast(AbstractPayload payload, BiConsumer<UUID, byte[]> sendToClient) {
        for (UUID uuid : playerPayloads.keySet()) {
            sendToPlayer(uuid, payload, sendToClient);
        }
    }
    
    public static void setSendToClientCallback(BiConsumer<UUID, byte[]> callback) {
        sendToClientCallback = callback;
    }
    
    public static void onPayloadReceived(UUID sender, String payloadJson) {
        if (payloadJson.contains("\"type\":\"handshake\"")) {
            payloadSendAllowedAt.put(sender, System.currentTimeMillis() + PAYLOAD_DELAY_MS);
            
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(PAYLOAD_DELAY_MS);
                    payloadSendAllowedAt.remove(sender);
                    BiConsumer<UUID, byte[]> callback = sendToClientCallback;
                    if (callback != null) {
                        sendQueuedPayloads(sender, callback);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.start();
        }
    }
    
    public static void onPlayerLeave(UUID uuid) {
        // Remove all payload timeouts for this player
        payloadTimeouts.entrySet().removeIf(entry -> {
            String key = entry.getKey();
            if (key.startsWith(uuid.toString() + ":")) {
                entry.getValue().cancel();
                return true;
            }
            return false;
        });
    }
} 