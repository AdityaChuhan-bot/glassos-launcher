package com.glassos.launcher.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glassos.launcher.models.BatteryInfo
import com.glassos.launcher.models.MediaSessionInfo

/**
 * Dynamic Island - Floating component at the top that shows various system information
 * Features:
 * - Collapsed pill UI
 * - Expandable to show more details
 * - Shows battery, media, notifications
 */
@Composable
fun DynamicIsland(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onToggleExpanded: () -> Unit = {},
    batteryInfo: BatteryInfo = BatteryInfo(),
    mediaInfo: MediaSessionInfo? = null,
    onMediaClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 12.dp)
    ) {
        // Main Island Container
        GlassSurface(
            modifier = Modifier
                .width(if (expanded) 280.dp else 120.dp)
                .height(if (expanded) 160.dp else 40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggleExpanded
                )
                .animateContentSize(),
            cornerRadius = if (expanded) 32f else 20f,
            backgroundColor = Color.White.copy(alpha = if (expanded) 0.12f else 0.1f)
        ) {
            if (expanded) {
                ExpandedIslandContent(
                    batteryInfo = batteryInfo,
                    mediaInfo = mediaInfo,
                    onMediaClick = onMediaClick
                )
            } else {
                CollapsedIslandContent(
                    batteryInfo = batteryInfo,
                    mediaInfo = mediaInfo
                )
            }
        }
    }
}

@Composable
private fun CollapsedIslandContent(
    batteryInfo: BatteryInfo,
    mediaInfo: MediaSessionInfo?
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (mediaInfo?.isPlaying == true) {
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = "Music",
                modifier = Modifier.width(16.dp),
                tint = Color.White
            )
        } else {
            Icon(
                imageVector = Icons.Filled.BatteryFull,
                contentDescription = "Battery",
                modifier = Modifier.width(16.dp),
                tint = Color.White
            )
            Text(
                text = "${batteryInfo.level}%",
                fontSize = 11.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun ExpandedIslandContent(
    batteryInfo: BatteryInfo,
    mediaInfo: MediaSessionInfo?,
    onMediaClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Battery info
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.BatteryFull,
                contentDescription = "Battery",
                modifier = Modifier
                    .width(24.dp)
                    .padding(end = 8.dp),
                tint = Color.White
            )
            Column {
                Text(
                    text = "Battery",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = "${batteryInfo.level}%${if (batteryInfo.isCharging) " - Charging" else ""}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Media info
        if (mediaInfo != null && mediaInfo.isPlaying) {
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onMediaClick
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = "Music",
                    modifier = Modifier
                        .width(24.dp)
                        .padding(end = 8.dp),
                    tint = Color.White
                )
                Column {
                    Text(
                        text = mediaInfo.title ?: "Now Playing",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1
                    )
                    Text(
                        text = mediaInfo.artist ?: "Unknown",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
