package gg.norisk.core.manager;

import gg.norisk.core.common.NoRiskServerAPI;
import gg.norisk.core.models.NrcPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, NrcPlayer> nrcPlayers = new HashMap<>();
    private NoRiskServerAPI serverAPI;

    public PlayerManager(NoRiskServerAPI serverAPI) {
        this.serverAPI = serverAPI;
    }

    public boolean isNrcPlayer(UUID uuid) {
        return nrcPlayers.containsKey(uuid);
    }

    public void setNrcPlayer(UUID uuid, boolean nrc) {
        if (nrc) {
            nrcPlayers.put(uuid, new NrcPlayer(uuid, serverAPI));
        } else {
            nrcPlayers.remove(uuid);
        }
    }

    public void removePlayer(UUID uuid) {
        nrcPlayers.remove(uuid);
    }

    public NrcPlayer getNrcPlayer(UUID uuid) {
        return nrcPlayers.get(uuid);
    }

    public void setServerAPI(NoRiskServerAPI serverAPI) {
        this.serverAPI = serverAPI;
        nrcPlayers.replaceAll((uuid, oldPlayer) -> new NrcPlayer(uuid, serverAPI));
    }
}
