package gg.norisk.core.payloads

import java.util.UUID

/**
 * Enum representing different toast notification types
 */
enum class ToastType {
    INFO,
    SUCCESS,
    ERROR
}

/**
 * Abstract payload for displaying toast notifications in the NoRisk Client
 *
 * @param progressBar Whether to show a progress bar in the toast
 * @param header The header text of the toast notification
 * @param content The main content text of the toast notification
 * @param playerHead Whether to show the player's avatar in the toast
 * @param playerUUID The UUID of the player whose avatar to show (required if playerHead is true)
 * @param toastType The type of toast (INFO, SUCCESS, ERROR) which determines the color scheme
 */
abstract class AbstractToastPayload(
    val progressBar: Boolean,
    val header: String,
    val content : String,
    val playerHead: Boolean,
    val playerUUID: UUID? = null,
    val toastType: ToastType
) : AbstractPayload("toast")
