package gg.norisk.core.channel;

public class PacketWrapper {
    private final String packetClassName;
    private final String payloadJson;
    
    public PacketWrapper(String packetClassName, String payloadJson) {
        this.packetClassName = packetClassName;
        this.payloadJson = payloadJson;
    }
    
    public String getPacketClassName() {
        return packetClassName;
    }
    
    public String getPayloadJson() {
        return payloadJson;
    }
} 