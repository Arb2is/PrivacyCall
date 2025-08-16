package com.speakpriv.api

/**
 * Manages persistent storage for user consent.
 */
interface ConsentStore {
    /**
     * @return `true` if the user has granted consent for the app to function, `false` otherwise.
     */
    fun isGranted(): Boolean

    /**
     * Sets the user's consent status.
     * @param isGranted The new consent status.
     */
    fun setGranted(isGranted: Boolean)
}
