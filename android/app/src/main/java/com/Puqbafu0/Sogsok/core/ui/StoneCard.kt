package com.Puqbafu0.Sogsok.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight
import com.Puqbafu0.Sogsok.core.theme.Surface1

@Composable
fun StoneCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Surface1,
    borderColor: Color = Gold.copy(alpha = 0.22f),
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 12.dp,
    inlay: Boolean = true,
    contentPadding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = elevation
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (inlay) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = contentPadding)
                        .padding(top = 6.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    SlabHighlight,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    }
}
