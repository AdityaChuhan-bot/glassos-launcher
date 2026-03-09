#!/bin/bash

# GlassOS Launcher - Setup Script for Android Development

echo "======================================="
echo "GlassOS Launcher - Development Setup"
echo "======================================="
echo ""

# Check if Android SDK is installed
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ ANDROID_HOME is not set"
    echo ""
    echo "To set up the Android SDK:"
    echo "1. Download Android SDK from: https://developer.android.com/studio"
    echo "2. Install Android Studio"
    echo "3. Or set ANDROID_HOME to your SDK directory:"
    echo "   export ANDROID_HOME=/path/to/android/sdk"
    exit 1
fi

echo "✓ ANDROID_HOME is set to: $ANDROID_HOME"

# Check for required SDK components
echo ""
echo "Checking for required SDK components..."

if [ -d "$ANDROID_HOME/platforms/android-34" ]; then
    echo "✓ Android API 34 SDK found"
else
    echo "❌ Android API 34 SDK not found"
    echo "   Run: sdkmanager 'platforms;android-34'"
fi

if [ -d "$ANDROID_HOME/build-tools/34.0.0" ] || [ -d "$ANDROID_HOME/build-tools/34"* ]; then
    echo "✓ Build tools found"
else
    echo "❌ Build tools not found"
    echo "   Run: sdkmanager 'build-tools;34.0.0'"
fi

echo ""
echo "======================================="
echo "Building the project..."
echo "======================================="
echo ""

# Build the APK
if [ -f "gradlew" ]; then
    chmod +x gradlew
    ./gradlew assembleDebug
else
    gradle assembleDebug
fi

echo ""
echo "Build complete!"
