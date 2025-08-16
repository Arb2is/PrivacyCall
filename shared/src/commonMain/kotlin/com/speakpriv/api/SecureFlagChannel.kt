package com.speakpriv.api

import kotlinx.coroutines.flow.Flow

/**
 * A secure channel to exchange the exposure flag with a peer.
 * The channel is established per-call and torn down afterwards.
 */
interface SecureFlagChannel {
    /**
     * A flow that emits the remote peer's exposure status.
     * Emits `null` if the connection is pending or lost.
     * Emits `true` for EXPOSED, `false` for PRIVATE.
     */
    val remoteExposure: Flow<Boolean?>

    /**
     * Connects to the signaling server and establishes a secure channel with the peer.
     * @param callId A unique identifier for the current call, used for pairing.
     * @param peerHint Optional hint for the peer's identity, not used in the current design but useful for future extensions.
     */
    suspend fun connect(callId: String, peerHint: String? = null)

    /**
     * Sends the local exposure flag to the peer.
     * @param isExposed True if the local user is exposed, false otherwise.
     */
    suspend fun sendExposureFlag(isExposed: Boolean)

    /**
     * Closes the connection and tears down the channel.
     */
    fun close()
}
