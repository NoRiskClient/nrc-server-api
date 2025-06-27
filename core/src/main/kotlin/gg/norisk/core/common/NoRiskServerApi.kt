package gg.norisk.core.common

import gg.norisk.core.Core
import gg.norisk.core.payloads.AbstractModuleDeactivatePayload
import gg.norisk.core.payloads.AbstractToastPayload
import gg.norisk.core.payloads.Modules
import gg.norisk.core.payloads.ToastType
import java.util.UUID

object NoRiskServerApi {
    @JvmStatic
    fun createToastPayload(
        progressBar: Boolean,
        header: String,
        content: String,
        playerHead: Boolean,
        playerUUID: UUID? = null,
        toastType: ToastType
    ): AbstractToastPayload {
        return Core.createToastPayload(progressBar, header, content, playerHead, playerUUID, toastType)
    }

    @JvmStatic
    fun createModuleDeactivatePayload(
        module: Modules
    ): AbstractModuleDeactivatePayload {
        return Core.createModuleDeactivatePayload(module)
    }
}