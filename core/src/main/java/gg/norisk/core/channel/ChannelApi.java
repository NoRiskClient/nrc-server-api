package gg.norisk.core.channel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import gg.norisk.core.payloads.HandshakePayload;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelApi {
    private final Gson gson = new Gson();
    private final Map<Class<?>, List<PacketListener<?>>> listeners = new HashMap<>();
    private final Map<String, Class<?>> allowedPacketClasses = new HashMap<>();
    
    {
        allowedPacketClasses.put("HandshakePayload", HandshakePayload.class);
    }
    
    public <T> void registerListener(Class<T> packetType, PacketListener<T> listener) {
        log.info("Registering listener for packet type {}", packetType.getSimpleName());
        listeners.computeIfAbsent(packetType, k -> new ArrayList<>()).add(listener);
    }
    
    public byte[] send(Object packet) {
        String payloadJson = gson.toJson(packet);
        PacketWrapper wrapper = new PacketWrapper(packet.getClass().getName(), payloadJson);
        String wrapperJson = gson.toJson(wrapper);
        return wrapperJson.getBytes(StandardCharsets.UTF_8);
    }
    
    public void receive(UUID sender, byte[] message) {
        String rawString = new String(message, StandardCharsets.UTF_8);
        int jsonStart = rawString.indexOf('{');
        if (jsonStart == -1) {
            return;
        }
        
        String wrapperJson = rawString.substring(jsonStart);
        try {
            PacketWrapper wrapper = gson.fromJson(wrapperJson, PacketWrapper.class);
            String simpleName = wrapper.packetClassName().substring(wrapper.packetClassName().lastIndexOf('.') + 1);
            Class<?> mappedClass = allowedPacketClasses.get(simpleName);
            
            if (mappedClass == null) {
                return;
            }
            
            List<PacketListener<?>> classListeners = listeners.get(mappedClass);
            if (classListeners == null) {
                log.info("No listeners found for packet type {}", mappedClass.getSimpleName());
                return;
            }
            
            Object packetPayload = gson.fromJson(wrapper.payloadJson(), mappedClass);
            for (PacketListener<?> listener : classListeners) {
                ((PacketListener<Object>) listener).onMessage(sender, packetPayload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 