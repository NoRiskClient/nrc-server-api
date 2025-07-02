package gg.norisk.core.payloads

class InputbarResponsePayload(
    val input: String? = null,
    val canceled: Boolean = false
) : AbstractPayload("inputbar_response")
