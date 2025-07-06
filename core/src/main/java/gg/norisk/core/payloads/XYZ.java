package gg.norisk.core.payloads;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data class representing 3D coordinates
 * Creates a new XYZ coordinate
 */
@RequiredArgsConstructor
@Getter
public final class XYZ {
    private final double x;
    private final double y;
    private final double z;
} 