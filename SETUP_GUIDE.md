# GlassOS Launcher - Setup Guide

## Prerequisites

### System Requirements
- **OS**: Windows 10+, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM**: Minimum 4GB (8GB recommended)
- **Disk Space**: 10GB+ (for Android SDK and emulator)
- **Java**: JDK 17 or newer

### Required Software

1. **Android Studio** (Recommended)
   - Download: https://developer.android.com/studio
   - Includes Android SDK, build tools, and emulator

2. **Android SDK** (if not using Android Studio)
   - Download: https://developer.android.com/studio#command-tools

3. **Git** (for cloning the repository)
   - Download: https://git-scm.com

## Step-by-Step Installation

### Option 1: Using Android Studio (Recommended)

#### Windows

1. **Install Android Studio**
   - Download the latest version from https://developer.android.com/studio
   - Run the installer
   - Follow the setup wizard

2. **Install Android SDK**
   - Open Android Studio
   - Go to: File → Settings → Appearance & Behavior → System Settings → Android SDK
   - Ensure API 34 (Android 14) is installed
   - Click "Apply" to download if needed

3. **Install Build Tools**
   - In the same SDK Manager window
   - Go to "SDK Tools" tab
   - Ensure "Android SDK Build-Tools" version 34.0.0+ is installed

4. **Set ANDROID_HOME**
   - Open Environment Variables (Win+X → System)
   - Create new user variable: `ANDROID_HOME`
   - Set value to your Android SDK location (typically: `C:\Users\<YourUsername>\AppData\Local\Android\sdk`)

5. **Open Project**
   - Launch Android Studio
   - File → Open
   - Navigate to the glassos-launcher folder
   - Click Open

6. **Sync Gradle**
   - Android Studio will automatically detect and prompt to sync Gradle
   - Click "Sync Now"
   - Wait for build completion

#### macOS

```bash
# Install Homebrew (if not installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java
brew install openjdk@17

# Install Android Studio
brew install android-studio

# Run Android Studio
open /Applications/Android\ Studio.app

# Then follow the Windows steps above for SDK setup
```

#### Linux (Ubuntu)

```bash
# Install Java
sudo apt-get update
sudo apt-get install openjdk-17-jdk

# Install Android Studio
# Download from: https://developer.android.com/studio
# Or use snap:
sudo snap install android-studio --classic

# Run Android Studio
android-studio

# Then follow the Windows steps above for SDK setup
```

### Option 2: Manual SDK Installation (Command Line)

#### Download Command-Line Tools

1. Go to: https://developer.android.com/studio#command-tools
2. Download "Command line tools only" for your OS

#### Linux/macOS Setup

```bash
# Create SDK directory
mkdir -p ~/Android/sdk
cd ~/Android/sdk

# Extract command-line tools
unzip ~/Downloads/commandlinetools-linux-*.zip
# (or commandlinetools-mac-*.zip for macOS)

# Create tools directory
mkdir -p cmdline-tools
mv cmdline-tools tools

# Accept licenses
yes | ~/Android/sdk/cmdline-tools/tools/bin/sdkmanager --licenses

# Install required components
~/Android/sdk/cmdline-tools/tools/bin/sdkmanager "platforms;android-34"
~/Android/sdk/cmdline-tools/tools/bin/sdkmanager "build-tools;34.0.0"
~/Android/sdk/cmdline-tools/tools/bin/sdkmanager "emulator"
~/Android/sdk/cmdline-tools/tools/bin/sdkmanager "platform-tools"

# Set ANDROID_HOME
echo 'export ANDROID_HOME=~/Android/sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/tools/bin' >> ~/.bashrc
source ~/.bashrc
```

#### Windows Setup (Command Prompt as Admin)

```cmd
# Create SDK directory
mkdir %USERPROFILE%\Android\sdk
cd %USERPROFILE%\Android\sdk

# Extract command-line tools (use 7-Zip or Windows native extraction)
# Navigate to extracted folder

# Accept licenses
%USERPROFILE%\Android\sdk\cmdline-tools\tools\bin\sdkmanager.bat --licenses

# Install required components
%USERPROFILE%\Android\sdk\cmdline-tools\tools\bin\sdkmanager.bat "platforms;android-34"
%USERPROFILE%\Android\sdk\cmdline-tools\tools\bin\sdkmanager.bat "build-tools;34.0.0"
%USERPROFILE%\Android\sdk\cmdline-tools\tools\bin\sdkmanager.bat "emulator"
%USERPROFILE%\Android\sdk\cmdline-tools\tools\bin\sdkmanager.bat "platform-tools"

# Set ANDROID_HOME environment variable (see Option 1 for detailed steps)
```

## Building the Project

