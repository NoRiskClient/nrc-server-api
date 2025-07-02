package gg.norisk.core.common

import gg.norisk.core.Core
import gg.norisk.core.manager.InputbarPayloadManager
import gg.norisk.core.payloads.AbstractBeaconBeamPayload
import gg.norisk.core.payloads.AbstractGamemodePayload
import gg.norisk.core.payloads.AbstractInputbarPayload
import gg.norisk.core.payloads.AbstractModuleDeactivatePayload
import gg.norisk.core.payloads.AbstractToastPayload
import gg.norisk.core.payloads.AbstractWheelPayload
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
        modules: List<Modules>
    ): AbstractModuleDeactivatePayload {
        return Core.createModuleDeactivatePayload(modules)
    }

    fun createBeaconBeamPayload(
        xyz: XYZ,
        dimension: Dimension,
        color: RGBColor
    ): AbstractBeaconBeamPayload {
        return Core.createBeaconBeamPayload(xyz, dimension, color)
    }

    fun createGamemodePayload(
        gamemode: String,
    ): AbstractGamemodePayload {
        return Core.createGamemodePayload(gamemode)
    }

    fun createWheelPayload(
        name: String,
        command: String,
    ): AbstractWheelPayload {
        return Core.createWheelPayload(name, command)
    }

    fun createInputbarPayload(
        input: String,
        placeholder: String,
        maxLength: Int
    ): AbstractInputbarPayload {
        return Core.createInputbarPayload(input, placeholder, maxLength)
    }

    fun requestInput(
        playerUuid: UUID,
        inputbarPayload: AbstractInputbarPayload,
        onResponse: (String) -> Unit
    ) {
        InputbarPayloadManager.registerInputHandler(playerUuid, onResponse)
    }
}