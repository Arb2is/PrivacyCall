package com.speakpriv.model

/**
 * Represents the privacy exposure state of a call participant.
 */
enum class Exposure {
    /**
     * The participant is using a public audio device (speakerphone, external speaker).
     */
    EXPOSED,

    /**
     * The participant is using a private audio device (earpiece, headset).
     */
    PRIVATE,

    /**
     * The other participant does not have the app or the security handshake failed.
     */
    UNVERIFIED
}
