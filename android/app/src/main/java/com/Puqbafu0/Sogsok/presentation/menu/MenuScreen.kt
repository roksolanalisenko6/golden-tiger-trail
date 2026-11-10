package com.Puqbafu0.Sogsok.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.EmptyStateMessage
import com.Puqbafu0.Sogsok.core.ui.HeaderChip
import com.Puqbafu0.Sogsok.core.ui.PrimaryButton
import com.Puqbafu0.Sogsok.core.ui.SecondaryButton
import com.Puqbafu0.Sogsok.core.ui.StatItem
import com.Puqbafu0.Sogsok.core.ui.StatRow
import com.Puqbafu0.Sogsok.core.ui.formatScore

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val transition = rememberInfiniteTransition(label = "menu_loop")
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menu_breathe"
    )

    AppBackground(
        stops = listOf(
            Color.Transparent,
            BgBase.copy(alpha = 0.45f),
            BgBase.copy(alpha = 0.92f),
            BgBase
        ),
        artAlpha = 0.95f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderChip(
                    text = stringResource(R.string.menu_rank, state.rank),
                    accent = Gold
                )
                HeaderChip(
                    text = stringResource(
                        R.string.fmt_ratio,
                        state.recovered,
                        state.totalRelics
                    ),
                    accent = Jade
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(Gold.copy(alpha = 0.22f), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            scaleX = breathe
                            scaleY = breathe
                        }
                )
            }

            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Surface1,
                shadowElevation = 24.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Gold.copy(alpha = 0.22f),
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                        .padding(horizontal = 18.dp)
                        .padding(top = 10.dp, bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .height(2.dp)
                            .fillMaxWidth(0.42f)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Gold, Color.Transparent)
                                )
                            )
                    )

                    Text(
                        text = stringResource(R.string.brand_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldLight,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val stats = listOf(
                        StatItem(
                            label = stringResource(R.string.stat_best),
                            value = formatScore(state.bestScore),
                            accent = Gold,
                            visible = state.bestScore > 0
                        ),
                        StatItem(
                            label = stringResource(R.string.stat_trail),
                            value = (state.unlockedLevel + 1).toString(),
                            accent = GoldLight
                        ),
                        StatItem(
                            label = stringResource(R.string.stat_sets),
                            value = state.completedSets.toString(),
                            accent = Jade,
                            visible = state.completedSets > 0
                        )
                    )
                    if (stats.count { it.visible } >= 2) {
                        StatRow(items = stats)
                    } else {
                        EmptyStateMessage(text = stringResource(R.string.menu_empty))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PrimaryButton(
                        text = stringResource(R.string.menu_play),
                        onClick = { onPlay(state.unlockedLevel) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(R.string.menu_continue, state.unlockedLevel + 1),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.menu_levels),
                            onClick = onLevels,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.menu_collection),
                            onClick = onCollection,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.menu_progress),
                            onClick = onProgress,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.menu_settings),
                            onClick = onSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
