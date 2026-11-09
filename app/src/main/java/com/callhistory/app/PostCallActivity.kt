package com.callhistory.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("call_history", MODE_PRIVATE)
        val number = intent.getParcelableExtra<Uri>(TelecomManager.EXTRA_HANDLE)?.schemeSpecificPart
            ?: prefs.getString("number", "").orEmpty()
        val direction = prefs.getString("direction", "Call").orEmpty()
        val duration = intent.getIntExtra(TelecomManager.EXTRA_CALL_DURATION, 0).coerceAtLeast(0)
        val today = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val sameDay = prefs.getString("day", "") == today
        val count = (if (sameDay) prefs.getInt("count", 0) else 0) + 1
        val total = (if (sameDay) prefs.getInt("total", 0) else 0) + duration
        prefs.edit().putString("day", today).putInt("count", count).putInt("total", total).apply()
        setContent {
            MaterialTheme {
                ReportPopup(number, direction, duration, count, total,
                    onClose = { finish() },
                    onCallBack = { if (number.isNotBlank()) startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(number)}"))) })
            }
        }
    }
}

@Composable
private fun ReportPopup(number: String, direction: String, duration: Int, count: Int, total: Int, onClose: () -> Unit, onCallBack: () -> Unit) {
    var timer by remember { mutableIntStateOf(5) }
    LaunchedEffect(Unit) { while (timer > 0) { delay(1000); timer-- } }
    Surface(Modifier.fillMaxSize(), color = Color(0x99000000)) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(Modifier.fillMaxWidth().padding(18.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("CALL REPORT", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Box(Modifier.size(10.dp).background(Color(0xFF20B86A), RoundedCornerShape(50)))
                        Spacer(Modifier.weight(1f)); Text("$timer", color = Color.Gray)
                    }
                    HorizontalDivider()
                    Text("Caller: ${if (number.isBlank()) "Unknown caller" else number}", style = MaterialTheme.typography.titleMedium)
                    Text(number.ifBlank { "Number unavailable" })
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("TODAY'S TOTAL"); Text(formatTime(total), style = MaterialTheme.typography.titleLarge) }
                        Column { Text("CALL COUNT"); Text("$count", style = MaterialTheme.typography.titleLarge) }
                    }
                    HorizontalDivider()
                    Text("LAST CALL", fontWeight = FontWeight.Bold)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(direction); Text(formatTime(duration))
                    }
                    Text(SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date()), color = Color.Gray)
                    AndroidViewBanner()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = onClose, modifier = Modifier.weight(1f)) { Text("Close") }
                        Button(onClick = onCallBack, modifier = Modifier.weight(1f)) { Text("Call Back") }
                    }
                }
            }
        }
    }
}
private fun formatTime(s: Int) = "%02d:%02d".format(s / 60, s % 60)

@Composable
private fun AndroidViewBanner() {
    androidx.compose.ui.viewinterop.AndroidView(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        factory = { ctx -> com.google.android.gms.ads.AdView(ctx).apply {
            setAdSize(com.google.android.gms.ads.AdSize.BANNER)
            adUnitId = "ca-app-pub-3940256099942544/9214589741"
            loadAd(com.google.android.gms.ads.AdRequest.Builder().build())
        } }
    )
}
