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

abstract class AbstractModuleDeactivatePayload(
    val module: Modules,
) : AbstractPayload() {
    override val id: String = "module_deactivate"
    override val version: Int = 2
}
