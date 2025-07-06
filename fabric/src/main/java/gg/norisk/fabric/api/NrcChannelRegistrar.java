package gg.norisk.fabric.api;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gg.norisk.core.common.Constants;
import gg.norisk.core.payloads.Payloads;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NrcChannelRegistrar {
    private static final Logger logger = LoggerFactory.getLogger("NrcChannelRegistrar");
    
    public static void register() {
        logger.info("Registering '" + Constants.NRC_CHANNEL + "' channel...");
        
        Identifier identifier = Identifier.of("norisk", "main");
        
        ServerPlayNetworking.registerGlobalReceiver(NrcCustomPayload.ID_OBJ, (payload, context) -> {
            var player = context.player();
            String json = payload.getJson();
            
            if (json != null) {
                logger.debug("Received message from player {}: {}", player.getUuid(), json);
                byte[] messageBytes = json.getBytes(StandardCharsets.UTF_8);
                Payloads.receive(player.getUuid(), messageBytes);
            }
        });
        
        logger.info("Channel registration complete.");
    }
} 