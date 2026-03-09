package com.glassos.launcher.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glassos.launcher.models.BatteryInfo
import com.glassos.launcher.models.MediaSessionInfo
import com.glassos.launcher.models.WeatherInfo
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Clock Widget - Displays current time
 */
@Composable
fun ClockWidget(
    modifier: Modifier = Modifier
) {
    val timeState = remember { mutableStateOf("12:00") }
    val dateState = remember { mutableStateOf("Monday") }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Date()
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            timeState.value = timeFormat.format(now)
            dateState.value = dateFormat.format(now)
            delay(1000)
        }
    }

    GlassSurface(
        modifier = modifier.size(width = 140.dp, height = 140.dp),
        cornerRadius = 20f
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 140.dp, height = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = timeState.value,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = dateState.value,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Weather Widget - Displays temperature and weather condition
 */
@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier,
    weatherInfo: WeatherInfo = WeatherInfo()
) {
    GlassSurface(
        modifier = modifier.size(width = 140.dp, height = 140.dp),
        cornerRadius = 20f
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 140.dp, height = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Cloud,
                contentDescription = "Weather",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Text(
                text = "${weatherInfo.temperature}°C",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = weatherInfo.condition,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Battery Widget - Displays battery level
 */
@Composable
fun BatteryWidget(
    modifier: Modifier = Modifier,
    batteryInfo: BatteryInfo = BatteryInfo()
) {
    val batteryColor = when {
        batteryInfo.level > 50 -> Color(0xFF4CAF50)
        batteryInfo.level > 20 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    GlassSurface(
        modifier = modifier.size(width = 140.dp, height = 140.dp),
        cornerRadius = 20f
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 140.dp, height = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = batteryColor.copy(alpha = 0.3f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${batteryInfo.level}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = batteryColor
                )
            }
            Text(
                text = "Battery",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )
            if (batteryInfo.isCharging) {
                Text(
                    text = "Charging",
                    fontSize = 10.sp,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

/**
 * Music Widget - Displays now playing information
 */
@Composable
fun MusicWidget(
    modifier: Modifier = Modifier,
    mediaInfo: MediaSessionInfo? = null
) {
    GlassSurface(
        modifier = modifier.size(width = 160.dp, height = 100.dp),
        cornerRadius = 16f
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .size(width = 160.dp, height = 100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = "Music",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = mediaInfo?.title ?: "No music",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = mediaInfo?.artist ?: "Playing",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
                if (mediaInfo != null && mediaInfo.duration > 0) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(width = 100.dp, height = 4.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

/**
 * Search Widget - Displays search bar
 */
@Composable
fun SearchWidget(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {}
) {
    GlassSurface(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        cornerRadius = 12f
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search apps...",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
