package gg.norisk.core.manager.models;

import java.util.UUID;

public record PacketWrapper(String type, UUID packetId, String payloadJson) {
}