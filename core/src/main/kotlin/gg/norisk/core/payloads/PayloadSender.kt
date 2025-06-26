package gg.norisk.core.payloads

import java.util.UUID

object PayloadSender {
    fun send(payload: AbstractPayload, receiver: UUID? = null) {
        println("Sende Payload vom Typ: ${payload.type}")
        ChannelApi.send(payload)
    }
}

