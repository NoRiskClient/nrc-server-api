package gg.norisk.core

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

    fun createBeaconBeamPayload(
        xyz: XYZ,
        dimension: Dimension,
        color: RGBColor
    ): AbstractBeaconBeamPayload {
        return object : AbstractBeaconBeamPayload(xyz, dimension, color) {}
    }

    fun createGamemodePayload(
        gamemode: String,
    ): AbstractGamemodePayload {
        return object : AbstractGamemodePayload(gamemode) {}
    }

    fun createWheelPayload(
        name: String,
        command: String,
    ): AbstractWheelPayload {
        return object : AbstractWheelPayload(name, command) {}
    }

    fun createInputbarPayload(
        input: String,
        placeholder: String,
        maxLength: Int,
    ): AbstractInputbarPayload {
        return object : AbstractInputbarPayload(input, placeholder, maxLength) {}
    }
}