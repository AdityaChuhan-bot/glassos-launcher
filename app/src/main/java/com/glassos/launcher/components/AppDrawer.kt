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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.glassos.launcher.models.AppInfo

/**
 * App Drawer - Full screen drawer showing all installed apps
 * Accessible via swipe up gesture
 * Features:
 * - Grid layout of apps
 * - Search functionality
 * - Alphabetical sorting
 * - Touch to open app
 */
@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    apps: List<AppInfo> = emptyList(),
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onAppClick: (AppInfo) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
            ) {
                // Header with search
                GlassSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 16.dp),
                    cornerRadius = 16f
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier
                                .weight(1f),
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            ),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search apps...",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                // Filter apps based on search query
                val filteredApps = if (searchQuery.isEmpty()) {
                    apps
                } else {
                    apps.filter { it.label.contains(searchQuery, ignoreCase = true) }
                }

                // Apps grid
                if (filteredApps.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isEmpty()) "No apps found" else "No results",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .weight(1f)
                    ) {
                        items(filteredApps) { app ->
                            AppGridItem(
                                app = app,
                                onClick = { onAppClick(app) }
                            )
                        }
                    }
                }

                // Close button area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismiss
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}

@Composable
private fun AppGridItem(
    app: AppInfo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App icon
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            try {
                AsyncImage(
                    model = app.icon,
                    contentDescription = app.label,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } catch (e: Exception) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = app.label.firstOrNull()?.toString() ?: "?",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // App label
        Text(
            text = app.label,
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
