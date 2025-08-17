package com.speakpriv.android.implementations

import android.content.Context

class AlertPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("alert_prefs", Context.MODE_PRIVATE)

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION, value).apply()

    var ledEnabled: Boolean
        get() = prefs.getBoolean(KEY_LED, false)
        set(value) = prefs.edit().putBoolean(KEY_LED, value).apply()

    companion object {
        private const val KEY_SOUND = "sound"
        private const val KEY_VIBRATION = "vibration"
        private const val KEY_LED = "led"
    }
}
