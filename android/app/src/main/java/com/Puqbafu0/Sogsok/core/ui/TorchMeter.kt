package com.Puqbafu0.Sogsok.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.StoneCold

@Composable
fun TorchMeter(
    lit: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val description = stringResource(R.string.cd_torches, lit, total)
    val state = stringResource(R.string.game_torches, lit)
    Row(
        modifier = modifier.semantics {
            contentDescription = description
            stateDescription = state
            liveRegion = LiveRegionMode.Polite
        },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            TorchPip(active = index < lit)
        }
    }
}

@Composable
private fun TorchPip(active: Boolean) {
    val glow by animateFloatAsState(
        targetValue = if (active) 1f else 0.25f,
        animationSpec = tween(durationMillis = 260),
        label = "torch_pip"
    )
    Canvas(modifier = Modifier.size(width = 16.dp, height = 22.dp)) {
        val width = size.width
        val height = size.height
        val stemColor = if (active) GoldLight else StoneCold
        drawRoundRect(
            color = stemColor.copy(alpha = if (active) 0.85f else 0.5f),
            topLeft = Offset(width * 0.38f, height * 0.52f),
            size = androidx.compose.ui.geometry.Size(width * 0.24f, height * 0.44f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(width * 0.1f)
        )
        val flame = Path().apply {
            moveTo(width * 0.5f, height * 0.04f)
            cubicTo(
                width * 0.92f, height * 0.26f,
                width * 0.84f, height * 0.58f,
                width * 0.5f, height * 0.60f
            )
            cubicTo(
                width * 0.16f, height * 0.58f,
                width * 0.08f, height * 0.26f,
                width * 0.5f, height * 0.04f
            )
            close()
        }
        drawPath(
            path = flame,
            brush = Brush.verticalGradient(
                colors = if (active) {
                    listOf(GoldLight, Ember)
                } else {
                    listOf(StoneCold, Color.Black.copy(alpha = 0.4f))
                }
            ),
            alpha = glow
        )
    }
}
