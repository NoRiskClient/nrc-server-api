package gg.norisk.fabric.client

import net.fabricmc.api.ClientModInitializer

class FabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("Initialized NoRiskClient-Server-API Fabric-Client!")
    }
}