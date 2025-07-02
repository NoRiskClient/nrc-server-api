# NoRisk Client Server API Documentation

The NoRisk Client Server API enables server plugins to communicate with NoRisk Clients and utilize special features.

## Quick Start

### 1. Add Dependency (Maven/Gradle)

```kotlin
// build.gradle.kts
dependencies {
    implementation("gg.norisk:server-api-core:0.1.0")
}
```

### 2. Create API Instance

```kotlin
val api = NoRiskServerApi()
```

### 3. Basic Usage

```kotlin
// Example for Paper/Spigot Plugin
class MyPlugin : JavaPlugin() {
    private val nrApi = NoRiskServerApi()
    
    override fun onEnable() {
        // Plugin ready
    }
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        
        // Show toast notification
        val toast = nrApi.createToastPayload(
            progressBar = false,
            header = "Welcome!",
            content = "You are using NoRisk Client!",
            playerHead = true,
            playerUUID = player.uniqueId,
            toastType = ToastType.SUCCESS
        )
        
        Payloads.send(player.uniqueId, toast) { uuid, data ->
            player.sendPluginMessage(this, NRC_CHANNEL, data)
        }
    }
}
```

## Available Payloads

### 1. Toast Payload
Shows notifications in the NoRisk Client.

### 2. Beacon Beam Payload
Displays colored beacon beams at specific coordinates.

### 3. Module Deactivate Payload
Deactivates specific NoRisk Client modules.

### 4. Gamemode Payload
Overrides the gamemode display in the client.

### 5. Wheel Payload
Adds entries to the NoRisk Client Action Wheel.

### 6. Inputbar Payload
Shows input prompts in the client.

---

Detailed documentation for each payload can be found in the corresponding files in this folder.
