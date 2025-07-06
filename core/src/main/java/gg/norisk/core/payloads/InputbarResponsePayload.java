package gg.norisk.core.payloads;

import lombok.Getter;

/**
 * Payload class for handling user input responses from the NoRisk Client
 * This payload is sent by the client when a user responds to an input prompt
 */
@Getter
public class InputbarResponsePayload extends AbstractPayload {
    private final String input;
    private final boolean canceled;
    
    /**
     * Creates a new inputbar response payload
     * @param input The text input provided by the user (null if canceled)
     * @param canceled Whether the input was canceled by the user
     */
    public InputbarResponsePayload(String input, boolean canceled) {
        super("inputbar_response");
        this.input = input;
        this.canceled = canceled;
    }
    
    /**
     * Default constructor for JSON deserialization
     */
    public InputbarResponsePayload() {
        super("inputbar_response");
        this.input = null;
        this.canceled = false;
    }

} 