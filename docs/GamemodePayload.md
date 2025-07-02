# Gamemode Payload Documentation

## Overview
The Gamemode Payload overrides the gamemode display in the NoRisk Client. This allows displaying custom gamemode names that differ from standard Minecraft gamemodes.

## Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `gamemode` | `String` | The custom gamemode name to display |

## Examples

### Custom Gamemode
```kotlin
val customGamemode = api.createGamemodePayload(
    gamemode = "Skyblock"
)

Payloads.send(player.uniqueId, customGamemode) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

### Server-specific Gamemodes
```kotlin
val miniGameMode = api.createGamemodePayload(
    gamemode = "BedWars"
)

Payloads.send(player.uniqueId, miniGameMode) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

## Practical Applications

### Dynamic Gamemode Display Based on Area
```kotlin
@EventHandler
fun onPlayerMove(event: PlayerMoveEvent) {
    val player = event.player
    val location = player.location
    
    val gamemodeName = when {
        isInPvPZone(location) -> "PvP Zone"
        isInSafeZone(location) -> "Safe Zone"
        isInShop(location) -> "Shop"
        else -> "Survival"
    }
    
    val gamemodePayload = api.createGamemodePayload(gamemode = gamemodeName)
    
    Payloads.send(player.uniqueId, gamemodePayload) { uuid, data ->
        player.sendPluginMessage(this, NRC_CHANNEL, data)
    }
}
```

### Event-based Gamemode Change
```kotlin
fun startEvent(players: List<Player>, eventName: String) {
    val eventGamemode = api.createGamemodePayload(gamemode = eventName)
    
    players.forEach { player ->
        Payloads.send(player.uniqueId, eventGamemode) { uuid, data ->
            player.sendPluginMessage(this, NRC_CHANNEL, data)
        }
    }
}
```

## Notes
- The gamemode display is only overridden in the NoRisk Client
- Standard Minecraft clients will still see normal gamemode names
- Empty strings are ignored
