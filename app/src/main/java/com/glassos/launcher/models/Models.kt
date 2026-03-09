package com.glassos.launcher.models

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val isSystemApp: Boolean = false
) {
    companion object {
        const val FOLDER_TYPE = "folder"
    }
}

data class HomeScreenItem(
    val id: String,
    val type: String, // "app", "folder", "widget"
    val label: String? = null,
    val packageName: String? = null,
    val gridX: Int = 0,
    val gridY: Int = 0,
    val icon: Drawable? = null,
    val children: List<AppInfo> = emptyList() // For folders
)

data class GridConfig(
    val columnsCount: Int = 4,
    val rowsCount: Int = 4,
    val pages: Int = 5
)

data class DynamicIslandNotification(
    val id: String,
    val title: String,
    val message: String,
    val icon: Drawable? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class MediaSessionInfo(
    val title: String?,
    val artist: String?,
    val album: String?,
    val isPlaying: Boolean = false,
    val playbackPosition: Long = 0L,
    val duration: Long = 0L
)

enum class ControlCenterState {
    COLLAPSED,
    EXPANDED,
    HIDDEN
}

data class ControlCenterSettings(
    val wifiEnabled: Boolean = false,
    val bluetoothEnabled: Boolean = false,
    val flashlightEnabled: Boolean = false,
    val brightness: Int = 128,
    val volume: Int = 128,
    val mediaInfo: MediaSessionInfo? = null
)

data class BatteryInfo(
    val level: Int = 100,
    val isCharging: Boolean = false,
    val health: Int = 0
)

data class WeatherInfo(
    val temperature: Int = 20,
    val condition: String = "Clear",
    val humidity: Int = 50,
    val windSpeed: Int = 5
)
