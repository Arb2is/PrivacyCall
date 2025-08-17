package com.speakpriv.android.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.speakpriv.model.Exposure
import com.speakpriv.model.SpeakUiState

@Composable
fun SpeakFreelyScreen(
    state: SpeakUiState,
    onToggleSpeaker: () -> Unit,
    onShare: () -> Unit,
    onEndCall: () -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onToggleLed: (Boolean) -> Unit
) {
    val haloColor = when (state.local) {
        Exposure.EXPOSED -> Color(0xFFEF4444)
        Exposure.PRIVATE -> Color(0xFF22C55E)
        Exposure.UNVERIFIED -> Color(0xFFFACC15)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SpeakFreely", style = MaterialTheme.typography.titleLarge)
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = Color(0xFF2DD4BF)
            )
        }

        val message = when (state.local) {
            Exposure.EXPOSED -> "Your speakerphone is on.\nOthers may hear your conversation."
            Exposure.PRIVATE -> "Privacy protected.\nOnly you can hear."
            Exposure.UNVERIFIED -> "Not verified yet.\nShare protection to invite."
        }
        Text(message, style = MaterialTheme.typography.bodyMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(color = haloColor, style = Stroke(width = 8f))
                }
                Icon(
                    imageVector = Icons.Filled.Headset,
                    contentDescription = null,
                    tint = haloColor,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(state.contactName, style = MaterialTheme.typography.titleMedium)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(onClick = {}) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Sound", color = if (state.soundEnabled) Color(0xFFEF4444) else Color.Unspecified)
                }
            }
            Button(onClick = { if (state.local == Exposure.UNVERIFIED) onShare() else onToggleSpeaker() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))) {
                val text = when (state.local) {
                    Exposure.EXPOSED -> "Turn Off Speakerphone"
                    Exposure.PRIVATE -> "Turn On Speakerphone"
                    Exposure.UNVERIFIED -> "Share protection"
                }
                Text(text)
            }
        }

        Divider()
        Text("Alert settings", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Sound", modifier = Modifier.weight(1f))
            Switch(checked = state.soundEnabled, onCheckedChange = onToggleSound)
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Vibration", modifier = Modifier.weight(1f))
            Switch(checked = state.vibrationEnabled, onCheckedChange = onToggleVibration)
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Luminosity / LED", modifier = Modifier.weight(1f))
            Switch(checked = state.ledEnabled, onCheckedChange = onToggleLed)
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onEndCall,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                modifier = Modifier.weight(1f)
            ) { Text("End Call") }
            Button(
                onClick = onShare,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                modifier = Modifier.weight(1f)
            ) { Text("Share protection") }
        }
    }
}
