package com.Puqbafu0.Sogsok.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.Puqbafu0.Sogsok.domain.model.AnimalTrack

@Composable
fun PawGlyph(
    animal: AnimalTrack,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        when (animal) {
            AnimalTrack.TIGER -> drawTiger(tint)
            AnimalTrack.SERPENT -> drawSerpent(tint)
            AnimalTrack.MONKEY -> drawMonkey(tint)
            AnimalTrack.HERON -> drawHeron(tint)
        }
    }
}

private fun DrawScope.oval(tint: Color, cx: Float, cy: Float, rx: Float, ry: Float, alpha: Float) {
    drawOval(
        color = tint,
        topLeft = Offset(size.width * (cx - rx), size.height * (cy - ry)),
        size = Size(size.width * rx * 2f, size.height * ry * 2f),
        alpha = alpha
    )
}

private fun DrawScope.drawTiger(tint: Color) {
    oval(tint, 0.50f, 0.68f, 0.26f, 0.22f, 0.95f)
    oval(tint, 0.20f, 0.40f, 0.11f, 0.13f, 0.85f)
    oval(tint, 0.40f, 0.26f, 0.11f, 0.14f, 0.85f)
    oval(tint, 0.62f, 0.26f, 0.11f, 0.14f, 0.85f)
    oval(tint, 0.82f, 0.42f, 0.11f, 0.13f, 0.85f)
}

private fun DrawScope.drawSerpent(tint: Color) {
    oval(tint, 0.30f, 0.26f, 0.16f, 0.12f, 0.9f)
    oval(tint, 0.52f, 0.50f, 0.18f, 0.13f, 0.95f)
    oval(tint, 0.72f, 0.76f, 0.16f, 0.12f, 0.9f)
    oval(tint, 0.24f, 0.70f, 0.07f, 0.06f, 0.6f)
}

private fun DrawScope.drawMonkey(tint: Color) {
    oval(tint, 0.48f, 0.72f, 0.20f, 0.20f, 0.95f)
    oval(tint, 0.18f, 0.52f, 0.08f, 0.11f, 0.85f)
    oval(tint, 0.33f, 0.30f, 0.08f, 0.12f, 0.85f)
    oval(tint, 0.50f, 0.22f, 0.08f, 0.12f, 0.85f)
    oval(tint, 0.67f, 0.28f, 0.08f, 0.12f, 0.85f)
    oval(tint, 0.82f, 0.48f, 0.08f, 0.11f, 0.85f)
}

private fun DrawScope.drawHeron(tint: Color) {
    val stroke = Stroke(width = size.minDimension * 0.10f, cap = StrokeCap.Round)
    val center = Offset(size.width * 0.5f, size.height * 0.62f)
    drawLine(
        color = tint,
        start = center,
        end = Offset(size.width * 0.16f, size.height * 0.20f),
        strokeWidth = stroke.width,
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = center,
        end = Offset(size.width * 0.50f, size.height * 0.10f),
        strokeWidth = stroke.width,
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = center,
        end = Offset(size.width * 0.84f, size.height * 0.20f),
        strokeWidth = stroke.width,
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = center,
        end = Offset(size.width * 0.50f, size.height * 0.92f),
        strokeWidth = stroke.width * 0.8f,
        cap = StrokeCap.Round
    )
}
