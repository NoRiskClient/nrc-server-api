package gg.norisk.core.payloads.out;

import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.annotations.Payload;
import gg.norisk.core.payloads.models.ToastType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Abstract payload for displaying toast notifications in the NoRisk Client
 */
@Getter
@RequiredArgsConstructor
@Payload(type = "toast")
public class ToastPayload implements OutPayload {
    private final boolean progressBar;
    private final String header;
    private final String content;
    private final boolean playerHead;
    private final UUID playerUUID;
    private final ToastType toastType;
} 