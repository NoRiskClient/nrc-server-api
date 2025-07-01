package gg.norisk.core.payloads

abstract class AbstractWheelPayload (
    val name: String,
    val command: String,
) : AbstractPayload("wheel") {
}