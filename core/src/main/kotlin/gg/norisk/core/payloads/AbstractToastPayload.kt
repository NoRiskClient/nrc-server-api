package gg.norisk.core.payloads

import java.util.UUID

enum class ToastType {
    TOAST,
    PROGRESS_BAR_TOAST
}

abstract class AbstractToastPayload(
    val toastType: ToastType,
    val header: String,
    val content : String,
    val playerHead: Boolean,
    val playerUUID: UUID? = null,
    val success: Boolean? = null,
) : AbstractPayload("toast") {
}
