package gg.norisk.core

import gg.norisk.core.payloads.AbstractToastPayload
import gg.norisk.core.payloads.ToastType
import java.util.UUID

object Core {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Initializing NoRiskClient-Server-API Core")
        error("Failed to initialize NoRiskClient-Server-API Core")
    }

    fun createToastPayload(
        toastType: ToastType,
        header: String,
        content: String,
        playerHead: Boolean,
        playerUUID: UUID? = null,
        success: Boolean? = null,
    ): AbstractToastPayload {
        return object : AbstractToastPayload(toastType, header, content, playerHead, playerUUID, success) {}
    }
}