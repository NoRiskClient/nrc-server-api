package gg.norisk.core.manager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HandshakeManager {
    private final Consumer<UUID> onNrcClient;
    private final Consumer<UUID> onNonNrcClient;
    private final long handshakeTimeoutSeconds;
    private final Map<UUID, Timer> pendingHandshakes = new ConcurrentHashMap<>();
    private final Set<UUID> nrcPlayers = Collections.synchronizedSet(new HashSet<>());
    
    public HandshakeManager(Consumer<UUID> onNrcClient, Consumer<UUID> onNonNrcClient, long handshakeTimeoutSeconds) {
        this.onNrcClient = onNrcClient;
        this.onNonNrcClient = onNonNrcClient;
        this.handshakeTimeoutSeconds = handshakeTimeoutSeconds;
    }
    
    public HandshakeManager(Consumer<UUID> onNrcClient, Consumer<UUID> onNonNrcClient) {
        this(onNrcClient, onNonNrcClient, 20L);
    }
    
    public void onPlayerJoin(UUID player, Runnable sendHandshake) {
        sendHandshake.run();
        Timer existingTimer = pendingHandshakes.remove(player);
        if (existingTimer != null) {
            existingTimer.cancel();
        }
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleHandshakeTimeout(player);
            }
        }, TimeUnit.SECONDS.toMillis(handshakeTimeoutSeconds));
        pendingHandshakes.put(player, timer);
    }
    
    public void onClientHandshake(UUID player) {
        Timer timer = pendingHandshakes.remove(player);
        if (timer != null) {
            timer.cancel();
        }
        nrcPlayers.add(player);
        onNrcClient.accept(player);
    }
    
    public boolean isNrcPlayer(UUID player) {
        return nrcPlayers.contains(player);
    }
    
    public void onPlayerLeave(UUID player) {
        Timer timer = pendingHandshakes.remove(player);
        if (timer != null) {
            timer.cancel();
        }
        nrcPlayers.remove(player);
    }
    
    private void handleHandshakeTimeout(UUID player) {
        pendingHandshakes.remove(player);
        onNonNrcClient.accept(player);
    }
    
    // Getter methods for reflection access (used by NRCPlayer)
    public Set<UUID> getNrcPlayers() {
        return Collections.unmodifiableSet(nrcPlayers);
    }
    
    public Map<UUID, Timer> getPendingHandshakes() {
        return Collections.unmodifiableMap(pendingHandshakes);
    }
} 