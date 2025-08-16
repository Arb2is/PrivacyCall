package com.speakpriv.android.implementations

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.speakpriv.api.AudioPrivacyMonitor
import com.speakpriv.model.Exposure
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@RequiresApi(Build.VERSION_CODES.M)
class AudioPrivacyMonitorAndroid(private val context: Context) : AudioPrivacyMonitor {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override val localExposure: Flow<Exposure> = callbackFlow {
        val callback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                trySend(getCurrentExposure())
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                trySend(getCurrentExposure())
            }
        }

        // Send initial state
        trySend(getCurrentExposure())

        audioManager.registerAudioDeviceCallback(callback, null)

        awaitClose { audioManager.unregisterAudioDeviceCallback(callback) }
    }

    override fun start() {
        // The flow is hot and starts upon collection
    }

    override fun stop() {
        // The flow is torn down when the scope is cancelled
    }

    private fun getCurrentExposure(): Exposure {
        if (audioManager.isSpeakerphoneOn) return Exposure.EXPOSED

        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        for (device in devices) {
            when (device.type) {
                AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> return Exposure.EXPOSED
                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
                AudioDeviceInfo.TYPE_HDMI,
                AudioDeviceInfo.TYPE_USB_DEVICE,
                AudioDeviceInfo.TYPE_USB_HEADSET -> return Exposure.EXPOSED

                AudioDeviceInfo.TYPE_BUILTIN_EARPIECE,
                AudioDeviceInfo.TYPE_WIRED_HEADSET,
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
                AudioDeviceInfo.TYPE_BLUETOETOOTH_SCO -> return Exposure.PRIVATE
            }
        }
        return Exposure.UNVERIFIED // Default case
    }
}
