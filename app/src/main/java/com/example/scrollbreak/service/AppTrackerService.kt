package com.example.scrollbreak.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.scrollbreak.MainActivity
import com.example.scrollbreak.model.SessionManager
import com.example.scrollbreak.model.allDistractingApps
import kotlinx.coroutines.*

class AppTrackerService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    
    companion object {
        const val CHANNEL_ID = "AppTrackerChannel"
        const val NOTIFICATION_ID = 1
        const val ALERT_NOTIFICATION_ID = 2
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ScrollBreak Tracking Active")
            .setContentText("Monitoring your usage of restricted apps.")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // using default icon for MVP
            .build()
            
        startForeground(NOTIFICATION_ID, notification)
        startTracking()
        
        return START_STICKY
    }

    private fun startTracking() {
        scope.launch {
            val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val restrictedPackages = allDistractingApps
                .filter { SessionManager.settings.selectedAppIds.contains(it.id) }
                .map { it.packageName }
                .toSet()

            var lastCheckTime = System.currentTimeMillis()
            var currentForegroundApp: String? = null

            while (isActive) {
                if (!SessionManager.isTracking) {
                    stopSelf()
                    break
                }

                val now = System.currentTimeMillis()
                val events = usageStatsManager.queryEvents(now - 10000, now)
                val event = UsageEvents.Event()

                // Find the latest MOVE_TO_FOREGROUND event
                while (events.hasNextEvent()) {
                    events.getNextEvent(event)
                    if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        currentForegroundApp = event.packageName
                    } else if (event.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND && event.packageName == currentForegroundApp) {
                        currentForegroundApp = null
                    }
                }

                // If a restricted app is in the foreground, accumulate time
                if (currentForegroundApp != null && restrictedPackages.contains(currentForegroundApp)) {
                    val delta = now - lastCheckTime
                    SessionManager.accumulatedMillis += delta
                    
                    val limitMillis = SessionManager.settings.allowedAppMinutes * 60 * 1000L
                    if (SessionManager.accumulatedMillis >= limitMillis) {
                        triggerCooldown()
                        stopSelf() // Stop tracking since cooldown started
                        break
                    }
                }

                lastCheckTime = now
                delay(1000)
            }
        }
    }

    private fun triggerCooldown() {
        SessionManager.isCooldownActive = true
        SessionManager.isTracking = false

        // Send a High-Priority Notification with Vibration
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alertNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Time's Up!")
            .setContentText("You have used the application for enough time now, time to focus on things that matter.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setAutoCancel(true)
            .build()
        notificationManager.notify(ALERT_NOTIFICATION_ID, alertNotification)

        // Launch MainActivity directly into Cooldown mode
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("START_COOLDOWN", true)
        }
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Tracker Service",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
