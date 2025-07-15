package gg.norisk.core.payloads.out;

import gg.norisk.core.models.NrcPlayer;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.payloads.in.InputbarResponsePayload;
import gg.norisk.core.annotations.Payload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

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
    
    /**
     * Sends this inputbar as a request to the specified player with a callback
     * @param player The NrcPlayer to send the request to
     * @param callback The callback to handle the response
     */
    public void sendRequest(NrcPlayer player, Consumer<InputbarResponsePayload> callback) {
        player.sendRequest("norisk:main", this, callback);
    }
} 