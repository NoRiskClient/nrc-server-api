package gg.norisk.core.common;

import gg.norisk.core.payloads.InPayload;
import gg.norisk.core.payloads.OutPayload;

import java.util.UUID;
import java.util.function.Consumer;

public interface NoRiskServerAPI {

    void sendPacket(UUID player, OutPayload packet);

    <R extends InPayload> void sendRequest(UUID player, OutPayload request, Consumer<R> callback);

    void registerListener(PacketListener listener);

}
