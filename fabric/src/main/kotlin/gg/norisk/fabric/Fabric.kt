package gg.norisk.fabric

import net.fabricmc.api.DedicatedServerModInitializer

class Fabric : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        println("Initialized NoRiskClient-Server-API Fabric Module")
    }
}