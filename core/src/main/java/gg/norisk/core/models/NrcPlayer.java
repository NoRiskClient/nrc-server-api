package gg.norisk.core.models;

import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;
import gg.norisk.core.payloads.out.ToastPayload;
import gg.norisk.core.payloads.out.InputbarPayload;
import gg.norisk.core.payloads.out.WheelPayload;
import gg.norisk.core.payloads.out.GamemodePayload;
import gg.norisk.core.payloads.out.BeaconBeamPayload;
import gg.norisk.core.payloads.out.ModuleDeactivatePayload;
import gg.norisk.core.payloads.models.ToastType;
import gg.norisk.core.payloads.models.Dimension;
import gg.norisk.core.payloads.models.RGBColor;
import gg.norisk.core.payloads.models.XYZ;
import gg.norisk.core.payloads.models.Modules;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class NrcPlayer {
    private final UUID uniqueId;

    public NrcPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void sendPayload(OutPayload payload) {
    }

    public void sendPayload(String channel, byte[] data) {
    }

    public ToastPayload sendToast(boolean show, String header, String description, boolean persistent, UUID uuid, ToastType type) {
        return new ToastPayload(show, header, description, persistent, uuid, type);
    }

    public InputbarPayload sendInputbar(String header, String placeholder, int maxLength) {
        return new InputbarPayload(header, placeholder, maxLength);
    }

    public WheelPayload sendWheel(String entry, String command) {
        return new WheelPayload(entry, command);
    }

    public GamemodePayload sendGamemode(String gamemode) {
        return new GamemodePayload(gamemode);
    }

    public BeaconBeamPayload sendBeaconBeam(XYZ xyz, Dimension dimension, RGBColor color) {
        return new BeaconBeamPayload(xyz, dimension, color);
    }

    public ModuleDeactivatePayload sendModuleDeactivate(List<Modules> modules) {
        return new ModuleDeactivatePayload(modules);
    }

    public <R extends InPayload> void sendRequest(String channel, OutPayload request, Consumer<R> callback) {
    }

    public void registerListener(PacketListener listener) {
    }
}
