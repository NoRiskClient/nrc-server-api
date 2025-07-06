package gg.norisk.core.payloads;

import lombok.Getter;

/**
 * Abstract payload for overriding the gamemode display in the NoRisk Client
 */
@Getter
public abstract class AbstractGamemodePayload extends AbstractPayload {
    private final String gamemode;
    
    /**
     * Creates a new gamemode payload
     * @param gamemode The custom gamemode name to display in the client
     */
    public AbstractGamemodePayload(String gamemode) {
        super("gamemode");
        this.gamemode = gamemode;
    }

} 