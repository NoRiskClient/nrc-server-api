package gg.norisk.core.payloads.in;

import gg.norisk.core.annotations.Payload;
import gg.norisk.core.payloads.InPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Payload class for handling user input responses from the NoRisk Client
 * This payload is sent by the client when a user responds to an input prompt
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Payload(type = "inputbar_response")
public class InputbarResponsePayload implements InPayload {
    private String input;
    private boolean canceled;
}
