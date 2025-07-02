package gg.norisk.fabric

import gg.norisk.fabric.api.NrcChannelRegistrar
import net.fabricmc.api.DedicatedServerModInitializer

class Fabric : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        println("NoRiskClient-Server-API Fabric module is starting...")

        NrcChannelRegistrar.register()

        println("NoRiskClient-Server-API Fabric module is ready!")
    }
}