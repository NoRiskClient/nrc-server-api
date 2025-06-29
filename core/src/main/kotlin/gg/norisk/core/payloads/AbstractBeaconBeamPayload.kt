package gg.norisk.core.payloads

import java.util.UUID

enum class Dimension {
    OVERWORLD,
    NETHER,
    END
}

data class XYZ(val x: Double, val y: Double, val z: Double)
data class RGBColor(val r: Int, val g: Int, val b: Int)

abstract class AbstractBeaconBeamPayload(
    val xyz: XYZ,
    val dimension: Dimension,
    val color: RGBColor
) : AbstractPayload("beacon_beam") {
}