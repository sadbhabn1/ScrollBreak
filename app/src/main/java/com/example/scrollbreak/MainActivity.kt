package com.example.scrollbreak

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.scrollbreak.model.SessionManager
import com.example.scrollbreak.ui.AppRoot
import com.example.scrollbreak.ui.theme.ScrollBreakTheme

class MainActivity : ComponentActivity() {
    private var forceCooldown by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        setContent {
            ScrollBreakTheme {
                AppRoot(forceCooldown = forceCooldown)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("START_COOLDOWN", false) == true) {
            forceCooldown = true
            intent.removeExtra("START_COOLDOWN")
        }
    }
}