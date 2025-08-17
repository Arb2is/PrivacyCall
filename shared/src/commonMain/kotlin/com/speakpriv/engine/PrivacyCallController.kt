package com.speakpriv.engine

import com.speakpriv.api.*
import com.speakpriv.model.Exposure
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * The core state machine of the application.
 * It orchestrates all the other components to manage the call privacy state.
 */
class PrivacyCallController(
    private val callId: String,
    private val audioMonitor: AudioPrivacyMonitor,
    private val flagChannel: SecureFlagChannel,
    private val speakerController: SpeakerphoneController,
    private val consentStore: ConsentStore,
    private val coroutineScope: CoroutineScope
) {

    private val _localState = MutableStateFlow(Exposure.UNVERIFIED)
    val localState: StateFlow<Exposure> = _localState.asStateFlow()

    private val _remoteState = MutableStateFlow(Exposure.UNVERIFIED)
    val remoteState: StateFlow<Exposure> = _remoteState.asStateFlow()

    fun start() {
        if (!consentStore.isGranted()) {
            _localState.value = Exposure.UNVERIFIED
            _remoteState.value = Exposure.UNVERIFIED
            return
        }

        audioMonitor.start()
        monitorLocalExposure()
        connectToPeer()
    }

    fun stop() {
        audioMonitor.stop()
        flagChannel.close()
        coroutineScope.cancel() // Cancel the scope to clean up all coroutines
    }

    fun toggleSpeaker() {
        val currentSpeakerState = speakerController.isSpeakerOn()
        speakerController.setSpeaker(!currentSpeakerState)
    }

    private fun monitorLocalExposure() {
        audioMonitor.localExposure
            .debounce(2500) // Debounce to avoid rapid state changes
            .onEach { exposure ->
                _localState.value = exposure
                flagChannel.sendExposureFlag(exposure == Exposure.EXPOSED)
            }
            .launchIn(coroutineScope)
    }

    private fun connectToPeer() {
        coroutineScope.launch {
            flagChannel.connect(callId)
            monitorRemoteExposure()
        }
    }

    private fun monitorRemoteExposure() {
        flagChannel.remoteExposure
            .onEach { isExposed ->
                _remoteState.value = when (isExposed) {
                    null -> Exposure.UNVERIFIED // Connection lost
                    true -> Exposure.EXPOSED
                    false -> Exposure.PRIVATE
                }
            }
            .launchIn(coroutineScope)
    }
}
