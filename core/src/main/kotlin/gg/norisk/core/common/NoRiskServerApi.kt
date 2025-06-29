package gg.norisk.core.common

import gg.norisk.core.Core
import gg.norisk.core.payloads.AbstractBeaconBeamPayload
import gg.norisk.core.payloads.AbstractModuleDeactivatePayload
import gg.norisk.core.payloads.AbstractToastPayload
import gg.norisk.core.payloads.Dimension
import gg.norisk.core.payloads.Modules
import gg.norisk.core.payloads.RGBColor
import gg.norisk.core.payloads.ToastType
import gg.norisk.core.payloads.XYZ
import java.util.UUID

class NoRiskServerApi {
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

    fun createModuleDeactivatePayload(
        module: Modules
    ): AbstractModuleDeactivatePayload {
        return Core.createModuleDeactivatePayload(module)
    }

    fun createBeaconBeamPayload(
        xyz: XYZ,
        dimension: Dimension,
        color: RGBColor
    ): AbstractBeaconBeamPayload {
        return Core.createBeaconBeamPayload(xyz, dimension, color)
    }
}