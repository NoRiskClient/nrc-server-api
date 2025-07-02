package gg.norisk.core.payloads

/**
 * Payload class for handling user input responses from the NoRisk Client
 * This payload is sent by the client when a user responds to an input prompt
 *
 * @param input The text input provided by the user (null if canceled)
 * @param canceled Whether the input was canceled by the user
 */
class InputbarResponsePayload(
    val input: String? = null,
    val canceled: Boolean = false
) : AbstractPayload("inputbar_response")
