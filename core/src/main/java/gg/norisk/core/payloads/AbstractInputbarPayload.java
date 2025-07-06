package gg.norisk.core.payloads;

import lombok.Getter;

/**
 * Abstract payload for showing input prompts in the NoRisk Client
 */
@Getter
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

} 