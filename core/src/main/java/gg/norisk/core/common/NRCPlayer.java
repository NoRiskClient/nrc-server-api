package gg.norisk.core.common;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import gg.norisk.core.channel.ChannelApi;
import gg.norisk.core.manager.HandshakeManager;

public class NRCPlayer {
    
    private static HandshakeManager getHandshakeManager() {
        try {
            Field field = ChannelApi.class.getDeclaredField("handshakeManager");
            field.setAccessible(true);
            return (HandshakeManager) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access handshakeManager", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Set<UUID> listAll() {
        try {
            HandshakeManager manager = getHandshakeManager();
            Field field = manager.getClass().getDeclaredField("nrcPlayers");
            field.setAccessible(true);
            return (Set<UUID>) field.get(manager);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access nrcPlayers", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Set<UUID> listAllNoNRC() {
        try {
            HandshakeManager manager = getHandshakeManager();
            Field field = manager.getClass().getDeclaredField("pendingHandshakes");
            field.setAccessible(true);
            Map<UUID, ?> all = (Map<UUID, ?>) field.get(manager);
            return all.keySet();
        } catch (Exception e) {
            throw new RuntimeException("Failed to access pendingHandshakes", e);
        }
    }
    
    public static boolean user(UUID uuid) {
        return listAll().contains(uuid);
    }
} 