package gg.norisk.core.payloads

/**
 * Abstract payload for showing input prompts in the NoRisk Client
 *
 * @param header The header text for the input prompt
 * @param placeholder The placeholder text shown in the input field (optional)
 * @param maxLength The maximum number of characters allowed in the input
 */
abstract class AbstractInputbarPayload(
    val header: String,
    val placeholder: String? = null,
    val maxLength: Int,
) : AbstractPayload("inputbar")
