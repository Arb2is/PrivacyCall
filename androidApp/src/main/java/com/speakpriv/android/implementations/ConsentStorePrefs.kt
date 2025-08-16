package com.speakpriv.android.implementations

import android.content.Context
import com.speakpriv.api.ConsentStore

class ConsentStorePrefs(private val context: Context) : ConsentStore {

    private val prefs = context.getSharedPreferences("consent_prefs", Context.MODE_PRIVATE)

    override fun isGranted(): Boolean {
        return prefs.getBoolean(KEY_CONSENT, false)
    }

    override fun setGranted(isGranted: Boolean) {
        prefs.edit().putBoolean(KEY_CONSENT, isGranted).apply()
    }

    companion object {
        private const val KEY_CONSENT = "key_consent"
    }
}
