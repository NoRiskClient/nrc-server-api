package gg.norisk.core.payloads.out;

import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.annotations.Payload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract payload for overriding the gamemode display in the NoRisk Client
 */
@Getter
@RequiredArgsConstructor
@Payload(type = "gamemode")
public class GamemodePayload implements OutPayload {
    private final String gamemode;
} 