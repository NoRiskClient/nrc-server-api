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
        coreAPI.registerPlayer(uuid);

        serverAPI.sendPacket(uuid, new ToastPayload(
                false,
                "Willkommen!",
                "Du bist dem Server beigetreten.",
                false,
                uuid,
                ToastType.SUCCESS
        ));
    }

}

