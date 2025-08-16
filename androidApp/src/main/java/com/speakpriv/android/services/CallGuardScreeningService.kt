package com.speakpriv.android.services

import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class CallGuardScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val callId = callDetails.getHandle().toString() // A unique identifier for the call

        val intent = Intent(this, PrivacyFgService::class.java).apply {
            putExtra(PrivacyFgService.EXTRA_CALL_ID, callId)
        }
        startForegroundService(intent)
    }
}
