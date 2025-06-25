package gg.norisk.fabric.client

import net.fabricmc.api.ClientModInitializer

class FabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("NoRiskClientServerAPI Fabric Client wurde geladen!")
    }
}