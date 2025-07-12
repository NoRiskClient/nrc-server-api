package gg.norisk.fabric.api;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import gg.norisk.core.payloads.Payload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class NrcCustomPayload implements CustomPayload {
    public static final Identifier ID = Identifier.of("norisk", "main");
    public static final Id<NrcCustomPayload> ID_OBJ = new Id<>(ID);
    private static final Gson gson = new Gson();
    
    public static final PacketCodec<PacketByteBuf, NrcCustomPayload> CODEC = PacketCodec.of(
        (payload, buf) -> {
            String jsonString = payload.json != null ? payload.json : gson.toJson(payload.payloadObj);
            byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
            buf.writeBytes(bytes);
        },
        (buf) -> {
            int readableBytes = buf.readableBytes();
            byte[] bytes = new byte[readableBytes];
            buf.readBytes(bytes);
            String json = new String(bytes, StandardCharsets.UTF_8);
            return new NrcCustomPayload(json);
        }
    );
    
    private final byte[] payloadBytes;
    private final Payload payloadObj;
    private final String json;
    
    public NrcCustomPayload(byte[] payloadBytes) {
        this.payloadBytes = payloadBytes;
        this.payloadObj = null;
        this.json = null;
    }
    
    public NrcCustomPayload(Payload payloadObj) {
        this.payloadObj = payloadObj;
        this.payloadBytes = null;
        this.json = gson.toJson(payloadObj);
    }
    
    public NrcCustomPayload(String json) {
        this.json = json;
        this.payloadObj = null;
        this.payloadBytes = null;
    }
    
    @Override
    public Id<NrcCustomPayload> getId() {
        return ID_OBJ;
    }
    
    public byte[] getPayloadBytes() {
        return payloadBytes;
    }
    
    public Payload getPayloadObj() {
        return payloadObj;
    }
    
    public String getJson() {
        return json;
    }
} 