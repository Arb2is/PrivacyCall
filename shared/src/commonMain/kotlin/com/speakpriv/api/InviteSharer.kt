package com.speakpriv.api

/**
 * Handles sharing a download link for the app through the OS's native share sheet.
 */
interface InviteSharer {
    /**
     * Opens the native share sheet to invite others to download the app.
     */
    fun share()
}
