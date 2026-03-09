package com.glassos.launcher.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Liquid Glass Surface - A reusable glass morphism component
 * Features:
 * - Blur effect using RenderEffect
 * - Transparent tint overlay
 * - Rounded corners
 * - Reflection highlight
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Float = 24f,
    blurRadius: Float = 20f,
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color = Color.White.copy(alpha = 0.2f),
    borderWidth: Float = 1.5f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .graphicsLayer {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    renderEffect = RenderEffect.createBlurEffect(
                        blurRadius,
                        blurRadius,
                        Shader.TileMode.CLAMP
                    )
                }
            }
    ) {
        // Content
        content()
    }
}

/**
 * Compact Glass Card - For smaller UI elements
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.08f)),
        color = Color.Transparent,
        onClick = onClick ?: {}
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .graphicsLayer {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        renderEffect = RenderEffect.createBlurEffect(
                            12f,
                            12f,
                            Shader.TileMode.CLAMP
                        )
                    }
                }
        ) {
            content()
        }
    }
}

/**
 * Glass Dock - For app dock at the bottom
 */
@Composable
fun GlassDock(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .graphicsLayer {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    renderEffect = RenderEffect.createBlurEffect(
                        15f,
                        15f,
                        Shader.TileMode.CLAMP
                    )
                }
            }
            .padding(12.dp)
    ) {
        content()
    }
}

/**
 * Glass Button - For interactive elements
 */
@Composable
fun GlassButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f)),
        color = Color.Transparent,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .graphicsLayer {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        renderEffect = RenderEffect.createBlurEffect(
                            10f,
                            10f,
                            Shader.TileMode.CLAMP
                        )
                    }
                }
        ) {
            content()
        }
    }
}

/**
 * Glass Slider - For control center sliders
 */
@Composable
fun GlassSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .graphicsLayer {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    renderEffect = RenderEffect.createBlurEffect(
                        8f,
                        8f,
                        Shader.TileMode.CLAMP
                    )
                }
            }
            .padding(8.dp)
    ) {
        androidx.compose.material3.Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.2f))
        )
    }
}
