package gg.norisk.core.payloads.models;

public enum Modules {
    FOV_CHANGER("FovChanger"),
    FULL_BRIGHT("FullBright"),
    ZOOM("Zoom"),
    FREE_LOOK("FreeLook"),
    NO_FOG("NoFog"),
    ARROW_TRAIL("ArrowTrail"),
    PACK_TWEAKS("PackTweaks"),
    ITEM_MODEL("ItemModel"),
    AUTO_TEXT("AutoText"),
    ITEM_HIGHLIGHTER("ItemHighlighter"),
    TNT_TIMER("TntTimer"),
    WEATHER_CHANGER("WeatherChanger"),
    TIME_CHANGER("TimeChanger");

    private final String moduleName;

    Modules(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String toString() {
        return moduleName;
    }
} 