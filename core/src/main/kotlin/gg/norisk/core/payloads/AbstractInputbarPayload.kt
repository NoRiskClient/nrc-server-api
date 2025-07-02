package gg.norisk.core.payloads


abstract class AbstractInputbarPayload (
    val header: String,
    val placeholder: String? = null,
    val maxLength: Int,
) : AbstractPayload("inputbar") {
}