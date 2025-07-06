package gg.norisk.fabric.api;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import gg.norisk.core.payloads.AbstractPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class NrcCustomPayload implements CustomPayload {
    public static final Identifier ID = Identifier.of("norisk", "main");
    public static final Id<NrcCustomPayload> ID_OBJ = new Id<>(ID);
    private static final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger("ClientChannelApi");
    
    public static final PacketCodec<PacketByteBuf, NrcCustomPayload> CODEC = PacketCodec.of(
        (payload, buf) -> {
            String jsonString = payload.json != null ? payload.json : gson.toJson(payload.payloadObj);
            buf.writeString(jsonString);
        },
        buf -> {
            int readerIndex = buf.readerIndex();
            try {
                String json = buf.readString(buf.readableBytes());
                return new NrcCustomPayload(json);
            } catch (Exception e) {
                buf.readerIndex(readerIndex);
                int remaining = buf.readableBytes();
                byte[] bytes = new byte[remaining];
                buf.readBytes(bytes);
                String json = new String(bytes, StandardCharsets.UTF_8);
                return new NrcCustomPayload(json);
            }
        }
    );
    
    private final byte[] payloadBytes;
    private final AbstractPayload payloadObj;
    private final String json;
    
    public NrcCustomPayload(byte[] payloadBytes) {
        this.payloadBytes = payloadBytes;
        this.payloadObj = null;
        this.json = null;
    }
    
    public NrcCustomPayload(AbstractPayload payloadObj) {
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
    
    public void write(PacketByteBuf buf) {
        buf.writeString(json != null ? json : gson.toJson(payloadObj));
    }
    
    public static NrcCustomPayload read(PacketByteBuf buf) {
        String json = buf.readString();
        return new NrcCustomPayload(json);
    }
    
    public byte[] getPayloadBytes() {
        return payloadBytes;
    }
    
    public AbstractPayload getPayloadObj() {
        return payloadObj;
    }
    
    public String getJson() {
        return json;
    }
} 