# 🎓 Ram Academy — Rebuilt in Modern Kotlin

A complete rebuild of the Ram Academy Android app using **modern Kotlin + Jetpack Compose**, with the exact design language from the GKK-APP (Navy `#1A237E` × Saffron `#FF6B00` × Gold `#F9A825`).

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Language | **Kotlin** |
| UI | **Jetpack Compose + Material3** |
| Architecture | **MVVM + Hilt (DI)** |
| Navigation | **Compose Navigation** |
| Networking | **Retrofit + OkHttp** |
| Auth | **Firebase Auth** |
| Database | **Firestore + Room (local cache)** |
| Video | **ExoPlayer (Media3)** |
| Notifications | **FCM** |
| Image Loading | **Coil** |
| Fonts | **DM Sans + Syne** (matching GKK-APP) |

---

## 🎨 Design Theme (from GKK-APP)

```
--navy:   #1A237E   (primary / header / buttons)
--saff:   #FF6B00   (accent / CTAs / active nav)
--gold:   #F9A825   (highlights / streaks / badges)
--bg:     #F4F6FB   (background)
--text:   #1E293B   (body text)
--muted:  #64748B   (secondary text)
```

---

## 📱 Features (from Ram Academy APK)

- ✅ **Authentication** — Login / Register / OTP / Forgot Password
- ✅ **Home Dashboard** — Stats, Quick Actions, Featured Courses, Live Now, Current Affairs
- ✅ **Video Courses** — Course list, detail, chapter accordion, ExoPlayer / YouTube
- ✅ **Test Series** — Full quiz engine with timer, question panel, flag, bookmark
- ✅ **Test Results** — Score circle, grade, review answers, analysis bars
- ✅ **Daily Quiz** — 10 questions per day streak builder
- ✅ **Study Material** — PDFs, e-books, notes by subject
- ✅ **Current Affairs** — Articles with category filter
- ✅ **Live Classes** — YouTube live + recorded classes
- ✅ **Doubts** — Post doubt with image, view answers, comments
- ✅ **Downloads** — Offline lecture/PDF manager
- ✅ **Faculty** — Teacher profiles, courses, ratings
- ✅ **Timetable** — Class schedule
- ✅ **Notifications** — FCM push notifications
- ✅ **Alarms** — Study reminders
- ✅ **Store / Cart / Checkout** — Course purchase with EMI
- ✅ **Referral** — Share & earn
- ✅ **Settings** — Dark mode, notifications, privacy
- ✅ **Profile** — Edit, avatar, stats

---

## 🚀 Setup

### 1. Clone & Open
```bash
git clone <your-repo>
# Open in Android Studio Hedgehog or newer
```

### 2. Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a project → Add Android app → package `com.ramacademy.app`
3. Download `google-services.json` → place in `app/`
4. Enable: **Authentication** (Phone + Email), **Firestore**, **Messaging**

### 3. Backend API
Edit `AppModule.kt`:
```kotlin
private const val BASE_URL = "https://your-backend.com/api/v1/"
```

### 4. Fonts
Download and add to `app/src/main/res/font/`:
- `dm_sans_regular.ttf`
- `dm_sans_medium.ttf`
- `dm_sans_semibold.ttf`
- `dm_sans_bold.ttf`
- `syne_bold.ttf`
- `syne_extrabold.ttf`

Get from: https://fonts.google.com/specimen/DM+Sans and https://fonts.google.com/specimen/Syne

### 5. Build
```bash
./gradlew assembleDebug
```

---

## 📂 Project Structure

```
app/src/main/java/com/ramacademy/app/
├── RamAcademyApp.kt           # Application class + notification channels
├── MainActivity.kt            # Entry point, edge-to-edge, theme
├── di/
│   └── AppModule.kt           # Hilt DI — Retrofit, Firebase, OkHttp
├── data/
│   └── model/
│       └── Models.kt          # All data classes
├── ui/
│   ├── Navigation.kt          # Screen sealed class (all routes)
│   ├── NavHost.kt             # Full nav graph with animations
│   ├── theme/
│   │   ├── Theme.kt           # GKK-APP colors in Compose
│   │   └── Typography.kt      # DM Sans + Syne type scale
│   ├── components/
│   │   └── Components.kt      # Reusable: TopBar, BottomNav, Cards, Buttons
│   └── screens/
│       ├── HomeScreen.kt      # Main dashboard
│       ├── LoginScreen.kt     # Login
│       ├── RegisterScreen.kt  # Register
│       ├── AuthScreens.kt     # Splash, OTP, Forgot Password
│       ├── CourseDetailScreen.kt
│       ├── TestAttemptScreen.kt  # Full quiz engine
│       ├── TestResultScreen.kt
│       └── OtherScreens.kt    # All other screens
├── viewmodel/
│   └── ViewModels.kt          # Auth, Home, Course, Test, Profile VMs
├── service/
│   └── Services.kt            # FCM + Download Service
└── receiver/
    └── AlarmReceiver.kt       # Study alarm notifications
```

---

## 🔌 Connecting Your Backend

Each ViewModel has `TODO` comments showing exactly where to plug in your API calls:

```kotlin
// In AuthViewModel.login():
val result = authRepository.login(phoneOrEmail, password)

// In HomeViewModel.loadHome():
val courses = courseRepository.getFeaturedCourses()

// In TestViewModel.loadTest():
val questions = testRepository.getQuestions(testId)
```

Create Repository classes that wrap your Retrofit API calls and Firestore queries.

---

## 📦 Build & Release

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (sign with your keystore)
./gradlew assembleRelease
```

---

Built with ❤️ for Ram Academy students.
