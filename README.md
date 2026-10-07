# RuqaiyaPro - Boss Rubel's Personal AI Assistant & Agent

**Package:** `com.ruqaiyapro`  
**Target:** Android 14 (API 34), Optimized for Infinix Hot 12 Play (Unisoc T610)  
**Architecture:** Jetpack Compose + CameraX + ML Kit Face Detection 16.1.5 + Vosk Offline + SQLite Khata + AccessibilityService

---

## 🚀 How to Build APK in Android Studio or A-IDE

### Option 1: Android Studio (PC / Mac / Linux)
1. Unzip `RuqaiyaPro.zip`.
2. Open Android Studio -> **File** -> **Open** -> Select the unzipped `RuqaiyaPro` folder.
3. Allow Gradle Sync to finish downloading dependencies (Jetpack Compose, CameraX, ML Kit 16.1.5, Vosk, WorkManager).
4. Connect your **Infinix Hot 12 Play** via USB with **USB Debugging** enabled.
5. Click **Run 'app'** (Green play button) or **Build** -> **Build Bundle(s) / APK(s)** -> **Build APK(s)**.

### Option 2: A-IDE (Directly on Android Phone)
1. Extract `RuqaiyaPro.zip` into `/sdcard/AppProjects/RuqaiyaPro`.
2. Open **A-IDE** or **AndroidIDE** app on your phone.
3. Open project `RuqaiyaPro`.
4. Tap **Build APK**.

---

## 🌟 Mandatory Google-Like Behavior Features
1. **Background Mini Mic (`RuqaiyaHotwordService`)**:
   - Runs as `ForegroundService` with `START_STICKY`.
   - Low priority notification: *"Ruqaiya ghumacche... Bolo Hey Ruqaiya"*.
   - 80dp Chibi overlay floating at 0.20 alpha when sleeping.
2. **No-Beep Trick**:
   - Mutes all 6 streams before `startListening` (`STREAM_MUSIC`, `STREAM_SYSTEM`, `STREAM_NOTIFICATION`, `STREAM_ALARM`, `STREAM_RING`, `STREAM_DTMF`).
   - 500ms delay to start.
   - On results/errors: un-mutes and 1500ms delay restart with no crash loop.
3. **Google-Style Wake**:
   - Listens for *"hey ruqaiya"*, *"hey rukaiya"*, *"he ruqaiya"*.
   - 50ms vibration + Chibi bounces from 80dp to 320dp with alpha 1.0 glow!
   - Speaks *"Ji Rubel Boss bolo"* and pops Google-style HUD toasts.
4. **WhatsApp Automation**:
   - Auto reply: *"Boss Rubel busy ache, pore reply dibe - Ruqaiya bolchi"*.
   - Broadcast loop with safe 3-second delay per contact.
5. **CameraX Face Recognition & Auto Blur**:
   - Checks Boss Rubel face (>70% threshold).
   - Unknown intruder face automatically blurred 80% with RenderScript!
