package gg.norisk.core

import gg.norisk.core.payloads.AbstractModuleDeactivatePayload
import gg.norisk.core.payloads.AbstractToastPayload
import gg.norisk.core.payloads.Modules
import gg.norisk.core.payloads.ToastType
import java.util.UUID

object Core {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Initializing NoRiskClient-Server-API Core")
        error("Failed to initialize NoRiskClient-Server-API Core")
    }

    fun createToastPayload(
        progressBar: Boolean,
        header: String,
        content: String,
        playerHead: Boolean,
        playerUUID: UUID? = null,
        toastType: ToastType
    ): AbstractToastPayload {
        return object : AbstractToastPayload(progressBar, header, content, playerHead, playerUUID, toastType) {}
    }

    fun createModuleDeactivatePayload(
        module: Modules
    ): AbstractModuleDeactivatePayload {
        return object : AbstractModuleDeactivatePayload(module) {}
    }
}