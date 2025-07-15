package gg.norisk.core.payloads.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data class representing RGB color values
 */
@Getter
@RequiredArgsConstructor
public class RGBColor {
    private final int r;
    private final int g;
    private final int b;
} 