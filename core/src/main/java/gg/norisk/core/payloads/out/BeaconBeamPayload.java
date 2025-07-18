package gg.norisk.core.payloads.out;

import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.annotations.Payload;
import gg.norisk.core.payloads.models.Dimension;
import gg.norisk.core.payloads.models.RGBColor;
import gg.norisk.core.payloads.models.XYZ;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract payload for creating beacon beams at specific coordinates
 */
@Deprecated
@Getter
@RequiredArgsConstructor
@Payload(type = "beacon_beam")
public class BeaconBeamPayload implements OutPayload {
    private final XYZ xyz;
    private final Dimension dimension;
    private final RGBColor color;
} 