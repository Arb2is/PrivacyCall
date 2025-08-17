package com.speakpriv.android.implementations

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.speakpriv.api.InviteSharer
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class InviteSharerAndroid(private val context: Context) : InviteSharer {

    override fun share() {
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://speakpriv.app/invite")
            domainUriPrefix = "https://example.page.link"
            androidParameters("com.speakpriv.android") { }
            socialMetaTagParameters {
                title = "SpeakPriv"
                description = "Secure your calls with SpeakPriv"
            }
        }.addOnSuccessListener { shortLink ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shortLink.shortLink.toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(shareIntent)
        }
    }
}
