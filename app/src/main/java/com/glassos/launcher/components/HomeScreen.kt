package com.glassos.launcher.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Grid
import androidx.compose.ui.unit.dp
import androidx.graphics.drawable.VectorDrawableCompat
import com.glassos.launcher.models.GridConfig
import com.glassos.launcher.models.HomeScreenItem

/**
 * Home Screen Workspace - Main launcher view with grid system and app pages
 * Features:
 * - Configurable grid layout (default 4x4)
 * - Horizontal page scrolling
 * - Drag and drop support (basic)
 * - Icon display
 */
@Composable
fun HomeScreenWorkspace(
    modifier: Modifier = Modifier,
    gridConfig: GridConfig = GridConfig(),
    items: List<HomeScreenItem> = emptyList(),
    currentPage: Int = 0,
    onPageChange: (Int) -> Unit = {},
    onItemClick: (HomeScreenItem) -> Unit = {},
    onItemLongClick: (HomeScreenItem) -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { gridConfig.pages }
    )

    // Update current page when it changes from outside
    if (currentPage != pagerState.currentPage) {
        onPageChange(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageIndex ->
        HomeScreenPage(
            gridConfig = gridConfig,
            items = items.filter { it.gridX / gridConfig.columnsCount == pageIndex },
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick
        )
    }
}

@Composable
private fun HomeScreenPage(
    gridConfig: GridConfig,
    items: List<HomeScreenItem>,
    onItemClick: (HomeScreenItem) -> Unit,
    onItemLongClick: (HomeScreenItem) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Create grid of app icons
        repeat(gridConfig.rowsCount) { row ->
            repeat(gridConfig.columnsCount) { col ->
                val index = row * gridConfig.columnsCount + col
                val item = items.find { it.gridX % gridConfig.columnsCount == col && it.gridY == row }

                Box(
                    modifier = Modifier
                        .align(
                            when {
                                row == 0 && col == 0 -> Alignment.TopStart
                                row == 0 -> if (col == gridConfig.columnsCount - 1) Alignment.TopEnd else Alignment.TopCenter
                                else -> Alignment.Center
                            }
                        )
                        .size(
                            width = (100.f / gridConfig.columnsCount).dp,
                            height = (100.f / gridConfig.rowsCount).dp
                        )
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onLongPress = {
                                    item?.let { onItemLongClick(it) }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (item != null) {
                        AppIconView(
                            item = item,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppIconView(
    item: HomeScreenItem,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.size(56.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (item.icon != null && item.type == "app") {
                // Icon rendering placeholder - in production would use image loader
                androidx.compose.material3.Text(
                    text = item.label?.firstOrNull()?.toString() ?: "?",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
