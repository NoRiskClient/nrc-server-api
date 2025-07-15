package gg.norisk.core.payloads.out;

import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.payloads.models.Modules;
import gg.norisk.core.annotations.Payload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Abstract payload for deactivating specific NoRisk Client modules
 */
@Getter
@RequiredArgsConstructor
@Payload(type = "module_deactivate")
public class ModuleDeactivatePayload implements OutPayload {
    private final List<Modules> modules;
}