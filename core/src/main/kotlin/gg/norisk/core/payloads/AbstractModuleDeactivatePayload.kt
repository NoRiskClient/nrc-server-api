package gg.norisk.core.payloads

enum class Modules {
    FovChanger,
    FullBrightModule,
    ZoomModule,
    FreeLookModule,
    NoFogModule,
    ArrowTrail,
    PackTweaks,
    ItemModel,
    AutoText,
    ItemHighlighter,
    TntTimer,
    WeatherChanger,
    TimeChanger
}

/**
 * Abstract payload for deactivating specific NoRisk Client modules
 *
 * @param modules List of modules to deactivate on the client
 */
abstract class AbstractModuleDeactivatePayload(
    val modules: List<Modules>,
) : AbstractPayload("module_deactivate") {
}
