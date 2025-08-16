package com.speakpriv.android.implementations

import android.content.Context
import android.content.Intent
import com.speakpriv.api.InviteSharer

class InviteSharerAndroid(private val context: Context) : InviteSharer {

    override fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out SpeakPriv for secure calls: [APP_URL]") // Replace with actual URL
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
