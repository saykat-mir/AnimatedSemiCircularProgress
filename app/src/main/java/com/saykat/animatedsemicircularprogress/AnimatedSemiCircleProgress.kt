package com.saykat.animatedsemicircularprogress

import android.graphics.Paint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withSave
import kotlin.math.cos
import kotlin.math.sin

/**
 * Data class representing a label specification for the semi-circle progress
 */
data class LabelSpec(
    val text: String,
    val angle: Float,
    val range: ClosedFloatingPointRange<Float>
)

/**
 * Predefined labels for different progress ranges
 */
val labels = listOf(
    LabelSpec("OFF TRACK", -200f, 0f..20f),
    LabelSpec("IMPROVING", -140f, 21f..40f),
    LabelSpec("GOOD", -80f, 41f..60f),
    LabelSpec("GREAT", -20f, 61f..80f),
    LabelSpec("SUPERB", 20f, 81f..100f)
)

/**
 * Animated Semi-Circle Progress Component
 * 
 * A beautiful, animated semi-circular progress indicator with:
 * - Smooth progress animation
 * - Dynamic gradient background
 * - Interactive label highlighting
 * - Clickable progress buttons for testing
 * 
 * @param progress Initial progress value (0-100)
 */
@Composable
fun AnimatedSemiCircleProgress(progress: Int = 95) {

    // Infinite animation for gradient background movement
    val infiniteTransition = rememberInfiniteTransition()
    val targetOffset = with(LocalDensity.current) {
        1000.dp.toPx()
    }
    val offset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 25000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Current progress state with bounds checking
    var updatedProgress by remember { mutableFloatStateOf(progress.toFloat()) }
    LaunchedEffect(updatedProgress) {
        updatedProgress = updatedProgress.coerceIn(0f, 100f)
    }

    // Convert progress to arc sweep angle (280° total sweep)
    var localProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(updatedProgress) {
        localProgress = (280f * updatedProgress / 100).toFloat()
    }

    // Animated progress with smooth transition
    val animatedProgress by animateFloatAsState(
        targetValue = localProgress,
        animationSpec = tween(3000)
    )

    // Gradient colors for background animation
    val colorStops = listOf(
        0.00f to Color(0xEF74A9E9),
        0.35f to Color(0xDDFEEDF5),
        0.50f to Color(0xFFFDD7E8),
        0.70f to Color(0xFFFDBCE0),
        0.85f to Color(0xFF9AC7FF),
    )

    // Arc styling
    val strokeWidth = 28.dp
    val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                ProgressCircle(
                    updatedProgress = updatedProgress,
                    animatedProgress = animatedProgress,
                    offset = offset.value,
                    colorStops = colorStops,
                    strokeWidth = strokeWidth,
                    strokePx = strokePx
                )
            }

            // Control Buttons
            ProgressControlButtons { newProgress ->
                updatedProgress = newProgress
            }
        }
    }
}

/**
 * Main progress circle component containing the arc, labels, and center display
 */
@Composable
private fun ProgressCircle(
    updatedProgress: Float,
    animatedProgress: Float,
    offset: Float,
    colorStops: List<Pair<Float, Color>>,
    strokeWidth: androidx.compose.ui.unit.Dp,
    strokePx: Float
) {
    Box(
        modifier = Modifier.size(320.dp),
        contentAlignment = Alignment.Center
    ) {
        // Canvas for drawing the progress arc and labels
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth / 2)
        ) {
            val startAngle = -230f
            val sweepAngle = 280f
            val arcRadius = (size.minDimension - strokePx) / 2
            val center = Offset(size.width / 2, size.height / 2)

            // Draw progress labels around the arc
            drawTextLabels(
                center = center,
                arcRadius = arcRadius,
                strokePx = strokePx,
                textPadding = strokePx,
                currentProgress = updatedProgress
            )

            // Draw background arc (gray)
            drawArc(
                color = Color(0xFFF3F4F6),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Draw progress arc (blue)
            drawArc(
                color = Color(0xFF5F71FD),
                startAngle = startAngle,
                sweepAngle = animatedProgress,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Draw end indicator line
            drawEndIndicator(
                center = center,
                arcRadius = arcRadius,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                strokeWidth = strokeWidth
            )
        }
        
        // Center content with animated background
        CenterContent(
            updatedProgress = updatedProgress,
            offset = offset,
            colorStops = colorStops
        )
    }
}

