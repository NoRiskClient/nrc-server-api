package gg.norisk.core.payloads

import java.util.UUID

enum class ToastType {
    INFO,
    SUCCESS,
    ERROR
}

abstract class AbstractToastPayload(
    val progressBar: Boolean,
    val header: String,
    val content : String,
    val playerHead: Boolean,
    val playerUUID: UUID? = null,
    val toastType: ToastType
) : AbstractPayload("toast") {
}