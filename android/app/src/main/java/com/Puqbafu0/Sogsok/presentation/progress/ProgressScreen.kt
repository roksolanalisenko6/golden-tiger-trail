package com.Puqbafu0.Sogsok.presentation.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
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
import com.Puqbafu0.Sogsok.core.theme.StoneCold
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.EmptyStateMessage
import com.Puqbafu0.Sogsok.core.ui.ScreenHeader
import com.Puqbafu0.Sogsok.core.ui.SecondaryButton
import com.Puqbafu0.Sogsok.core.ui.StatGrid
import com.Puqbafu0.Sogsok.core.ui.StatItem
import com.Puqbafu0.Sogsok.core.ui.formatScore
import com.Puqbafu0.Sogsok.core.ui.multiplierRes
import kotlin.math.roundToInt

@Composable
fun ProgressScreen(onMenu: () -> Unit) {
    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val sweep by animateFloatAsState(
        targetValue = state.completion,
        animationSpec = tween(durationMillis = 900),
        label = "progress_arc"
    )

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
                title = stringResource(R.string.progress_title),
                onBack = onMenu
            )

            if (state.roundsPlayed == 0) {
                EmptyStateMessage(text = stringResource(R.string.progress_empty))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    val stroke = 14.dp.toPx()
                    val inset = stroke / 2f
                    drawArc(
                        color = Surface2,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = Size(size.width - stroke, size.height - stroke),
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(Gold, GoldLight, Ember, Gold)),
                        startAngle = 135f,
                        sweepAngle = 270f * sweep.coerceIn(0f, 1f),
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = Size(size.width - stroke, size.height - stroke),
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(
                            R.string.fmt_percent,
                            (state.completion * 100f).roundToInt()
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldLight
                    )
                    Text(
                        text = stringResource(R.string.progress_complete),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            StatGrid(
                columns = 3,
                items = listOf(
                    StatItem(
                        label = stringResource(R.string.stat_best),
                        value = formatScore(state.bestScore),
                        accent = Gold,
                        visible = state.bestScore > 0
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_total),
                        value = formatScore(state.totalScore),
                        accent = GoldLight,
                        visible = state.totalScore > 0
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_cleared),
                        value = state.levelsCleared.toString(),
                        accent = Jade,
                        visible = state.levelsCleared > 0
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_stars),
                        value = stringResource(
                            R.string.fmt_ratio,
                            state.starsEarned,
                            state.starsPossible
                        ),
                        accent = Gold
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_relics),
                        value = stringResource(
                            R.string.fmt_ratio,
                            state.recovered,
                            state.totalRelics
                        ),
                        accent = Jade
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_rounds),
                        value = state.roundsPlayed.toString(),
                        accent = Ember,
                        visible = state.roundsPlayed > 0
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_streak),
                        value = stringResource(multiplierRes(state.bestMultiplierStep)),
                        accent = Ember,
                        visible = state.bestMultiplierStep > 0
                    )
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.progress_trails),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                for (level in 0 until GameConfig.LEVELS) {
                    val stars = state.stars.getOrElse(level) { 0 }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .height((14 + stars * 16).dp)
                                .background(
                                    if (stars > 0) Gold else StoneCold,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.fmt_level, level + 1),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            SecondaryButton(
                text = stringResource(R.string.action_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp, bottom = 16.dp)
            )
        }
    }
}
