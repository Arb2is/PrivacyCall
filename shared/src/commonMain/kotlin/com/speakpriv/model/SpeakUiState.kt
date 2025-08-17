package com.speakpriv.model

data class SpeakUiState(
    val local: Exposure,
    val contactName: String,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean,
    val ledEnabled: Boolean
)
