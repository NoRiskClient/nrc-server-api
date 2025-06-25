package gg.norisk.core.payloads

import java.util.UUID

enum class ToastType {
    TOAST,
    PROGRESS_BAR_TOAST
}

abstract class AbstractToastPayload(
    val toastType: ToastType,
    val playerHead: Boolean,
    val playerUUID: UUID? = null,
    val success: Boolean? = null,
    val color: String? = null
)