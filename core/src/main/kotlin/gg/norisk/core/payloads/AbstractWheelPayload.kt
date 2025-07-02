package gg.norisk.core.payloads

/**
 * Abstract payload for adding entries to the NoRisk Client action wheel
 *
 * @param name The display name of the action wheel entry
 * @param command The command to execute when the entry is selected
 */
abstract class AbstractWheelPayload (
    val name: String,
    val command: String,
) : AbstractPayload("wheel") {
}