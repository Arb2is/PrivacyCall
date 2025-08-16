package com.speakpriv.android.implementations

import android.content.Context
import android.media.AudioManager
import com.speakpriv.api.SpeakerphoneController

class SpeakerphoneControllerAndroid(private val context: Context) : SpeakerphoneController {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun setSpeaker(isOn: Boolean) {
        audioManager.isSpeakerphoneOn = isOn
    }

    override fun isSpeakerOn(): Boolean {
        return audioManager.isSpeakerphoneOn
    }
}
