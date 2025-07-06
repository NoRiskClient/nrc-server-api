package gg.norisk.core.payloads;

import lombok.Getter;

/**
 * Abstract payload for adding entries to the NoRisk Client action wheel
 */
@Getter
public abstract class AbstractWheelPayload extends AbstractPayload {
    private final String name;
    private final String command;
    
    /**
     * Creates a new wheel payload
     * @param name The display name of the action wheel entry
     * @param command The command to execute when the entry is selected
     */
    public AbstractWheelPayload(String name, String command) {
        super("wheel");
        this.name = name;
        this.command = command;
    }

} 