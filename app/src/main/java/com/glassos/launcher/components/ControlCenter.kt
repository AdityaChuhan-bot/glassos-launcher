package com.glassos.launcher.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Music
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glassos.launcher.models.ControlCenterSettings
import com.glassos.launcher.models.ControlCenterState
import com.glassos.launcher.models.MediaSessionInfo

/**
 * Control Center - iOS style control panel accessed by swiping down from top right
 * Features:
 * - WiFi, Bluetooth, Flashlight toggles
 * - Brightness and Volume sliders
 * - Media playback controls
 * - Glass UI cards
 */
@Composable
fun ControlCenter(
    modifier: Modifier = Modifier,
    state: ControlCenterState = ControlCenterState.HIDDEN,
    settings: ControlCenterSettings = ControlCenterSettings(),
    onToggleWifi: (Boolean) -> Unit = {},
    onToggleBluetooth: (Boolean) -> Unit = {},
    onToggleFlashlight: (Boolean) -> Unit = {},
    onBrightnessChange: (Int) -> Unit = {},
    onVolumeChange: (Int) -> Unit = {},
    onMediaPlayPause: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = state != ControlCenterState.HIDDEN,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClose
                )
        ) {
            GlassSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                cornerRadius = 24f,
                backgroundColor = Color.White.copy(alpha = 0.08f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Header
                    Text(
                        text = "Control Center",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Quick toggles
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        item {
                            ControlToggleButton(
                                icon = Icons.Filled.Wifi,
                                label = "WiFi",
                                enabled = settings.wifiEnabled,
                                onToggle = onToggleWifi
                            )
                        }
                        item {
                            ControlToggleButton(
                                icon = Icons.Filled.Bluetooth,
                                label = "Bluetooth",
                                enabled = settings.bluetoothEnabled,
                                onToggle = onToggleBluetooth
                            )
                        }
                        item {
                            ControlToggleButton(
                                icon = Icons.Filled.FlashlightOn,
                                label = "Flashlight",
                                enabled = settings.flashlightEnabled,
                                onToggle = onToggleFlashlight
                            )
                        }
                    }

                    // Sliders
                    SliderControl(
                        icon = Icons.Filled.Brightness7,
                        label = "Brightness",
                        value = settings.brightness.toFloat() / 255f,
                        onValueChange = { onBrightnessChange((it * 255).toInt()) }
                    )

                    SliderControl(
                        icon = Icons.Filled.VolumeUp,
                        label = "Volume",
                        value = settings.volume.toFloat() / 255f,
                        onValueChange = { onVolumeChange((it * 255).toInt()) }
                    )

                    // Media Controls
                    if (settings.mediaInfo != null) {
                        MediaControlsCard(
                            mediaInfo = settings.mediaInfo,
                            onPlayPause = onMediaPlayPause
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlToggleButton(
    icon: ImageVector,
    label: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    GlassCard(
        modifier = Modifier
            .size(90.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onToggle(!enabled) }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = if (enabled) Color.White else Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = if (enabled) Color.White else Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun SliderControl(
    icon: ImageVector,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp),
            tint = Color.White
        )
        GlassSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
        )
    }
}

@Composable
private fun MediaControlsCard(
    mediaInfo: MediaSessionInfo,
    onPlayPause: () -> Unit
) {
    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        cornerRadius = 16f
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mediaInfo.title ?: "Now Playing",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = mediaInfo.artist ?: "Unknown",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
            GlassButton(
                onClick = onPlayPause,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Music,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
