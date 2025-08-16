package com.speakpriv.api

import com.speakpriv.model.Exposure
import kotlinx.coroutines.flow.Flow

/**
 * Monitors the local audio device to determine the current privacy exposure.
 */
interface AudioPrivacyMonitor {
    /**
     * A flow that emits the current local exposure state whenever it changes.
     */
    val localExposure: Flow<Exposure>

    /**
     * Starts monitoring the audio devices.
     */
    fun start()

    /**
     * Stops monitoring the audio devices.
     */
    fun stop()
}