/**
 * Draws the text labels around the arc
 */
private fun DrawScope.drawTextLabels(
    center: Offset,
    arcRadius: Float,
    strokePx: Float,
    textPadding: Float,
    currentProgress: Float
) {
    fun drawLabel(
        text: String,
        angleDeg: Float,
        radius: Float,
        color: Color = Color.Gray,
        size: Float = 32f,
        bold: Boolean = false
    ) {
        val angleRad = Math.toRadians(angleDeg.toDouble())
        val x = center.x + radius * cos(angleRad).toFloat()
        val y = center.y + radius * sin(angleRad).toFloat()

        val paint = Paint().apply {
            this.color = color.toArgb()
            this.textSize = size
            this.textAlign = Paint.Align.CENTER
            this.isFakeBoldText = bold
        }

        drawContext.canvas.nativeCanvas.apply {
            withSave {
                translate(x, y)
                rotate(angleDeg + 90f)
                drawText(text, 0f, 0f, paint)
            }
        }
    }

    val labelRadius = arcRadius + strokePx / 2 + textPadding
    labels.forEach { label ->
        val isActive = currentProgress in label.range
        drawLabel(
            text = label.text,
            angleDeg = label.angle,
            radius = labelRadius,
            color = if (isActive) Color.Black else Color.LightGray,
            size = 36f,
            bold = isActive
        )
    }
}

/**
 * Draws the end indicator line at the arc end
 */
private fun DrawScope.drawEndIndicator(
    center: Offset,
    arcRadius: Float,
    startAngle: Float,
    sweepAngle: Float,
    strokeWidth: androidx.compose.ui.unit.Dp
) {
    val angleRad = Math.toRadians((startAngle + sweepAngle * .90f).toDouble())
    val arcEnd = Offset(
        x = center.x + arcRadius * cos(angleRad).toFloat(),
        y = center.y + arcRadius * sin(angleRad).toFloat()
    )

    val lineLength = strokeWidth.toPx()
    val lineWidth = lineLength / 4
    val adjustLength = (lineLength - 2 * lineWidth)

    val lineStart = Offset(
        x = arcEnd.x + adjustLength - lineLength / 2 - adjustLength / 4,
        y = arcEnd.y
    )
    val lineEnd = Offset(
        x = arcEnd.x + adjustLength + lineLength / 2 + adjustLength / 4,
        y = arcEnd.y
    )

    // Draw white outline
    drawLine(
        color = Color.White,
        start = lineStart,
        end = lineEnd,
        strokeWidth = 2 * lineWidth,
        cap = StrokeCap.Round
    )

    // Draw green center line
    drawLine(
        color = Color(0xFF33CC66),
        start = lineStart,
        end = lineEnd,
        strokeWidth = lineWidth,
        cap = StrokeCap.Round
    )
}

/**
 * Center content with animated gradient background and progress text
 */
@Composable
private fun CenterContent(
    updatedProgress: Float,
    offset: Float,
    colorStops: List<Pair<Float, Color>>
) {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        // Animated gradient background
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape = CircleShape)
                .blur(20.dp)
                .drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.radialGradient(
                        colorStops = colorStops.toTypedArray(),
                        center = Offset(offset, offset),
                        radius = brushSize,
                        tileMode = TileMode.Mirror
                    )

                    onDrawBehind {
                        drawRect(brush = brush)
                    }
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${updatedProgress.toInt()}%",
                color = Color(0x99000000),
                fontSize = 46.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Completed",
                color = Color(0x99000000),
                fontSize = 26.sp
            )
        }
    }
}

@Composable
private fun ProgressControlButtons(
    onProgressChange: (Float) -> Unit
) {
    val progressValues = listOf(20f, 35f, 55f, 75f, 95f)
    
    Row {
        progressValues.forEachIndexed { index, progressValue ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            ProgressButton(
                progress = progressValue,
                onClick = { onProgressChange(progressValue) }
            )
        }
    }
}

@Composable
private fun ProgressButton(
    progress: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${progress.toInt()}%",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AnimatedSemiCircleProgressPreview() {
    AnimatedSemiCircleProgress()
}