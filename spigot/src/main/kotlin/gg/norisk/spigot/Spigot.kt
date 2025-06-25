package gg.norisk.spigot

import org.bukkit.plugin.java.JavaPlugin

class Spigot : JavaPlugin() {
    override fun onEnable() {
        logger.info("Initialized NoRiskClient-Server-API Spigot Module!")
    }
}