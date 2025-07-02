package gg.norisk.core.payloads

/**
 * Abstract payload for overriding the gamemode display in the NoRisk Client
 *
 * @param gamemode The custom gamemode name to display in the client
 */
abstract class AbstractGamemodePayload(
    val gamemode: String,
) : AbstractPayload("gamemode") {
}