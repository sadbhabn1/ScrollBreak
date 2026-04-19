package com.example.scrollbreak.ui

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.scrollbreak.model.Screen
import com.example.scrollbreak.model.SessionManager
import com.example.scrollbreak.service.AppTrackerService
import com.example.scrollbreak.ui.screens.*

@Composable
fun AppRoot(forceCooldown: Boolean = false) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var settings by remember { mutableStateOf(SessionManager.settings) }
    var earnedMinutes by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(forceCooldown) {
        if (forceCooldown) {
            currentScreen = Screen.Cooldown
        }
    }

    when (currentScreen) {
        Screen.Home -> HomeScreen(
            onGetStarted = { currentScreen = Screen.Permissions }
        )

        Screen.Permissions -> PermissionsScreen(
            onAllPermissionsGranted = { currentScreen = Screen.AppSelection }
        )

        Screen.AppSelection -> AppSelectionScreen(
            selectedIds = settings.selectedAppIds,
            onSelectionChanged = { ids -> 
                settings = settings.copy(selectedAppIds = ids)
                SessionManager.settings = settings
            },
            onBack = { currentScreen = Screen.Home },
            onNext = { currentScreen = Screen.LimitSetup }
        )

        Screen.LimitSetup -> LimitSetupScreen(
            settings = settings,
            onSettingsChanged = { updated -> 
                settings = updated
                SessionManager.settings = updated
            },
            onBack = { currentScreen = Screen.AppSelection },
            onStartFocus = {
                SessionManager.resetSession()
                SessionManager.isTracking = true
                val intent = Intent(context, AppTrackerService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
                currentScreen = Screen.TrackingActive
            }
        )

        Screen.TrackingActive -> TrackingActiveScreen(
            onStopTracking = { currentScreen = Screen.LimitSetup }
        )

        Screen.Cooldown -> CooldownScreen(
            cooldownMinutes = settings.cooldownMinutes,
            onEarnTime = { currentScreen = Screen.EarnTime },
            onCooldownComplete = { currentScreen = Screen.SessionComplete }
        )

        Screen.EarnTime -> EarnTimeScreen(
            rewardMinutes = settings.rewardMinutes,
            onTaskCompleted = { bonus ->
                earnedMinutes = bonus
                
                // 1. Give them more time
                val bonusMillis = bonus * 60 * 1000L
                SessionManager.accumulatedMillis = (SessionManager.accumulatedMillis - bonusMillis).coerceAtLeast(0L)
                
                // 2. Restart tracking
                SessionManager.isCooldownActive = false
                SessionManager.isTracking = true
                val intent = Intent(context, AppTrackerService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
                
                // 3. Set the screen to tracking so when they open ScrollBreak again it's correct
                currentScreen = Screen.TrackingActive
                
                // 4. Minimize the app to seamlessly return to what they were doing
                (context as? android.app.Activity)?.moveTaskToBack(true)
            },
            onBack = { currentScreen = Screen.Cooldown }
        )

        Screen.SessionComplete -> SessionCompleteScreen(
            settings = settings,
            onRestart = {
                SessionManager.resetSession()
                settings = SessionManager.settings
                currentScreen = Screen.Home
            }
        )
    }
}
