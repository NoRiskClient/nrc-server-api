package gg.norisk.core.channel;

import java.util.UUID;

public interface PacketListener<T> {
    void onMessage(UUID sender, T packet);
} 