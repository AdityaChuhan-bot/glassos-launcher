# GlassOS Launcher

A custom Android home launcher with Liquid Glass UI design system, Dynamic Island, Control Center, and advanced gesture support.

## Features

### Core Launcher
- ✅ Acts as Android HOME launcher
- ✅ Horizontal page scrolling home screen
- ✅ Configurable 4x4 grid workspace
- ✅ App launching support

### App Drawer
- ✅ Full-screen app drawer accessible via swipe up
- ✅ Grid layout of installed apps (4 columns)
- ✅ Search functionality with real-time filtering
- ✅ Alphabetical sorting

### Liquid Glass UI System
- ✅ Glass morphism design with blur effects
- ✅ RenderEffect-based blur (Android 12+)
- ✅ Transparent tint overlays
- ✅ Rounded corners
- ✅ Reusable components:
  - `GlassSurface` - Main glass container
  - `GlassCard` - Compact glass card
  - `GlassDock` - Bottom dock with glass effect
  - `GlassButton` - Interactive glass button
  - `GlassSlider` - Control sliders with glass effect

### Dynamic Island
- ✅ Floating pill UI at top of screen
- ✅ Collapsed and expanded states
- ✅ Battery status display
- ✅ Media playback information
- ✅ Animated expand/collapse transitions

### Control Center
- ✅ iOS-style control panel (swipe-down)
- ✅ Quick toggles: WiFi, Bluetooth, Flashlight
- ✅ Brightness slider
- ✅ Volume slider
- ✅ Media playback controls
- ✅ Glass UI cards for all controls

### Widgets
- ✅ Clock Widget - Real-time clock and day display
- ✅ Weather Widget - Temperature and condition
- ✅ Battery Widget - Battery level with charging status
- ✅ Music Widget - Now playing information
- ✅ Search Widget - Quick search access

### Gesture System
- ✅ Swipe up - Open app drawer
- ✅ Double tap - Lock screen
- ✅ Tap on island - Expand/collapse
- ✅ Long press on app - App options (framework ready)

### Performance
- ✅ Optimized for 60fps operation
- ✅ Efficient recomposition prevention
- ✅ Async app loading
- ✅ Smooth animations

## Project Structure

```
glassos-launcher/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── java/com/glassos/launcher/
│           │   ├── ui/
│           │   │   └── LauncherActivity.kt
│           │   ├── models/
│           │   │   └── Models.kt
│           │   ├── managers/
│           │   │   ├── AppManager.kt
│           │   │   └── LauncherStateManager.kt
│           │   ├── components/
│           │   │   ├── GlassUIComponents.kt
│           │   │   ├── Widgets.kt
│           │   │   ├── DynamicIsland.kt
│           │   │   ├── ControlCenter.kt
│           │   │   ├── AppDrawer.kt
│           │   │   └── HomeScreen.kt
│           │   └── theme/
│           │       ├── Color.kt
│           │       ├── Theme.kt
│           │       └── Typography.kt
│           ├── res/
│           │   ├── values/
│           │   │   ├── strings.xml
│           │   │   ├── colors.xml
│           │   │   ├── themes.xml
│           │   │   └── attrs.xml
│           │   ├── xml/
│           │   │   ├── data_extraction_rules.xml
│           │   │   └── backup_scheme.xml
│           │   ├── drawable/
│           │   ├── mipmap-xhdpi/
│           │   └── layout/
│           └── AndroidManifest.xml
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
```

## Build Requirements

- **Android Studio**: Latest stable version (Iguana or newer)
- **Java**: JDK 17 or newer
- **Android SDK**: API 34+ (for development)
- **Min SDK**: API 26 (Android 8.0)
- **Target SDK**: API 34 (Android 14)

## Building the Project

### Option 1: Using Android Studio

1. **Clone or open the project**
   ```bash
   cd /workspaces/glassos-launcher
   ```

2. **Open in Android Studio**
   - File → Open → Select the project directory
   - Android Studio will automatically detect and load the Gradle configuration

3. **Sync Gradle**
   - Android Studio will prompt you to sync Gradle
   - Click "Sync Now" and wait for completion

4. **Build the APK**
   - Select "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
   - Wait for the build to complete
   - APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Using Command Line

