package com.example.scrollbreak.model

import androidx.compose.ui.graphics.Color

// ── Navigation ──────────────────────────────────────────────────────────────

enum class Screen {
    Home,
    Permissions,
    AppSelection,
    LimitSetup,
    TrackingActive,
    Cooldown,
    EarnTime,
    SessionComplete
}

// ── Distracting apps ────────────────────────────────────────────────────────

data class DistractingApp(
    val id: String,
    val name: String,
    val emoji: String,
    val accentColor: Color,
    val packageName: String
)

val allDistractingApps = listOf(
    DistractingApp("instagram", "Instagram",  "📸", Color(0xFFE1306C), "com.android.settings"),
    DistractingApp("youtube",   "YouTube",    "▶️", Color(0xFFFF0000), "com.google.android.youtube"),
    DistractingApp("chrome",    "Chrome",     "🌐", Color(0xFFF4B400), "com.android.chrome"),
    DistractingApp("tiktok",    "TikTok",     "🎵", Color(0xFF69C9D0), "com.zhiliaoapp.musically"),
    DistractingApp("twitter",   "X / Twitter","🐦", Color(0xFF1DA1F2), "com.twitter.android"),
    DistractingApp("snapchat",  "Snapchat",   "👻", Color(0xFFFFFC00), "com.snapchat.android"),
    DistractingApp("facebook",  "Facebook",   "📘", Color(0xFF1877F2), "com.facebook.katana"),
    DistractingApp("reddit",    "Reddit",     "🤖", Color(0xFFFF4500), "com.reddit.frontpage"),
    DistractingApp("pinterest", "Pinterest",  "📌", Color(0xFFE60023), "com.pinterest"),
    DistractingApp("linkedin",  "LinkedIn",   "💼", Color(0xFF0A66C2), "com.linkedin.android"),
    DistractingApp("twitch",    "Twitch",     "🎮", Color(0xFF9146FF), "tv.twitch.android.app"),
    DistractingApp("bereal",    "BeReal",     "📷", Color(0xFF000000), "com.bereal.ft"),
    DistractingApp("threads",   "Threads",    "🧵", Color(0xFF101010), "com.instagram.barcelona"),
    DistractingApp("telegram",  "Telegram",   "✈️", Color(0xFF2CA5E0), "org.telegram.messenger"),
    DistractingApp("discord",   "Discord",    "🎧", Color(0xFF5865F2), "com.discord"),
    DistractingApp("tumblr",    "Tumblr",     "📝", Color(0xFF35465C), "com.tumblr")
)

// ── Session settings ─────────────────────────────────────────────────────────

data class SessionSettings(
    val selectedAppIds: Set<String> = emptySet(),
    val allowedAppMinutes: Int = 10,
    val cooldownMinutes: Int = 5,
    val rewardMinutes: Int = 10
)

// ── Shared State (Memory-backed for Hackathon) ───────────────────────────────

object SessionManager {
    var settings = SessionSettings()
    var accumulatedMillis: Long = 0L
    var isTracking: Boolean = false
    var isCooldownActive: Boolean = false

    fun resetSession() {
        accumulatedMillis = 0L
        isTracking = false
        isCooldownActive = false
    }
}

// ── Earn-time tasks ──────────────────────────────────────────────────────────

data class ProductivityTask(
    val id: String,
    val label: String,
    val emoji: String,
    val durationSeconds: Int   // used for the task's own countdown if needed
)

val allProductivityTasks = listOf(
    ProductivityTask("water",    "Drink a glass of water", "💧", 30),
    ProductivityTask("stretch",  "Stretch for 60 seconds", "🧘", 60),
    ProductivityTask("pushups",  "Do 10 push-ups",         "💪", 60),
    ProductivityTask("desk",     "Clean your desk",        "🗂️", 120),
    ProductivityTask("read",     "Read 2 pages of a book", "📖", 180),
)
