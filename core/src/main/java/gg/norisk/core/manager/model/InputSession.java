package gg.norisk.core.manager.model;

import gg.norisk.core.payloads.AbstractInputbarPayload;
import lombok.Data;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Data
public class InputSession {
    private final AbstractInputbarPayload payload;
    private final BiConsumer<UUID, byte[]> sendToClient;
    private final Consumer<String> handler;
    private final Runnable onCancel;
}