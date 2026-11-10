package com.Puqbafu0.Sogsok.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.TextSecondary

data class StatItem(
    val label: String,
    val value: String,
    val accent: Color,
    val visible: Boolean = true
)

@Composable
fun StatCard(
    item: StatItem,
    modifier: Modifier = Modifier
) {
    StoneCard(
        modifier = modifier,
        containerColor = Surface2,
        borderColor = item.accent.copy(alpha = 0.35f),
        shape = RoundedCornerShape(14.dp),
        elevation = 8.dp,
        inlay = false,
        contentPadding = 10.dp
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(item.accent, CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.value,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = item.accent,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextSecondary,
            maxLines = 1
        )
    }
}

@Composable
fun StatRow(
    items: List<StatItem>,
    modifier: Modifier = Modifier
) {
    val shown = items.filter { it.visible }
    if (shown.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        shown.forEach { item ->
            StatCard(item = item, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun StatGrid(
    items: List<StatItem>,
    columns: Int,
    modifier: Modifier = Modifier
) {
    val shown = items.filter { it.visible }
    if (shown.size < 2) return
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        shown.chunked(columns).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { item ->
                    StatCard(item = item, modifier = Modifier.weight(1f))
                }
                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}