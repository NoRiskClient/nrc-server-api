package gg.norisk.core.payloads.out;

import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.annotations.Payload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract payload for showing input prompts in the NoRisk Client
 */
@Getter
@RequiredArgsConstructor
@Payload(type = "inputbar")
public class InputbarPayload implements OutPayload {
    private final String header;
    private final String placeholder;
    private final int maxLength;
} 