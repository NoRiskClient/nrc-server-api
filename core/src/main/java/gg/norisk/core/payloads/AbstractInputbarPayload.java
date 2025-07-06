package gg.norisk.core.payloads;

/**
 * Abstract payload for showing input prompts in the NoRisk Client
 */
public abstract class AbstractInputbarPayload extends AbstractPayload {
    private final String header;
    private final String placeholder;
    private final int maxLength;
    
    /**
     * Creates a new inputbar payload
     * @param header The header text for the input prompt
     * @param placeholder The placeholder text shown in the input field (optional)
     * @param maxLength The maximum number of characters allowed in the input
     */
    public AbstractInputbarPayload(String header, String placeholder, int maxLength) {
        super("inputbar");
        this.header = header;
        this.placeholder = placeholder;
        this.maxLength = maxLength;
    }
    
    public String getHeader() {
        return header;
    }
    
    public String getPlaceholder() {
        return placeholder;
    }
    
    public int getMaxLength() {
        return maxLength;
    }
} 