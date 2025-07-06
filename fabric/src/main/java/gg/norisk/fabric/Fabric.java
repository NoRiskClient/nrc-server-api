package gg.norisk.fabric;

import gg.norisk.fabric.api.NrcChannelRegistrar;
import net.fabricmc.api.DedicatedServerModInitializer;

public class Fabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        System.out.println("NoRiskClient-Server-API Fabric module is starting...");
        
        NrcChannelRegistrar.register();
        
        System.out.println("NoRiskClient-Server-API Fabric module is ready!");
    }
} 