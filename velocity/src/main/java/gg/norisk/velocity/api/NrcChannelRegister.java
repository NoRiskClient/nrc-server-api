package gg.norisk.velocity.api;

import org.slf4j.Logger;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import gg.norisk.core.common.Constants;

public class NrcChannelRegister {
    public static void register(ProxyServer server, Logger logger) {
        logger.info("Registering '" + Constants.NRC_CHANNEL + "' channel...");
        MinecraftChannelIdentifier channelIdentifier = MinecraftChannelIdentifier.from(Constants.NRC_CHANNEL);
        server.getChannelRegistrar().register(channelIdentifier);
        logger.info("Channel registration complete.");
    }
} 