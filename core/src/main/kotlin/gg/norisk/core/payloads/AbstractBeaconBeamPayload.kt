package gg.norisk.core.payloads

import java.util.UUID

/**
 * Enum representing different dimensions in Minecraft
 */
enum class Dimension {
    OVERWORLD,
    NETHER,
    END
}

/**
 * Data class representing 3D coordinates
 *
 * @param x The X coordinate
 * @param y The Y coordinate
 * @param z The Z coordinate
 */
data class XYZ(val x: Double, val y: Double, val z: Double)

/**
 * Data class representing RGB color values
 *
 * @param r The red component (0-255)
 * @param g The green component (0-255)
 * @param b The blue component (0-255)
 */
data class RGBColor(val r: Int, val g: Int, val b: Int)

/**
 * Abstract payload for creating beacon beams at specific coordinates
 *
 * @param xyz The coordinates where the beacon beam should appear
 * @param dimension The dimension where the beacon beam should appear
 * @param color The RGB color of the beacon beam
 */
abstract class AbstractBeaconBeamPayload(
    val xyz: XYZ,
    val dimension: Dimension,
    val color: RGBColor
) : AbstractPayload("beacon_beam") {
}