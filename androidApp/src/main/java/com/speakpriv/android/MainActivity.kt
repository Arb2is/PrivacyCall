package com.speakpriv.android

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.speakpriv.api.ConsentStore
import com.speakpriv.android.implementations.AlertPrefs
import com.speakpriv.android.implementations.ConsentStorePrefs
import com.speakpriv.android.implementations.InviteSharerAndroid
import com.speakpriv.android.implementations.SpeakerphoneControllerAndroid
import com.speakpriv.android.ui.SpeakFreelyScreen
import com.speakpriv.android.ui.SpeakPrivTheme
import com.speakpriv.model.Exposure
import com.speakpriv.model.SpeakUiState
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : ComponentActivity() {

    private lateinit var consentStore: ConsentStore

    private val roleRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            consentStore.setGranted(true)
            FirebaseCrashlytics.getInstance().setCustomKey("roleGranted", true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consentStore = ConsentStorePrefs(this)

        setContent {
            SpeakPrivTheme {
                val prefs = AlertPrefs(this)
                val speaker = SpeakerphoneControllerAndroid(this)
                val sharer = InviteSharerAndroid(this)
                val initialState = SpeakUiState(
                    local = if (speaker.isSpeakerOn()) Exposure.EXPOSED else Exposure.PRIVATE,
                    contactName = "Contact",
                    soundEnabled = prefs.soundEnabled,
                    vibrationEnabled = prefs.vibrationEnabled,
                    ledEnabled = prefs.ledEnabled
                )
                val state = remember { mutableStateOf(initialState) }

                if (consentStore.isGranted()) {
                    SpeakFreelyScreen(
                        state = state.value,
                        onToggleSpeaker = {
                            val new = !speaker.isSpeakerOn()
                            speaker.setSpeaker(new)
                            state.value = state.value.copy(local = if (new) Exposure.EXPOSED else Exposure.PRIVATE)
                        },
                        onShare = { sharer.share() },
                        onEndCall = { /* stop service */ },
                        onToggleSound = {
                            prefs.soundEnabled = it
                            state.value = state.value.copy(soundEnabled = it)
                        },
                        onToggleVibration = {
                            prefs.vibrationEnabled = it
                            state.value = state.value.copy(vibrationEnabled = it)
                        },
                        onToggleLed = {
                            prefs.ledEnabled = it
                            state.value = state.value.copy(ledEnabled = it)
                        }
                    )
                } else {
                    MainScreen(false) { requestRole() }
                }
            }
        }
    }

    private fun requestRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            roleRequest.launch(intent)
        } else {
            // For older versions, consent is granted implicitly by installing
            consentStore.setGranted(true)
        }
    }
}

@Composable
fun MainScreen(isConsentGranted: Boolean, onRequest: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isConsentGranted) {
            Text("SpeakPriv is active.")
        } else {
            Button(onClick = onRequest) {
                Text("Enable Protection")
            }
        }
    }
}
