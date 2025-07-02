# Toast Payload Documentation

## Übersicht
Das Toast Payload zeigt Benachrichtigungen im NoRisk Client an. Diese erscheinen als kleine Pop-up-Fenster mit verschiedenen Stilen.

## Parameter

| Parameter | Typ | Beschreibung |
|-----------|-----|--------------|
| `progressBar` | `Boolean` | Zeigt eine Fortschrittsleiste an |
| `header` | `String` | Überschrift der Benachrichtigung |
| `content` | `String` | Haupttext der Benachrichtigung |
| `playerHead` | `Boolean` | Zeigt den Spieler-Avatar an |
| `playerUUID` | `UUID?` | UUID des Spielers für den Avatar (optional) |
| `toastType` | `ToastType` | Art der Benachrichtigung (INFO, SUCCESS, ERROR) |

## Beispiele

### Einfache Benachrichtigung
```kotlin
val toast = api.createToastPayload(
    progressBar = false,
    header = "Willkommen!",
    content = "Du bist auf unserem Server!",
    playerHead = false,
    toastType = ToastType.INFO
)

Payloads.send(player.uniqueId, toast) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

### Erfolgs-Benachrichtigung mit Spieler-Avatar
```kotlin
val successToast = api.createToastPayload(
    progressBar = false,
    header = "Mission erfolgreich!",
    content = "Du hast 100 Coins erhalten!",
    playerHead = true,
    playerUUID = player.uniqueId,
    toastType = ToastType.SUCCESS
)

Payloads.send(player.uniqueId, successToast) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

### Fehler-Benachrichtigung mit Fortschrittsleiste
```kotlin
val errorToast = api.createToastPayload(
    progressBar = true,
    header = "Fehler!",
    content = "Nicht genügend Berechtigung!",
    playerHead = false,
    toastType = ToastType.ERROR
)

Payloads.send(player.uniqueId, errorToast) { uuid, data ->
    player.sendPluginMessage(this, NRC_CHANNEL, data)
}
```

## ToastType Enum

- `INFO` - Normale Informationsbenachrichtigung (blau)
- `SUCCESS` - Erfolgsbenachrichtigung (grün)
- `ERROR` - Fehlerbenachrichtigung (rot)

## Verwendung in verschiedenen Plattformen

### Paper/Spigot
```kotlin
class MyPlugin : JavaPlugin() {
    private val nrApi = NoRiskServerApi()
    
    fun showToast(player: Player, message: String) {
        val toast = nrApi.createToastPayload(
            progressBar = false,
            header = "Server",
            content = message,
            playerHead = true,
            playerUUID = player.uniqueId,
            toastType = ToastType.INFO
        )
        
        Payloads.send(player.uniqueId, toast) { uuid, data ->
            player.sendPluginMessage(this, NRC_CHANNEL, data)
        }
    }
}
```

### Fabric
```kotlin
class MyMod : DedicatedServerModInitializer {
    private val nrApi = NoRiskServerApi()
    
    fun showToast(player: ServerPlayerEntity, message: String) {
        val toast = nrApi.createToastPayload(
            progressBar = false,
            header = "Server",
            content = message,
            playerHead = true,
            playerUUID = player.uuid,
            toastType = ToastType.INFO
        )
        
        Payloads.send(player.uuid, toast) { uuid, data ->
            ServerPlayNetworking.send(player, NrcCustomPayload(data))
        }
    }
}
```
