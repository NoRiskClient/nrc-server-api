# Module Deactivate Payload Documentation

## Overview
The Module Deactivate Payload disables specific NoRisk Client modules for individual players. This is useful for servers that want to restrict certain client features.

## Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `modules` | `List<Modules>` | List of modules to deactivate |

## Available Modules
```kotlin
enum class Modules {
    FovChanger,         
    FullBrightModule,   
    ZoomModule,         
    FreeLookModule,     
    NoFogModule,        
    ArrowTrail,         
    PackTweaks,         
    ItemModel,          
    AutoText,           
    ItemHighlighter,    
    TntTimer,           
    WeatherChanger,     
    TimeChanger         
}
```

## Examples

### Disable Single Module
```kotlin
val moduleDeactivate = api.createModuleDeactivatePayload(
    modules = listOf(Modules.ZoomModule)
)

Payloads.send(player.uniqueId, moduleDeactivate) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

### Disable Multiple Modules
```kotlin
val pvpModules = api.createModuleDeactivatePayload(
    modules = listOf(
        Modules.ZoomModule,
        Modules.FreeLookModule,
        Modules.FullBrightModule
    )
)

Payloads.send(player.uniqueId, pvpModules) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

## Practical Applications

### PvP Arena without Advantages
```kotlin
fun enterPvPArena(player: Player) {
    val fairPlay = api.createModuleDeactivatePayload(
        modules = listOf(
            Modules.ZoomModule,
            Modules.FreeLookModule,
            Modules.FullBrightModule,
            Modules.NoFogModule
        )
    )
    
    Payloads.send(player.uniqueId, fairPlay) { uuid, data ->
        player.sendPluginMessage(this, NRC_CHANNEL, data)
    }
}
```

### Minigame Area
```kotlin
fun enterMinigame(player: Player) {
    val minigameRestrictions = api.createModuleDeactivatePayload(
        modules = listOf(
            Modules.TimeChanger,
            Modules.WeatherChanger,
            Modules.AutoText
        )
    )
    
    Payloads.send(player.uniqueId, minigameRestrictions) { uuid, data ->
        player.sendPluginMessage(this, NRC_CHANNEL, data)
    }
}
```

### Event Handler Example
```kotlin
@EventHandler
fun onPlayerChangeWorld(event: PlayerChangedWorldEvent) {
    val player = event.player
    val worldName = player.world.name
    
    if (worldName == "pvp_world") {
        val restrictions = api.createModuleDeactivatePayload(
            modules = listOf(
                Modules.ZoomModule,
                Modules.FullBrightModule,
                Modules.FreeLookModule
            )
        )
        
        Payloads.send(player.uniqueId, restrictions) { uuid, data ->
            player.sendPluginMessage(this, NRC_CHANNEL, data)
        }
    }
}
```

## Notes
- Modules are only deactivated for the duration of the session
- Players can reactivate modules after a restart
- Deactivation only affects NoRisk Client users
