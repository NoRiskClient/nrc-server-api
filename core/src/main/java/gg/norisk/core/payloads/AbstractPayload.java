package gg.norisk.core.payloads;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPayload {
    private final String type;
}