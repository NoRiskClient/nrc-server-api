package gg.norisk.core.manager

import java.util.Collections
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class HandshakeManager(
    private val onNrcClient: (UUID) -> Unit,
    private val onNonNrcClient: (UUID) -> Unit,
    private val handshakeTimeoutSeconds: Long = 20L
) {
    private val pendingHandshakes = ConcurrentHashMap<UUID, Timer>()
    private val nrcPlayers = Collections.synchronizedSet(mutableSetOf<UUID>())

    fun onPlayerJoin(player: UUID, sendHandshake: () -> Unit) {
        sendHandshake()
        pendingHandshakes[player]?.cancel()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handleHandshakeTimeout(player)
            }
        }, TimeUnit.SECONDS.toMillis(handshakeTimeoutSeconds))
        pendingHandshakes[player] = timer
    }

    fun onClientHandshake(player: UUID) {
        val timer = pendingHandshakes.remove(player)
        timer?.cancel()
        nrcPlayers.add(player)
        onNrcClient(player)
    }

    fun isNrcPlayer(player: UUID): Boolean = nrcPlayers.contains(player)

    fun onPlayerLeave(player: UUID) {
        pendingHandshakes.remove(player)?.cancel()
        nrcPlayers.remove(player)
    }

    private fun handleHandshakeTimeout(player: UUID) {
        pendingHandshakes.remove(player)
        onNonNrcClient(player)
    }
}