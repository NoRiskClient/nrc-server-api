package gg.norisk.core.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.norisk.core.manager.models.PacketWrapper;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.payloads.in.AckPayload;
import gg.norisk.core.payloads.in.HandshakePayload;
import gg.norisk.core.payloads.in.InputbarResponsePayload;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketManager {

    private static final Gson gson = new Gson();
    private static final Map<String, Class<? extends InPayload>> inPayloadRegistry = new HashMap<>();

    static {
        //TODO: Replace this with the annotations
        // Register all InPayload types
        registerInPayload("ack", AckPayload.class);
        registerInPayload("inputbar_response", InputbarResponsePayload.class);
        registerInPayload("handshake", HandshakePayload.class);
    }

    private static void registerInPayload(String type, Class<? extends InPayload> clazz) {
        inPayloadRegistry.put(type, clazz);
    }

    public PacketWrapper serializeOutPayload(OutPayload payload) {
        return serializeOutPayload(payload, UUID.randomUUID());
    }

    public PacketWrapper serializeOutPayload(OutPayload payload, UUID packetId) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }

        String type = payload.getType();
        String payloadJson = gson.toJson(payload);

        return new PacketWrapper(type, packetId, payloadJson);
    }

    public InPayload deserializeInPayload(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            if (!jsonObject.has("type")) {
                throw new IllegalArgumentException("JSON must contain a 'type' field");
            }

            String type = jsonObject.get("type").getAsString();
            Class<? extends InPayload> payloadClass = inPayloadRegistry.get(type);

            if (payloadClass == null) {
                throw new IllegalArgumentException("Unknown payload type: " + type);
            }

            return gson.fromJson(json, payloadClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize InPayload from JSON: " + json, e);
        }
    }

    public PacketWrapper deserializePacketWrapper(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Byte array cannot be null or empty");
        }

        try {
            String json = new String(bytes, StandardCharsets.UTF_8);
            return gson.fromJson(json, PacketWrapper.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize PacketWrapper from byte array", e);
        }
    }

    public String serializePacketWrapper(PacketWrapper wrapper) {
        if (wrapper == null) {
            throw new IllegalArgumentException("PacketWrapper cannot be null");
        }

        return gson.toJson(wrapper);
    }
}
