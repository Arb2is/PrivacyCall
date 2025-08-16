package com.speakpriv.model

/**
 * UI representation of a contact.
 * Minimal data to avoid PII leakage.
 */
data class ContactUi(
    val name: String?,
    val photoUri: String?
)