### Using Android Studio

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Select "Build" → "Build Module 'app'"
4. APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Using Command Line

```bash
# Navigate to project
cd /path/to/glassos-launcher

# Run gradle build (make executable on Linux/macOS)
chmod +x gradlew
./gradlew assembleDebug

# Result: app/build/outputs/apk/debug/app-debug.apk
```

### Build Variants

```bash
# Debug build (faster, includes debugging info)
./gradlew assembleDebug

# Release build (optimized, requires signing)
./gradlew assembleRelease

# Both
./gradlew assemble

# Run on connected device
./gradlew installDebug
./gradlew runDebug
```

## Installation on Device/Emulator

### Using Android Studio

1. **Create/Start Emulator**
   - Tools → Device Manager → Create Virtual Device
   - Select a device template
   - Finish
   - Click Play to start emulator

2. **Run App**
   - Click "Run 'app'" button or press Shift+F10
   - Select your device/emulator
   - Click OK
   - App will install and launch

### Using ADB (Command Line)

```bash
# List connected devices
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.glassos.launcher/.ui.LauncherActivity

# View logs
adb logcat
```

## Troubleshooting

### Issue: "ANDROID_HOME not set"

**Solution:**
- Ensure ANDROID_HOME environment variable is set to your SDK path
- On Linux/macOS: `export ANDROID_HOME=/path/to/sdk`
- On Windows: Set via Environment Variables (see setup above)
- Verify: `echo $ANDROID_HOME` (Linux/macOS) or `echo %ANDROID_HOME%` (Windows)

### Issue: "Gradle sync failed"

**Solution:**
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

### Issue: "SDK location not set"

**Solution:**
- In Android Studio: File → Settings → Appearance & Behavior → System Settings → Android SDK
- Verify SDK path is correct
- Or create `local.properties` in project root:
  ```
  sdk.dir=/path/to/android/sdk
  ```

### Issue: "Emulator won't start"

**Solution:**
- Ensure KVM is enabled (Linux): `kvm-ok`
- Increase allocated RAM in emulator settings
- Try: `emulator -avd <name> -no-snapshot-load`

### Issue: "Build fails with Kotlin errors"

**Solution:**
- Check Kotlin plugin version in Android Studio is up to date
- Clear cache: `./gradlew clean`
- Rebuild: `./gradlew build`

### Issue: "App crashes on launch"

**Solution:**
```bash
# Check logcat
adb logcat *:E

# Clear app data
adb shell pm clear com.glassos.launcher

# Reinstall
./gradlew installDebug
```

## Development Workflow

### Modifying Code

1. Edit Kotlin files in `app/src/main/java/com/glassos/launcher/`
2. Save changes
3. Android Studio auto-compiles (if enabled)
4. Run → Run 'app' to test

### Building for Release

1. Generate signing key (if you don't have one):
   ```bash
   keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias launcher
   ```

2. Create `keystore.properties` in project root:
   ```properties
   storeFile=./release.keystore
   storePassword=<your_password>
   keyAlias=launcher
   keyPassword=<your_password>
   ```

3. Build release APK:
   ```bash
   ./gradlew assembleRelease
   ```

4. APK location: `app/build/outputs/apk/release/app-release.apk`

## Setting as Default Launcher

1. Install app on device
2. Press Home button
3. Choose "GlassOS Launcher"
4. Select "Always" to make it default

Or via ADB:
```bash
adb shell cmd package set-preferred-activity --user 0 com.android.launcher3 com.android.launcher3.Launcher intent
```

## Emulator Tips

### Recommended Configuration
- Device: Pixel 5 (1440×2560, 120dp)
- Android Version: 14 (API 34)
- RAM: 4GB
- Storage: 4GB
- GPU: On

### Fast Emulator Launch
```bash
emulator -avd <name> -no-snapshot-save &
```

### Testing on Real Device
- Enable Developer Mode: Settings → About → Build Number (tap 7x)
- Enable USB Debugging: Developer Options → USB Debugging
- Connect via USB
- Accept RSA fingerprint when prompted

## Next Steps

1. Review [README.md](./README.md) for feature documentation
2. Check [ARCHITECTURE.md](./ARCHITECTURE.md) for code structure
3. Start building and customizing!

## Support

For issues:
1. Check Gradle console for error messages
2. Review logcat output: `adb logcat`
3. Clean rebuild: `./gradlew clean build`
4. Check Android Studio Help → Check for Updates

## Resources

- [Android Documentation](https://developer.android.com/docs)
- [Jetpack Compose Guide](https://developer.android.com/jetpack/compose)
- [Android Studio IDE](https://developer.android.com/studio)
- [Gradle for Android](https://developer.android.com/build)
