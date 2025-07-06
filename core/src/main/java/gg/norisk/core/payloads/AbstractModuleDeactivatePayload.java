package gg.norisk.core.payloads;

import lombok.Getter;

import java.util.List;

/**
 * Abstract payload for deactivating specific NoRisk Client modules
 */
@Getter
public abstract class AbstractModuleDeactivatePayload extends AbstractPayload {
    private final List<Modules> modules;
    
    /**
     * Creates a new module deactivate payload
     * @param modules List of modules to deactivate on the client
     */
    public AbstractModuleDeactivatePayload(List<Modules> modules) {
        super("module_deactivate");
        this.modules = modules;
    }

} 