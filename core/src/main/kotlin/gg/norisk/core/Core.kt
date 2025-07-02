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

    /**
     * Creates a toast notification payload to display messages in the NoRisk Client
     *
     * @param progressBar Whether to show a progress bar in the toast
     * @param header The header text of the toast notification
     * @param content The main content text of the toast notification
     * @param playerHead Whether to show the player's avatar in the toast
     * @param playerUUID The UUID of the player whose avatar to show (required if playerHead is true)
     * @param toastType The type of toast (INFO, SUCCESS, ERROR) which determines the color scheme
     * @return AbstractToastPayload The created toast payload
     */
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

    /**
     * Creates a module deactivate payload to disable specific NoRisk Client modules
     *
     * @param modules List of modules to deactivate on the client
     * @return AbstractModuleDeactivatePayload The created module deactivate payload
     */
    fun createModuleDeactivatePayload(
        modules: List<Modules>
    ): AbstractModuleDeactivatePayload {
        return object : AbstractModuleDeactivatePayload(modules) {}
    }

    /**
     * Creates a beacon beam payload to display colored light beams at specific coordinates
     *
     * @param xyz The X, Y, Z coordinates where the beacon beam should appear
     * @param dimension The dimension where the beacon beam should appear (OVERWORLD, NETHER, END)
     * @param color The RGB color of the beacon beam
     * @return AbstractBeaconBeamPayload The created beacon beam payload
     */
    fun createBeaconBeamPayload(
        xyz: XYZ,
        dimension: Dimension,
        color: RGBColor
    ): AbstractBeaconBeamPayload {
        return object : AbstractBeaconBeamPayload(xyz, dimension, color) {}
    }

    /**
     * Creates a gamemode payload to override the gamemode display in the NoRisk Client
     *
     * @param gamemode The custom gamemode name to display
     * @return AbstractGamemodePayload The created gamemode payload
     */
    fun createGamemodePayload(
        gamemode: String,
    ): AbstractGamemodePayload {
        return object : AbstractGamemodePayload(gamemode) {}
    }

    /**
     * Creates a wheel payload to add entries to the NoRisk Client action wheel
     *
     * @param name The display name of the action wheel entry
     * @param command The command to execute when the entry is selected
     * @return AbstractWheelPayload The created wheel payload
     */
    fun createWheelPayload(
        name: String,
        command: String,
    ): AbstractWheelPayload {
        return object : AbstractWheelPayload(name, command) {}
    }

    /**
     * Creates an inputbar payload to show input prompts in the NoRisk Client
     *
     * @param input The header text for the input prompt
     * @param placeholder The placeholder text shown in the input field
     * @param maxLength The maximum number of characters allowed in the input
     * @return AbstractInputbarPayload The created inputbar payload
     */
    fun createInputbarPayload(
        input: String,
        placeholder: String,
        maxLength: Int,
    ): AbstractInputbarPayload {
        return object : AbstractInputbarPayload(input, placeholder, maxLength) {}
    }
}