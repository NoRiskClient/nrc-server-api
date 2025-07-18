package gg.norisk.core.models;

import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.payloads.models.*;
import gg.norisk.core.payloads.out.ToastPayload;
import gg.norisk.core.payloads.out.InputbarPayload;
import gg.norisk.core.payloads.out.WheelPayload;
import gg.norisk.core.payloads.out.GamemodePayload;
import gg.norisk.core.payloads.out.BeaconBeamPayload;
import gg.norisk.core.payloads.out.ModuleDeactivatePayload;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public record NrcPlayer(UUID uniqueId, gg.norisk.core.common.NoRiskServerAPI serverAPI) {
    public void sendPayload(OutPayload payload) {
        serverAPI.sendPacket(uniqueId, payload);
    }

    public void sendPayload(String channel, byte[] data) {
    }

    public void sendToast(Boolean progressBar, String header, String content, Boolean playerHead, UUID playerUUID, ToastType toastType) {
        serverAPI.sendPacket(uniqueId, new ToastPayload(progressBar, header, content, playerHead, playerUUID, toastType));
    }

    public void sendInputbar(String header, String placeholder, int maxLength) {
        serverAPI.sendPacket(uniqueId, new InputbarPayload(header, placeholder, maxLength));
    }

    public void sendWheel(String name, String command) {
        serverAPI.sendPacket(uniqueId, new WheelPayload(name, command));
    }

    public void sendGamemode(String gamemode) {
        serverAPI.sendPacket(uniqueId, new GamemodePayload(gamemode));
    }

    @Deprecated
    public void sendBeaconBeam(XYZ xyz, Dimension dimension, RGBColor color) {
        serverAPI.sendPacket(uniqueId, new BeaconBeamPayload(xyz, dimension, color));
    }

    public void sendModuleDeactivate(List<Modules> modules) {
        serverAPI.sendPacket(uniqueId, new ModuleDeactivatePayload(modules));
    }

    public <R extends InPayload> void sendRequest(String channel, OutPayload request, Consumer<R> callback) {
        serverAPI.sendRequest(uniqueId, request, callback);
    }

    public void registerListener(PacketListener listener) {
        serverAPI.registerListener(listener);
    }
}
