# Beacon Beam Payload Documentation

## Overview
The Beacon Beam Payload displays colored light beams at specific coordinates in the NoRisk Client. These are visible to all players and can be used to mark important locations.

## Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `xyz` | `XYZ` | Coordinates (x, y, z) of the beacon beam |
| `dimension` | `Dimension` | Dimension (OVERWORLD, NETHER, END) |
| `color` | `RGBColor` | RGB color of the beam |

## Data Types
```kotlin
data class XYZ(val x: Double, val y: Double, val z: Double)
data class RGBColor(val r: Int, val g: Int, val b: Int)

enum class Dimension {
    OVERWORLD,
    NETHER,
    END
}
```

## Examples

### Red Beacon Beam in Overworld
```kotlin
val beaconBeam = api.createBeaconBeamPayload(
    xyz = XYZ(100.0, 64.0, 200.0),
    dimension = Dimension.OVERWORLD,
    color = RGBColor(255, 0, 0) // Red
)

Payloads.send(player.uniqueId, beaconBeam) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

### Green Beacon Beam in Nether
```kotlin
val netherBeacon = api.createBeaconBeamPayload(
    xyz = XYZ(-50.0, 100.0, 75.0),
    dimension = Dimension.NETHER,
    color = RGBColor(0, 255, 0) // Green
)

Payloads.send(player.uniqueId, netherBeacon) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

### Blue Beacon Beam in End
```kotlin
val endBeacon = api.createBeaconBeamPayload(
    xyz = XYZ(0.0, 60.0, 0.0),
    dimension = Dimension.END,
    color = RGBColor(0, 100, 255) // Blue
)

Payloads.send(player.uniqueId, endBeacon) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

## Practical Applications

### Shop Marking
```kotlin
fun markShop(player: Player, shopLocation: Location) {
    val shopBeacon = api.createBeaconBeamPayload(
        xyz = XYZ(shopLocation.x, shopLocation.y, shopLocation.z),
        dimension = Dimension.OVERWORLD,
        color = RGBColor(255, 215, 0) // Gold
    )
    
    Payloads.send(player.uniqueId, shopBeacon) { uuid, data ->
        player.sendPluginMessage(this, NRC_CHANNEL, data)
    }
}
```

### Event Area Marking
```kotlin
fun markEventArea(player: Player, center: Location) {
    val eventBeacon = api.createBeaconBeamPayload(
        xyz = XYZ(center.x, center.y, center.z),
        dimension = Dimension.OVERWORLD,
        color = RGBColor(255, 0, 255) // Magenta
    )
    
    Payloads.send(player.uniqueId, eventBeacon) { uuid, data ->
        player.sendPluginMessage(this, NRC_CHANNEL, data)
    }
}
```

## Notes
- Beacon beams are visible to all players with NoRisk Client
- Beams automatically disappear when the player leaves the game
- RGB values should be between 0 and 255
