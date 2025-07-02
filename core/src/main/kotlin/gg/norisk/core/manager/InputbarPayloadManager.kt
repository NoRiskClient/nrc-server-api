package gg.norisk.core.manager

import gg.norisk.core.payloads.InputbarResponsePayload
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InputbarPayloadManager {
    companion object {
        private val inputHandlers = ConcurrentHashMap<UUID, (String) -> Unit>()

        fun registerInputHandler(playerUuid: UUID, handler: (String) -> Unit) {
            inputHandlers[playerUuid] = handler
        }

        fun unregisterInputHandler(playerUuid: UUID) {
            inputHandlers.remove(playerUuid)
        }

        fun handleInputbarResponse(playerUuid: UUID, payload: InputbarResponsePayload) {
            val handler = inputHandlers[playerUuid]
            if (handler != null) {
                handler(payload.input)
                unregisterInputHandler(playerUuid)
            } else {
                println("[DEBUG] No input handler registered for player $playerUuid")
            }
        }
    }
}