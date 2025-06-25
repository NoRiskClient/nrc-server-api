package gg.norisk.paper.api

import gg.norisk.core.Core
import gg.norisk.core.payloads.AbstractToastPayload
import gg.norisk.core.payloads.ToastType
import java.util.UUID

object NoRiskApi {
    @JvmStatic
    fun createToastPayload(
        toastType: ToastType,
        playerHead: Boolean,
        playerUUID: UUID? = null,
        success: Boolean? = null,
        color: String? = null
    ): AbstractToastPayload {
        return Core.createToastPayload(toastType, playerHead, playerUUID, success, color)
    }
}