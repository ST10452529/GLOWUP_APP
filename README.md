# GlowUp — Personal Skincare Companion Android Application 

GlowUp is a feature-complete, modern Android application developed in Kotlin to help users organize, monitor, and manage their daily skincare routines, products, and progress in one convenient place.

---

## Key Features & Requirements Matrix

| Requirement | Implementation Details |
| :--- | :--- |
| **Authentication & Password Encryption** | User registration and secure authentication managed via Firebase Authentication with password encryption. |
| **Single Sign-On (SSO)** | Google Sign-In integration (`btnGoogleSignIn`) on Login & Registration screens. |
| **Settings Management** | Interactive Settings panel in `ProfileActivity` supporting Dark Mode toggle, Skin Type selection, and Gender preference (Male, Female, Non-binary, Unisex). |
| **REST API Integration** | Open Beauty Facts REST API integrated via Retrofit 2 (`OpenBeautyFactsApi`) and Glide for product image caching. |
| **Offline Mode & Local Storage** | `FirestoreRepository` provides local caching via SharedPreferences and JSON data persistence for offline functionality with live cloud sync. |
| **Real-time Push Notifications** | `GlowNotificationManager` sends real-time system alerts and daily routine reminders via Android Notification Channels. |
| **Multi-language Support** | Supports South African official languages: **English (`en`)**, **isiZulu (`zu`)**, and **Sesotho (`st`)** with dynamic in-app language switching. |
| **Automated Testing & CI/CD** | GitHub Actions workflow (`.github/workflows/android.yml`) automatically compiles and executes unit tests (`GlowUpUnitTest.kt`). |

---

## Architecture & Tech Stack

- **Language**: Kotlin 100%
- **Architecture**: Repository Pattern with MVVM / ViewBinding
- **UI Framework**: Material Design 3 Components & XML Layouts
- **Backend / Storage**: Firebase Authentication, Cloud Firestore, SharedPreferences
- **Networking**: Retrofit 2 + Gson Converter + OkHttp Logging Interceptor
- **Image Loading**: Glide
- **CI/CD**: GitHub Actions

---

## Release Notes (Final PoE Version)

### Version 1.0.0 (Final PoE Release)
- **Firebase Authentication & Google SSO**: Integrated Firebase Auth with encrypted password verification and Google Single Sign-On.
- **Open Beauty Facts REST API**: Product search screen connecting to Open Beauty Facts API with instant search and "+ Add to Routine" action.
- **Offline Storage & Sync**: Local database repository allowing users to manage skincare steps and diary notes seamlessly even without internet access.
- **Multi-language Support**: Added full localized string resources for South African official languages **isiZulu (`values-zu`)** and **Sesotho (`values-st`)**.
- **Real-time Notifications**: Added `GlowNotificationManager` for instant skincare alerts and daily routine notifications.
- **Dark Mode & Profile Editing**: Added Dark/Night theme toggle and full profile editing (Name, Gender, Skin Type).

---

## AI Tools Write-Up & Citation (PoE Assessment)

During the development of the **GlowUp** application, AI development tools (Android Studio AI Agent & Large Language Models) were utilized to assist in code generation, debugging, and architectural refinement. 

### Usage & Implementation:
1. **Gradle Dependency Resolution**: AI tools helped resolve build errors involving Android Gradle Plugin 9.0 compatibility, compileSdk configuration, and `google-services.json` package matching.
2. **REST API Data Modeling**: AI tools assisted in mapping Open Beauty Facts JSON payloads into typed Kotlin data classes (`OpenBeautyFactsResponse`, `ApiProduct`).
3. **Multi-language Translation Verification**: AI tools assisted in verifying accurate isiZulu and Sesotho string translations for skincare domain terminology (`values-zu` and `values-st`).
4. **Code Quality & Testing**: AI tools helped generate comprehensive JUnit test cases (`GlowUpUnitTest.kt`) testing data models and repository logic.


