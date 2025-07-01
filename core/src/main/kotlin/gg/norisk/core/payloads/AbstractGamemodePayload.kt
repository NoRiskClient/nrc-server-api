package gg.norisk.core.payloads

abstract class AbstractGamemodePayload (
    val gamemode: String,
    ) : AbstractPayload("gamemode") {
}