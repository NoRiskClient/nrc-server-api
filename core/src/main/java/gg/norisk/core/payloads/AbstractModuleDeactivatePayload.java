package gg.norisk.core.payloads;

import java.util.List;

/**
 * Abstract payload for deactivating specific NoRisk Client modules
 */
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
    
    public List<Modules> getModules() {
        return modules;
    }
} 