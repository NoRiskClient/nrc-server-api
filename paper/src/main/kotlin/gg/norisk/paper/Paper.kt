package gg.norisk.paper

import org.bukkit.plugin.java.JavaPlugin

class Paper : JavaPlugin() {
    override fun onEnable() {
        logger.info("NoRiskClientServerAPI wurde aktiviert!")
    }

    override fun onDisable() {
        logger.info("NoRiskClientServerAPI wurde deaktiviert!")
    }
}