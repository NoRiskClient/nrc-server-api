package gg.norisk.core.common

import gg.norisk.core.manager.HandshakeManager
import java.util.UUID

object NRCPlayer {
    private val handshakeManager: HandshakeManager
        get() = ChannelApi::class.java.getDeclaredField("handshakeManager").apply { isAccessible = true }.get(null) as HandshakeManager

    fun listAll(): Set<UUID> =
        handshakeManager.javaClass.getDeclaredField("nrcPlayers").apply { isAccessible = true }.get(handshakeManager) as Set<UUID>

    fun listAllNoNRC(): Set<UUID> {
        val all = handshakeManager.javaClass.getDeclaredField("pendingHandshakes").apply { isAccessible = true }.get(handshakeManager) as Map<UUID, *>
        return all.keys
    }

    fun user(uuid: UUID): Boolean = listAll().contains(uuid)
}