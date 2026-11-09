package com.callhistory.app

import android.app.role.RoleManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val roleRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val rm = if (Build.VERSION.SDK_INT >= 29) getSystemService(RoleManager::class.java) else null
                val enabled = rm?.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) == true && rm.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
                Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Call History", style = MaterialTheme.typography.headlineLarge)
                    Text(if (enabled) "Call reporting enabled" else "Enable call reporting to receive supported call events")
                    Button(onClick = { if (rm != null && !enabled) roleRequest.launch(rm.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)) }, enabled = rm != null && !enabled) {
                        Text(if (enabled) "Enabled" else "Enable call reporting")
                    }
                    Text("The app does not block or reject calls.")
                }
            }
        }
    }
}
