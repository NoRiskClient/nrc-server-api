package gg.norisk.paper.listener;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.PacketHandler;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.in.HandshakePayload;
import gg.norisk.core.payloads.out.ToastPayload;
import gg.norisk.paper.api.ServerAPI;
import gg.norisk.core.payloads.models.ToastType;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.UUID;
import gg.norisk.core.payloads.models.Dimension;
import gg.norisk.core.payloads.models.RGBColor;
import gg.norisk.core.payloads.models.XYZ;
import gg.norisk.core.payloads.models.Modules;
import gg.norisk.core.payloads.in.InputbarResponsePayload;
import gg.norisk.core.models.NrcPlayer;
import java.util.Arrays;


@RequiredArgsConstructor
public class JoinListener implements PacketListener {

    private final ServerAPI serverAPI;
    private final CoreAPI coreAPI;

    public JoinListener(CoreAPI coreAPI) {
        this.coreAPI = coreAPI;
        this.serverAPI = gg.norisk.paper.Paper.getApi();
    }

    @PacketHandler
    public void onPlayerJoin(UUID uuid, HandshakePayload payload) {
        Bukkit.broadcastMessage(uuid + " joined the game!");
        coreAPI.getPlayerManager().setNrcPlayer(uuid, true);

        NrcPlayer nrcPlayer = coreAPI.getPlayerManager().getNrcPlayer(uuid);
        if (nrcPlayer == null) return;

        serverAPI.sendPacket(uuid, nrcPlayer.sendToast(
                true,
                "Willkommen!",
                "Du bist dem Server beigetreten.",
                true,
                uuid,
                ToastType.SUCCESS
        ));
        serverAPI.sendPacket(uuid, nrcPlayer.sendInputbar(
                "Wie heißt du?",
                "Dein Name...",
                32
        ));
        serverAPI.sendPacket(uuid, nrcPlayer.sendWheel(
                "Test-Eintrag",
                "/help"
        ));
        serverAPI.sendPacket(uuid, nrcPlayer.sendGamemode(
                "CityBuild"
        ));
        serverAPI.sendPacket(uuid, nrcPlayer.sendBeaconBeam(
                new XYZ(0, 64, 0),
                Dimension.OVERWORLD,
                new RGBColor(255, 0, 0)
        ));
        serverAPI.sendPacket(uuid, nrcPlayer.sendModuleDeactivate(
                Arrays.asList(Modules.FOV_CHANGER, Modules.FREE_LOOK_MODULE)
        ));
    }

    @PacketHandler
    public void onInputbarResponse(UUID uuid, InputbarResponsePayload payload) {
        if (!payload.isCanceled()) {
            Bukkit.broadcastMessage(uuid + " hat geantwortet: " + payload.getInput());
        } else {
            Bukkit.broadcastMessage(uuid + " hat die Eingabe abgebrochen.");
        }
    }

}
