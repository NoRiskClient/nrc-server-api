package gg.norisk.core.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, Boolean> isNrcPlayer = new HashMap<>();

    public boolean isNrcPlayer(UUID uuid) {
        return isNrcPlayer.getOrDefault(uuid, false);
    }

    public void setNrcPlayer(UUID uuid, boolean nrc) {
        isNrcPlayer.put(uuid, nrc);
    }

    public void removePlayer(UUID uuid) {
        isNrcPlayer.remove(uuid);
    }

}
