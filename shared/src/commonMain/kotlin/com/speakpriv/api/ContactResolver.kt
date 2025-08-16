package com.speakpriv.api

import com.speakpriv.model.ContactUi

/**
 * Resolves contact information based on a phone number.
 * The implementation must be privacy-focused and not send contact data off-device.
 */
interface ContactResolver {
    /**
     * Looks up a contact by their phone number.
     * @param e164Number The phone number in E.164 format.
     * @return A [ContactUi] object if a contact is found, `null` otherwise.
     */
    fun forNumber(e164Number: String?): ContactUi?
}
