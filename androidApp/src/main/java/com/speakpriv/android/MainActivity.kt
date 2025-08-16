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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.speakpriv.api.ConsentStore
import com.speakpriv.android.implementations.ConsentStorePrefs

class MainActivity : ComponentActivity() {

    private lateinit var consentStore: ConsentStore

    private val roleRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            consentStore.setGranted(true)
            // You can add a toast or a message here to confirm
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consentStore = ConsentStorePrefs(this)

        setContent {
            MaterialTheme {
                MainScreen(consentStore.isGranted()) { requestRole() }
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
