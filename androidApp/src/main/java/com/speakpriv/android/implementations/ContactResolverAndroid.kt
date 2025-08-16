package com.speakpriv.android.implementations

import android.content.Context
import android.provider.ContactsContract
import com.speakpriv.api.ContactResolver
import com.speakpriv.model.ContactUi

class ContactResolverAndroid(private val context: Context) : ContactResolver {

    override fun forNumber(e164Number: String?): ContactUi? {
        if (e164Number == null) return null

        val uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(e164Number).build()
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME))
                val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.PHOTO_URI))
                return ContactUi(name, photoUri)
            }
        }
        return null
    }
}
