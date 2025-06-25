package gg.norisk.fabric

import net.fabricmc.api.DedicatedServerModInitializer

class FabricServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        println("Initializing NoRiskClient-Server-API Fabric-Server Module...")
    }
}