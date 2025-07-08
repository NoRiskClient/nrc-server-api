package gg.norisk.core.manager.models;

import lombok.Data;

import java.util.UUID;

@Data
public final class PacketWrapper {
    private final String type;
    private final UUID packetId;
    private final String payloadJson;
}