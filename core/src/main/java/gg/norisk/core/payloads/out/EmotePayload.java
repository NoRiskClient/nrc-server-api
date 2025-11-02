package gg.norisk.core.payloads.out;

import gg.norisk.core.annotations.Payload;
import gg.norisk.core.payloads.OutPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
  * Abstract payload for making NPCs perform emotes from the NoRisk Client Emote Catalog
 */

@Getter
@RequiredArgsConstructor
@Payload(type = "emote")
public class EmotePayload implements OutPayload {
    private final UUID npc_uuid;
    private final String emote;
}
