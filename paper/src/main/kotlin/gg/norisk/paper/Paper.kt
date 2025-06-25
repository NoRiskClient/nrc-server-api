package gg.norisk.paper

import org.bukkit.plugin.java.JavaPlugin

class Paper : JavaPlugin() {
    override fun onEnable() {
        logger.info("Initialized NoRiskClient-Server-API Paper Module!")
    }
}