package com.Puqbafu0.Sogsok.presentation.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.core.di.AppViewModelFactory
import com.Puqbafu0.Sogsok.core.theme.BgBase
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight
import com.Puqbafu0.Sogsok.core.theme.StoneCold
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.HeaderChip
import com.Puqbafu0.Sogsok.core.ui.ScreenHeader
import com.Puqbafu0.Sogsok.core.ui.StarRow
import com.Puqbafu0.Sogsok.core.ui.SecondaryButton

@Composable
fun LevelsScreen(
    onOpenTrail: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: LevelsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    AppBackground(
        stops = listOf(BgBase, Surface1.copy(alpha = 0.92f), BgBase),
        artAlpha = 0.22f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.levels_title),
                onBack = onMenu,
                trailing = {
                    HeaderChip(
                        text = stringResource(
                            R.string.fmt_ratio,
                            state.unlockedLevel + 1,
                            GameConfig.LEVELS
                        ),
                        accent = Gold
                    )
                }
            )

            Text(
                text = stringResource(R.string.levels_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 6.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items((0 until GameConfig.LEVELS).toList()) { levelIndex ->
                    TrailTile(
                        levelIndex = levelIndex,
                        unlocked = levelIndex <= state.unlockedLevel,
                        stars = state.stars.getOrElse(levelIndex) { 0 },
                        relics = state.relicsPerLevel.getOrElse(levelIndex) { 3 },
                        onOpen = { onOpenTrail(levelIndex) },
                        onLocked = { viewModel.onLockedTrail(levelIndex) }
                    )
                }
            }

            val notice = state.lockedNotice
            if (notice != null) {
                Text(
                    text = stringResource(R.string.levels_locked, notice),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = Ember,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }

            SecondaryButton(
                text = stringResource(R.string.action_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 6.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun TrailTile(
    levelIndex: Int,
    unlocked: Boolean,
    stars: Int,
    relics: Int,
    onOpen: () -> Unit,
    onLocked: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val lockedLabel = stringResource(R.string.cd_locked)
    val openLabel = stringResource(R.string.cd_open)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(
                if (unlocked) {
                    Brush.verticalGradient(listOf(Surface2, Surface1))
                } else {
                    Brush.verticalGradient(listOf(StoneCold, Surface1))
                },
                shape
            )
            .border(
                width = 1.dp,
                color = if (unlocked) Gold.copy(alpha = 0.45f) else SlabHighlight,
                shape = shape
            )
            .alpha(if (unlocked) 1f else 0.55f)
            .clickable { if (unlocked) onOpen() else onLocked() }
            .semantics { stateDescription = if (unlocked) openLabel else lockedLabel },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.fmt_level, levelIndex + 1),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = if (unlocked) GoldLight else TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (unlocked) {
                StarRow(stars = stars)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.fmt_ratio, relics, relics),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = Jade
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(10.dp)
                        .background(TextMuted.copy(alpha = 0.35f), RoundedCornerShape(3.dp))
                )
            }
        }
    }
}
