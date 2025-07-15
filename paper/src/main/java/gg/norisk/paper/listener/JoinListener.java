package gg.norisk.paper.listener;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.PacketHandler;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.in.HandshakePayload;
import java.util.UUID;

public class JoinListener implements PacketListener {
    private final CoreAPI coreAPI;

    public JoinListener(CoreAPI coreAPI) {
        this.coreAPI = coreAPI;
    }

    @PacketHandler
    public void onPlayerJoin(UUID uuid, HandshakePayload payload) {
        coreAPI.getPlayerManager().setNrcPlayer(uuid, true);
    }
}
