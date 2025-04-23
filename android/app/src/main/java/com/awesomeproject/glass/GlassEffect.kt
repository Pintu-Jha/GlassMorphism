package com.awesomeproject.glass

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlin.math.cos
import kotlin.math.sin
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Safe wrapper for GlassEffect that properly handles lifecycle events
 */
@Composable
fun SafeGlassEffectWrapper() {
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Use DisposableEffect to handle lifecycle events safely
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // You can handle specific lifecycle events here if needed
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // Only render the GlassEffect when the view is at least STARTED
    if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
        GlassEffect()
    }
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun GlassEffect(modifier: Modifier = Modifier) {
    // Create StateFlow to properly handle state across composition
    val hazeState = remember { HazeState() }
    var springState by remember { mutableStateOf(false) }

    // Safely animate with composition awareness and memory efficiency
    val springScale by animateFloatAsState(
        targetValue = if (springState) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    // Use remember for transition to prevent recreation during recomposition
    val infiniteTransition = rememberInfiniteTransition(label = "rotationTransition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angleAnimation"
    )

    val density = LocalDensity.current
    val boxSize = 200.dp
    val boxSizePx = with(density) { boxSize.toPx() }
    val centerOffset = Offset(boxSizePx / 2, boxSizePx / 2)

    // Create brush with optimized calculation
    val colors = remember {
        listOf(
            Color(0xFF7790ff),
            Color(0xFFff787a),
            Color(0xFF1acd9a),
            Color(0xFFff8fc0)
        )
    }
    
    val gradientBrush = remember(angle) {
        Brush.linearGradient(
            colors = colors,
            start = Offset(0f, 0f).rotate(angle, centerOffset),
            end = Offset(boxSizePx, boxSizePx).rotate(angle, centerOffset)
        )
    }

    Box(
        modifier = modifier
            .background(Color(0xff030521))
            .fillMaxSize()
            .haze(hazeState, debugWithBackgroundImage = false)
            .clickable { springState = !springState }
    ) {
        val minRadius = with(density) { 100.dp.toPx() }

        // Use remember to optimize calculations
        val circleCenters = remember {
            listOf(
                Offset(0.25f, 0.25f),
                Offset(0.75f, 0.25f),
                Offset(0.25f, 0.75f),
                Offset(0.75f, 0.75f)
            )
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            circleCenters.forEachIndexed { index, offsetPercentage ->
                val center = Offset(
                    size.width * offsetPercentage.x,
                    size.height * offsetPercentage.y
                )
                drawCircle(
                    color = colors[index],
                    center = center,
                    radius = minRadius
                )
            }
        }

        Box(
            modifier = Modifier
                .hazeChild(
                    state = hazeState,
                    shape = RoundedCornerShape(50.dp),
                    style = HazeMaterials.ultraThin()
                )
                .clip(RoundedCornerShape(50.dp))
                .border(
                    width = 4.dp,
                    brush = gradientBrush,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color.Transparent)
                .align(Alignment.Center)
                .size(width = 340.dp, height = 500.dp)
                .scale(springScale)
        ) {
            Text(
                text = "Filled",
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun Offset.rotate(degrees: Float, pivot: Offset): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    val cos = cos(radians)
    val sin = sin(radians)
    return Offset(
        x = (cos * (x - pivot.x) - sin * (y - pivot.y) + pivot.x).toFloat(),
        y = (sin * (x - pivot.x) + cos * (y - pivot.y) + pivot.y).toFloat()
    )
}