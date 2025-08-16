package com.speakpriv.api

/**
 * An abstraction for controlling the device's speakerphone.
 */
interface SpeakerphoneController {
    /**
     * Turns the speakerphone on or off.
     * @param isOn `true` to turn the speakerphone on, `false` to turn it off.
     */
    fun setSpeaker(isOn: Boolean)

    /**
     * @return `true` if the speakerphone is currently on, `false` otherwise.
     */
    fun isSpeakerOn(): Boolean
}
