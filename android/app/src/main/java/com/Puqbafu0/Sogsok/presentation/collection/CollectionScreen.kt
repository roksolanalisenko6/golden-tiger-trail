package com.Puqbafu0.Sogsok.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.di.AppViewModelFactory
import com.Puqbafu0.Sogsok.core.theme.BgBase
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextPrimary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.EmptyStateMessage
import com.Puqbafu0.Sogsok.core.ui.HeaderChip
import com.Puqbafu0.Sogsok.core.ui.ScreenHeader
import com.Puqbafu0.Sogsok.core.ui.SecondaryButton
import com.Puqbafu0.Sogsok.core.ui.StoneCard
import com.Puqbafu0.Sogsok.core.ui.relicSpriteRes
import com.Puqbafu0.Sogsok.data.sample.SampleData
import com.Puqbafu0.Sogsok.domain.model.RelicSet

private const val CHECK_GLYPH = "✓"
private const val DOT_GLYPH = "·"

@Composable
fun CollectionScreen(onMenu: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    AppBackground(
        stops = listOf(BgBase, Surface1.copy(alpha = 0.9f), BgBase),
        artAlpha = 0.20f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.collection_title),
                onBack = onMenu,
                trailing = {
                    HeaderChip(
                        text = stringResource(
                            R.string.fmt_ratio,
                            state.recovered.size,
                            state.totalRelics
                        ),
                        accent = Jade
                    )
                }
            )

            if (state.recovered.isEmpty()) {
                EmptyStateMessage(text = stringResource(R.string.collection_empty))
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(SampleData.relicSets) { relicSet ->
                    SetPanel(relicSet = relicSet, recovered = state.recovered)
                }
            }

            SecondaryButton(
                text = stringResource(R.string.action_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun SetPanel(relicSet: RelicSet, recovered: Set<Int>) {
    val complete = relicSet.relics.all { recovered.contains(it.id) }
    StoneCard(
        borderColor = if (complete) Gold else Gold.copy(alpha = 0.18f),
        containerColor = Surface1,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        if (complete) Gold else SlabHighlight,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(relicSpriteRes(relicSet.id)),
                    contentDescription = stringResource(
                        R.string.cd_relic,
                        stringResource(relicSet.nameRes)
                    ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .alpha(if (complete) 1f else 0.75f)
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(relicSet.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = if (complete) GoldLight else TextPrimary
                )
                if (complete) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = stringResource(R.string.collection_complete),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = Jade
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        relicSet.relics.forEach { relic ->
            val owned = recovered.contains(relic.id)
            val stateLabel = stringResource(
                if (owned) R.string.cd_open else R.string.cd_locked
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .semantics { stateDescription = stateLabel },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (owned) CHECK_GLYPH else DOT_GLYPH,
                        fontSize = 13.sp,
                        color = if (owned) Jade else TextMuted
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(relic.nameRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (owned) TextPrimary else TextMuted
                    )
                }
                if (!owned) {
                    Text(
                        text = stringResource(R.string.collection_missing),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
