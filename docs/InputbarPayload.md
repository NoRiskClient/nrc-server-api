# Inputbar Payload Documentation

## Overview
The Inputbar Payload displays input prompts in the NoRisk Client. Players can enter text which is then sent back to the server.

## Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `header` | `String` | Header text for the input prompt |
| `placeholder` | `String?` | Placeholder text shown in the input field (optional) |
| `maxLength` | `Int` | Maximum number of characters allowed in the input |

## Examples

### Simple Text Input
```kotlin
val inputPayload = api.createInputbarPayload(
    input = "Enter Name",
    placeholder = "Your name...",
    maxLength = 50
)

api.requestInput(player.uniqueId, inputPayload, 
    { uuid, data -> player.sendPluginMessage(this, NRC_CHANNEL, data) },
    { response -> 
        player.sendMessage("You entered: $response")
    }
)
```

### With Cancel Handler
```kotlin
val inputPayload = api.createInputbarPayload(
    input = "Enter Amount",
    placeholder = "Number of coins...",
    maxLength = 10
)

api.requestInput(player.uniqueId, inputPayload,
    { uuid, data -> player.sendPluginMessage(this, NRC_CHANNEL, data) },
    { response -> 
        val amount = response.toIntOrNull()
        if (amount != null) {
            transferCoins(player, amount)
        } else {
            player.sendMessage("Invalid input!")
        }
    },
    { 
        player.sendMessage("Input canceled.")
    }
)
```

## Practical Applications

### Shop System
```kotlin
fun buyItem(player: Player, item: String, price: Int) {
    val confirmPayload = api.createInputbarPayload(
        input = "Confirm Purchase",
        placeholder = "Type 'yes' to confirm",
        maxLength = 10
    )
    
    api.requestInput(player.uniqueId, confirmPayload,
        { uuid, data -> player.sendPluginMessage(this, NRC_CHANNEL, data) },
        { response ->
            if (response.lowercase() == "yes") {
                if (hasEnoughMoney(player, price)) {
                    giveItem(player, item)
                    takeMoney(player, price)
                    player.sendMessage("§a$item purchased!")
                } else {
                    player.sendMessage("§cNot enough money!")
                }
            }
        },
        {
            player.sendMessage("§7Purchase canceled.")
        }
    )
}
```

### Custom Commands
```kotlin
@EventHandler
fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
    if (event.message == "/customname") {
        event.isCancelled = true
        
        val inputPayload = api.createInputbarPayload(
            input = "Enter New Name",
            placeholder = "Your new name...",
            maxLength = 16
        )
        
        api.requestInput(event.player.uniqueId, inputPayload,
            { uuid, data -> event.player.sendPluginMessage(this, NRC_CHANNEL, data) },
            { response ->
                if (isValidName(response)) {
                    setCustomName(event.player, response)
                    event.player.sendMessage("§aYour name has been changed!")
                } else {
                    event.player.sendMessage("§cInvalid name!")
                }
            }
        )
    }
}
```

## Notes
- Input is automatically canceled after a certain time
- Players can cancel input with ESC
- Empty inputs are treated as cancellation
