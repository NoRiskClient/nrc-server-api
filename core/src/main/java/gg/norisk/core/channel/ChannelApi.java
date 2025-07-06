package gg.norisk.core.channel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import gg.norisk.core.payloads.AbstractPayload;
import gg.norisk.core.payloads.HandshakePayload;

public class ChannelApi {
    private static final Gson gson = new Gson();
    private static final Map<Class<?>, List<PacketListener<?>>> listeners = new HashMap<>();
    private static final Map<String, Class<?>> allowedPacketClasses = new HashMap<>();
    
    static {
        allowedPacketClasses.put("HandshakePayload", HandshakePayload.class);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void registerListener(Class<T> packetType, PacketListener<T> listener) {
        System.out.println("Registering listener for packet type " + packetType.getSimpleName());
        listeners.computeIfAbsent(packetType, k -> new ArrayList<>()).add(listener);
    }
    
    public static byte[] send(Object packet) {
        String type;
        if (packet instanceof AbstractPayload) {
            type = ((AbstractPayload) packet).getType();
        } else {
            type = packet.getClass().getSimpleName();
        }
        
        String payloadJson = gson.toJson(packet);
        PacketWrapper wrapper = new PacketWrapper(packet.getClass().getName(), payloadJson);
        String wrapperJson = gson.toJson(wrapper);
        return wrapperJson.getBytes(StandardCharsets.UTF_8);
    }
    
    @SuppressWarnings("unchecked")
    public static void receive(UUID sender, byte[] message) {
        String rawString = new String(message, StandardCharsets.UTF_8);
        int jsonStart = rawString.indexOf('{');
        if (jsonStart == -1) {
            return;
        }
        
        String wrapperJson = rawString.substring(jsonStart);
        try {
            PacketWrapper wrapper = gson.fromJson(wrapperJson, PacketWrapper.class);
            String simpleName = wrapper.getPacketClassName().substring(wrapper.getPacketClassName().lastIndexOf('.') + 1);
            Class<?> mappedClass = allowedPacketClasses.get(simpleName);
            
            if (mappedClass == null) {
                return;
            }
            
            List<PacketListener<?>> classListeners = listeners.get(mappedClass);
            if (classListeners == null) {
                System.out.println("No listeners found for packet type " + mappedClass.getSimpleName());
                return;
            }
            
            Object packetPayload = gson.fromJson(wrapper.getPayloadJson(), mappedClass);
            for (PacketListener<?> listener : classListeners) {
                ((PacketListener<Object>) listener).onMessage(sender, packetPayload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 