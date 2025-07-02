package gg.norisk.core.manager

import gg.norisk.core.payloads.AbstractInputbarPayload
import gg.norisk.core.payloads.InputbarResponsePayload
import gg.norisk.core.payloads.Payloads
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InputbarPayloadManager {
    companion object {
        private data class InputSession(
            val payload: AbstractInputbarPayload,
            val sendToClient: (UUID, ByteArray) -> Unit,
            val handler: (String) -> Unit,
            val onCancel: (() -> Unit)? = null
        )

        private val sessions = ConcurrentHashMap<UUID, InputSession>()

        fun registerInputSession(
            playerUuid: UUID,
            payload: AbstractInputbarPayload,
            sendToClient: (UUID, ByteArray) -> Unit,
            handler: (String) -> Unit,
            onCancel: (() -> Unit)? = null
        ) {
            sessions[playerUuid] = InputSession(payload, sendToClient, handler, onCancel)
            Payloads.send(playerUuid, payload, sendToClient)
        }

        fun unregisterSession(playerUuid: UUID) {
            sessions.remove(playerUuid)
        }

        fun handleInputbarResponse(playerUuid: UUID, payload: InputbarResponsePayload) {
            val session = sessions[playerUuid] ?: run {
                return
            }

            if (payload.canceled) {
                session.onCancel?.invoke()
            } else if (payload.input != null) {
                session.handler(payload.input)
                unregisterSession(playerUuid)
            }
        }

        fun resendInputbar(playerUuid: UUID) {
            val session = sessions[playerUuid] ?: return
            Payloads.send(playerUuid, session.payload, session.sendToClient)
        }
    }
}