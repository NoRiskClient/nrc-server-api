package gg.norisk.core.common.impl;

import gg.norisk.core.Core;
import gg.norisk.core.common.NoRiskServerApi;
import gg.norisk.core.manager.InputbarPayloadManager;
import gg.norisk.core.payloads.*;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NoRiskServerApiImpl implements NoRiskServerApi {
    private final Core core;
    private final InputbarPayloadManager inputbarPayloadManager;

    public NoRiskServerApiImpl() {
        this.core = new Core();
        this.inputbarPayloadManager = new InputbarPayloadManager();
    }

    public AbstractToastPayload createToastPayload(
            boolean progressBar,
            String header,
            String content,
            boolean playerHead,
            UUID playerUUID,
            ToastType toastType
    ) {
        return core.createToastPayload(progressBar, header, content, playerHead, playerUUID, toastType);
    }

    public AbstractModuleDeactivatePayload createModuleDeactivatePayload(
            List<Modules> modules
    ) {
        return core.createModuleDeactivatePayload(modules);
    }

    public AbstractBeaconBeamPayload createBeaconBeamPayload(
            XYZ xyz,
            Dimension dimension,
            RGBColor color
    ) {
        return core.createBeaconBeamPayload(xyz, dimension, color);
    }

    public AbstractGamemodePayload createGamemodePayload(
            String gamemode
    ) {
        return core.createGamemodePayload(gamemode);
    }

    public AbstractWheelPayload createWheelPayload(
            String name,
            String command
    ) {
        return core.createWheelPayload(name, command);
    }

    public AbstractInputbarPayload createInputbarPayload(
            String input,
            String placeholder,
            int maxLength
    ) {
        return core.createInputbarPayload(input, placeholder, maxLength);
    }

    public void requestInput(
            UUID playerUuid,
            AbstractInputbarPayload inputbarPayload,
            BiConsumer<UUID, byte[]> sendToClient,
            Consumer<String> onResponse
    ) {
        inputbarPayloadManager.registerInputSession(playerUuid, inputbarPayload, sendToClient, onResponse);
    }

    public void requestInput(
            UUID playerUuid,
            AbstractInputbarPayload inputbarPayload,
            BiConsumer<UUID, byte[]> sendToClient,
            Consumer<String> onResponse,
            Runnable onCancel
    ) {
        inputbarPayloadManager.registerInputSession(playerUuid, inputbarPayload, sendToClient, onResponse, onCancel);
    }
} 