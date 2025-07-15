package gg.norisk.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;

public class Fabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        System.out.println("NoRiskClient-Server-API Fabric module is starting...");
        // FabricServer will be initialized automatically as it implements DedicatedServerModInitializer
        System.out.println("NoRiskClient-Server-API Fabric module is ready!");
    }
} 