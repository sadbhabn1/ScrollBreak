# ScrollBreak 🛑📱

**ScrollBreak** is an Android application designed to help users break the cycle of doomscrolling and regain focus. Instead of permanently blocking apps, ScrollBreak acts as an active intervention system. It tracks your time in distracting apps and, when you exceed your limit, forcefully pulls you out. To get back in, you have to earn your time back through productive tasks!

Built as a **Hackathon MVP**, ScrollBreak demonstrates a seamless integration of modern Android UI and robust background service tracking.

## ✨ Features

- **App Tracking:** Uses Android's `UsageStatsManager` in a Foreground Service to monitor how long you spend in distracting apps (like Instagram, TikTok, Chrome) without draining the battery.
- **Forceful Interventions:** Automatically kicks you out of the distracting app and traps you in a "Cooldown Screen" using system overlays and high-priority intents.
- **Earn Your Time:** Don't want to wait out the cooldown? Complete physical or productive micro-tasks (like doing 10 push-ups, drinking water, or reading 2 pages of a book) to immediately earn back screen time.
- **Seamless Return:** Once a task is completed, ScrollBreak instantly minimizes itself, dropping you right back into the app you were using.
- **Premium Aesthetics:** Built entirely with Jetpack Compose featuring smooth animations, glassmorphic UI elements, and a tailored pastel dark-mode design palette.

## 🛠️ Tech Stack

- **Language:** 100% Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Background Work:** Kotlin Coroutines & Foreground Services
- **System APIs:** `UsageStatsManager` (for app tracking), `SYSTEM_ALERT_WINDOW` (for app hijacking)
- **Build System:** Gradle (Kotlin DSL)

## 🚀 How it Works

1. **Setup:** Select the apps you want to restrict and set an "Allowed App Time" (e.g., 10 minutes).
2. **Background Monitoring:** ScrollBreak runs quietly in the background tracking your active screen time in the selected apps.
3. **The Block:** When your time is up, a high-priority notification triggers, your phone vibrates, and the ScrollBreak Cooldown screen forces its way to the front of your device.
4. **The Choice:** Wait out the cooldown timer, or select an "Earn Time" task.
5. **Reward:** Complete the task, claim your reward time, and the app seamlessly returns you to where you left off.

---
*Built for the Hackathon.*
