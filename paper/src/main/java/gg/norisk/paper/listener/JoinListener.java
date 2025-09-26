package gg.norisk.paper.listener;

import gg.norisk.core.common.CoreAPI;
import gg.norisk.core.common.PacketHandler;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.in.HandshakePayload;
import java.util.UUID;

public record JoinListener(CoreAPI coreAPI) implements PacketListener {

  @PacketHandler
  public void onPlayerJoin(UUID uuid, HandshakePayload payload) {
    coreAPI.getPlayerManager().setNrcPlayer(uuid, true);
  }
}
