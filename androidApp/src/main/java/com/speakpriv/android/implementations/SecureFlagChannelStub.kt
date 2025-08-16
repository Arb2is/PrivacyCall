package com.speakpriv.android.implementations

import com.speakpriv.api.SecureFlagChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A stub implementation of the SecureFlagChannel for development and testing.
 * It simulates a handshake timeout after 3.5 seconds.
 */
class SecureFlagChannelStub : SecureFlagChannel {

    override val remoteExposure: Flow<Boolean?> = flow {
        delay(3500)
        emit(null) // Simulate timeout -> UNVERIFIED
    }

    override suspend fun connect(callId: String, peerHint: String?) {
        // No-op
    }

    override suspend fun sendExposureFlag(isExposed: Boolean) {
        // No-op
    }

    override fun close() {
        // No-op
    }
}