```bash
# Navigate to project root
cd /workspaces/glassos-launcher

# Grant execute permission to gradlew (Linux/Mac)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug
# Result: app/build/outputs/apk/debug/app-debug.apk

# Build release APK (requires signing configuration)
./gradlew assembleRelease

# Build and run on connected device
./gradlew installDebug

# Clean build
./gradlew clean
```

## Installation

### On a Physical Device or Emulator

```bash
# Install the APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Or use gradlew
./gradlew installDebug

# Launch the app
adb shell am start -n com.glassos.launcher/.ui.LauncherActivity
```

### Set as Default Launcher

1. After installation, press the Home button
2. Select "GlassOS Launcher" as your home app
3. Or go to Settings → Default apps → Home and select GlassOS Launcher

## Configuration

### Changing Grid Size

Edit `/app/src/main/java/com/glassos/launcher/models/Models.kt`:

```kotlin
data class GridConfig(
    val columnsCount: Int = 4,  // Change here
    val rowsCount: Int = 4,     // Change here
    val pages: Int = 5
)
```

### Customizing Colors

Edit `/app/src/main/java/com/glassos/launcher/theme/Color.kt`:

```kotlin
val DarkBackground = Color(0xFF000000)
val SurfaceColor = Color(0xFF1F1F1F)
// ... customize any color
```

### Adjusting Glass Effect Blur

Edit component files (e.g., `GlassUIComponents.kt`):

```kotlin
renderEffect = RenderEffect.createBlurEffect(
    20f,  // Change blur radius
    20f,
    Shader.TileMode.CLAMP
)
```

## Permissions

The launcher requests the following permissions:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.FLASHLIGHT" />
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## Architecture

### MVVM Pattern
- **Models**: Data classes in `models/Models.kt`
- **ViewModels**: `LauncherStateManager` manages UI state
- **Views**: Compose-based UI components

### Component Organization
- **UI Components**: Reusable Compose components in `components/`
- **Managers**: Business logic in `managers/`
- **Theme**: Design system in `theme/`

## Dependencies

### Core
- androidx.core:core-ktx
- androidx.appcompat:appcompat
- com.google.android.material:material

### Compose
- androidx.compose.ui:ui
- androidx.compose.materials3:material3
- androidx.activity:activity-compose

### Lifecycle & State Management
- androidx.lifecycle:lifecycle-runtime-ktx
- androidx.lifecycle:lifecycle-viewmodel-compose

### Image Loading
- io.coil-kt:coil-compose

### Coroutines
- org.jetbrains.kotlinx:kotlinx-coroutines-android

## Troubleshooting

### Build Issues

**Error: "Gradle could not find android.os.Build"**
- Ensure Min SDK is set to 26 in `build.gradle.kts`
- Run `./gradlew clean build`

**Error: "Compose plugin not found"**
- Check that `buildFeatures { compose = true }` is in `build.gradle.kts`
- Update Android Studio to the latest version

### Runtime Issues

**App crashes on startup**
- Check logcat for detailed error messages: `adb logcat`
- Ensure all permissions are granted
- Clear app data: `adb shell pm clear com.glassos.launcher`

**Blur effect not visible**
- Glass blur effects require Android 12+ (API 31)
- On lower API levels, fallback to transparent overlays

## Contributing

To add new features:

1. Create new components in `components/`
2. Update state management in `LauncherStateManager`
3. Add models to `models/Models.kt`
4. Integrate into `LauncherActivity.kt`

## License

This project is provided as a complete Android launcher example.

## Version

- **Version**: 1.0.0
- **Latest SDK**: 34 (Android 14)
- **Min SDK**: 26 (Android 8.0)
- **Kotlin**: 1.9.22
- **Gradle**: 8.4

## Future Enhancements

- [ ] Folder management with drag-and-drop
- [ ] Widget customization and placement
- [ ] Custom icon packs support
- [ ] Theme customization UI
- [ ] Gesture customization
- [ ] Notification drawer integration
- [ ] System shortcuts integration
- [ ] App suggestions/predictions
- [ ] Multiple home screen layouts
- [ ] Advanced animation transitions

---

**Built with Jetpack Compose and MVVM Architecture**
