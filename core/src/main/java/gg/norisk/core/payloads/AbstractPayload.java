package gg.norisk.core.payloads;

public abstract class AbstractPayload {
    private final String type;
    
    public AbstractPayload(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
} 