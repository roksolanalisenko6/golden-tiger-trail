package com.Puqbafu0.Sogsok.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight

private const val STAR_GLYPH = "★"

@Composable
fun StarRow(
    stars: Int,
    modifier: Modifier = Modifier,
    glyphSize: TextUnit = 13.sp,
    spacing: Int = 3
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        repeat(3) { index ->
            Text(
                text = STAR_GLYPH,
                fontSize = glyphSize,
                color = if (index < stars) Gold else SlabHighlight
            )
        }
    }
}
