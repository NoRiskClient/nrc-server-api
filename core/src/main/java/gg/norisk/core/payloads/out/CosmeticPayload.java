package gg.norisk.core.payloads.out;

import gg.norisk.core.annotations.Payload;
import gg.norisk.core.payloads.OutPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Abstract payload for equipping NPCs with cosmetics from the NoRisk Client Cosmetic Catalog
 */

@Getter
@RequiredArgsConstructor
@Payload(type = "cosmetic")
public class CosmeticPayload implements OutPayload {
    private final UUID npc_uuid;
    private final String cosmetic;
}
