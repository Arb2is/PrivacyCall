package com.speakpriv.android.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.speakpriv.engine.PrivacyCallController
import com.speakpriv.android.R
import com.speakpriv.android.implementations.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PrivacyFgService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var controller: PrivacyCallController? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val callId = intent?.getStringExtra(EXTRA_CALL_ID) ?: return START_NOT_STICKY

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        // Instantiate KMP components
        val audioMonitor = AudioPrivacyMonitorAndroid(applicationContext)
        val flagChannel = SecureFlagChannelStub() // Using STUB for now as per blueprint
        val speakerController = SpeakerphoneControllerAndroid(applicationContext)
        val consentStore = ConsentStorePrefs(applicationContext)

        // Create and start the controller
        controller = PrivacyCallController(
            callId = callId,
            audioMonitor = audioMonitor,
            flagChannel = flagChannel,
            speakerController = speakerController,
            consentStore = consentStore,
            coroutineScope = scope
        )
        controller?.start()

        // TODO: Observe controller state and update notification

        return START_STICKY
    }

    override fun onDestroy() {
        controller?.stop()
        job.cancel()
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Privacy Call", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("SpeakPriv")
            .setContentText("Monitoring call privacy...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Placeholder icon
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val EXTRA_CALL_ID = "extra_call_id"
        private const val NOTIFICATION_ID = 1337
        private const val CHANNEL_ID = "PrivacyCallServiceChannel"
    }
}
