package gg.norisk.core.payloads.out;

import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.annotations.Payload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract payload for adding entries to the NoRisk Client action wheel
 */
@Getter
@RequiredArgsConstructor
@Payload(type = "wheel")
public class WheelPayload implements OutPayload {
    private final String name;
    private final String command;
} 