package gg.norisk.core.payloads;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

import com.google.gson.Gson;

import gg.norisk.core.channel.ChannelApi;
import gg.norisk.core.manager.InputbarPayloadManager;

public class Payloads {
    private static final Map<String, Class<? extends AbstractPayload>> payloadTypes = new HashMap<>();
    private static final ConcurrentHashMap<UUID, Boolean> handshakeDone = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, ConcurrentLinkedQueue<PayloadQueueItem>> queuedPayloads = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    private static final ConcurrentHashMap<UUID, Boolean> waitingForAck = new ConcurrentHashMap<>();

    private record PayloadQueueItem(AbstractPayload payload,
                                    BiConsumer<UUID, byte[]> sendToClient,
                                    boolean requiresAck) {
    }
    
    public static void register(String type, Class<? extends AbstractPayload> clazz) {
        payloadTypes.put(type, clazz);
    }
    
    public static Class<? extends AbstractPayload> get(String type) {
        return payloadTypes.get(type);
    }
    
    public static Map<String, Class<? extends AbstractPayload>> all() {
        return new HashMap<>(payloadTypes);
    }
    
    public static void registerDefaults() {
        register("toast", AbstractToastPayload.class);
        register("module_deactivate", AbstractModuleDeactivatePayload.class);
        register("inputbar_response", InputbarResponsePayload.class);
    }
    
    private static byte[] encodeVarInt(int value) {
        List<Byte> out = new ArrayList<>();
        int v = value;
        while (true) {
            if ((v & ~0x7F) == 0) {
                out.add((byte) v);
                break;
            } else {
                out.add((byte) ((v & 0x7F) | 0x80));
                v >>>= 7;
            }
        }
        byte[] result = new byte[out.size()];
        for (int i = 0; i < out.size(); i++) {
            result[i] = out.get(i);
        }
        return result;
    }
    
    private static byte[] wrapWithVarIntLength(byte[] data) {
        byte[] lengthPrefix = encodeVarInt(data.length);
        byte[] result = new byte[lengthPrefix.length + data.length];
        System.arraycopy(lengthPrefix, 0, result, 0, lengthPrefix.length);
        System.arraycopy(data, 0, result, lengthPrefix.length, data.length);
        return result;
    }
    
    public static void sendHandshake(UUID uuid, BiConsumer<UUID, byte[]> sendToClient) {
        HandshakePayload handshake = new HandshakePayload();
        byte[] data = ChannelApi.send(handshake);
        byte[] wrapped = wrapWithVarIntLength(data);
        sendToClient.accept(uuid, wrapped);
    }
    
    public static void send(UUID uuid, AbstractPayload payload, BiConsumer<UUID, byte[]> sendToClient) {
        queuedPayloads.computeIfAbsent(uuid, k -> new ConcurrentLinkedQueue<>())
                .add(new PayloadQueueItem(payload, sendToClient, false));
        trySendNext(uuid);
    }
    
    private static void trySendNext(UUID uuid) {
        if (Boolean.TRUE.equals(handshakeDone.get(uuid))) {
            ConcurrentLinkedQueue<PayloadQueueItem> queue = queuedPayloads.get(uuid);
            if (queue == null) return;
            
            while (!queue.isEmpty()) {
                PayloadQueueItem next = queue.poll();
                if (next == null) break;
                
                if ("handshake".equals(next.payload().getType())) {
                    byte[] data = ChannelApi.send(next.payload());
                    byte[] wrapped = wrapWithVarIntLength(data);
                    next.sendToClient().accept(uuid, wrapped);
                } else {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            byte[] data = ChannelApi.send(next.payload());
                            byte[] wrapped = wrapWithVarIntLength(data);
                            next.sendToClient().accept(uuid, wrapped);
                        }
                    }, 1000);
                }
            }
        }
    }
    
    public static void receive(UUID uuid, byte[] message) {
        String rawString = new String(message, StandardCharsets.UTF_8);
        
        String cleanedString = rawString.startsWith("+") ? rawString.substring(1) : rawString;
        
        int jsonStart = cleanedString.indexOf('{');
        if (jsonStart == -1) {
            return;
        }
        
        int classNameStart = cleanedString.indexOf('(');
        int classNameEnd = cleanedString.indexOf('{');
        String packetClassName = null;
        if (classNameStart != -1 && classNameEnd != -1 && classNameEnd > classNameStart + 1) {
            packetClassName = cleanedString.substring(classNameStart + 1, classNameEnd).trim();
        }
        
        String payloadJson = cleanedString.substring(jsonStart);
        try {
            if (payloadJson.contains("inputbar_response")) {
                try {
                    InputbarResponsePayload payload = gson.fromJson(payloadJson, InputbarResponsePayload.class);
                    InputbarPayloadManager.handleInputbarResponse(uuid, payload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } else if ("{\"type\":\"handshake\"}".equals(payloadJson)) {
                System.out.println("Player " + uuid + " is a NRC client.");
                onHandshakeReceived(uuid);
                return;
            } else if ("{\"type\":\"ack\"}".equals(payloadJson)) {
                trySendNext(uuid);
                return;
            }
            
            if (packetClassName != null && !packetClassName.isEmpty()) {
                if ((packetClassName.endsWith("HandshakePayload") || 
                     "gg.norisk.core.payloads.HandshakePayload".equals(packetClassName)) && 
                    "{\"type\":\"handshake\"}".equals(payloadJson)) {
                    System.out.println("Player " + uuid + " is a NRC client.");
                    onHandshakeReceived(uuid);
                } else if ((packetClassName.endsWith("AckPayload") || 
                           "gg.norisk.core.payloads.AckPayload".equals(packetClassName)) && 
                          "{\"type\":\"ack\"}".equals(payloadJson)) {
                    trySendNext(uuid);
                } else if (packetClassName.endsWith("InputbarResponsePayload") || 
                          "gg.norisk.core.payloads.InputbarResponsePayload".equals(packetClassName)) {
                    try {
                        InputbarResponsePayload payload = gson.fromJson(payloadJson, InputbarResponsePayload.class);
                        InputbarPayloadManager.handleInputbarResponse(uuid, payload);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void onHandshakeReceived(UUID uuid) {
        handshakeDone.put(uuid, true);
        waitingForAck.put(uuid, false);
        trySendNext(uuid);
    }
    
    public static void onPlayerLeave(UUID uuid) {
        handshakeDone.remove(uuid);
        queuedPayloads.remove(uuid);
        waitingForAck.remove(uuid);
    }
} 