package com.Puqbafu0.Sogsok.presentation.result

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.StoneCold
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.SurfaceEmber
import com.Puqbafu0.Sogsok.core.theme.SurfaceWarm
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.EmptyStateMessage
import com.Puqbafu0.Sogsok.core.ui.PrimaryButton
import com.Puqbafu0.Sogsok.core.ui.SecondaryButton
import com.Puqbafu0.Sogsok.core.ui.StarRow
import com.Puqbafu0.Sogsok.core.ui.StatItem
import com.Puqbafu0.Sogsok.core.ui.StatRow
import com.Puqbafu0.Sogsok.core.ui.formatScore
import com.Puqbafu0.Sogsok.core.ui.multiplierRes
import com.Puqbafu0.Sogsok.core.ui.relicSpriteRes
import com.Puqbafu0.Sogsok.data.sample.SampleData

@Composable
fun ResultScreen(
    levelIndex: Int,
    onReplay: (Int) -> Unit,
    onCollection: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: ResultViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(levelIndex) {
        viewModel.refresh(levelIndex)
    }

    val transition = rememberInfiniteTransition(label = "result_loop")
    val gleam by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "result_gleam"
    )
    val rotation = if (state.reducedMotion) 0f else gleam

    AppBackground(
        stops = if (state.won) {
            listOf(SurfaceWarm, Surface1, BgBase)
        } else {
            listOf(SurfaceEmber, Surface1, BgBase)
        },
        artAlpha = 0.18f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(170.dp)) {
                    val rays = 8
                    val radius = size.minDimension / 2f
                    for (ray in 0 until rays) {
                        val angle = Math.toRadians(
                            (rotation + ray * (360f / rays)).toDouble()
                        )
                        val dx = kotlin.math.cos(angle).toFloat()
                        val dy = kotlin.math.sin(angle).toFloat()
                        drawLine(
                            color = if (state.won) {
                                Gold.copy(alpha = 0.30f)
                            } else {
                                StoneCold.copy(alpha = 0.40f)
                            },
                            start = Offset(
                                center.x + dx * radius * 0.52f,
                                center.y + dy * radius * 0.52f
                            ),
                            end = Offset(
                                center.x + dx * radius,
                                center.y + dy * radius
                            ),
                            strokeWidth = 7f,
                            cap = StrokeCap.Round
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .background(
                            Brush.verticalGradient(
                                if (state.won) {
                                    listOf(SurfaceWarm, Surface2)
                                } else {
                                    listOf(StoneCold, Surface2)
                                }
                            ),
                            CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (state.won) Gold else StoneCold,
                            shape = CircleShape
                        )
                        .graphicsLayer { rotationZ = 0f },
                    contentAlignment = Alignment.Center
                ) {
                    val heroId = state.recovered.lastOrNull()
                    if (heroId != null) {
                        Image(
                            painter = painterResource(
                                relicSpriteRes(SampleData.relic(heroId).setId)
                            ),
                            contentDescription = stringResource(
                                R.string.cd_relic,
                                stringResource(SampleData.relic(heroId).nameRes)
                            ),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(72.dp)
                        )
                    } else {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawRoundRect(
                                color = TextMuted.copy(alpha = 0.6f),
                                topLeft = Offset(size.width * 0.42f, size.height * 0.30f),
                                size = androidx.compose.ui.geometry.Size(
                                    size.width * 0.16f,
                                    size.height * 0.62f
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                            )
                            drawCircle(
                                color = Ember.copy(alpha = 0.35f),
                                radius = size.minDimension * 0.17f,
                                center = Offset(size.width * 0.5f, size.height * 0.24f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    if (state.won) R.string.result_win else R.string.result_lose
                ),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = if (state.won) GoldLight else Ember,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            StarRow(stars = state.stars, glyphSize = 30.sp, spacing = 8)

            Spacer(modifier = Modifier.height(16.dp))

            StatRow(
                items = listOf(
                    StatItem(
                        label = stringResource(R.string.stat_score),
                        value = formatScore(state.score),
                        accent = Gold
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_relics),
                        value = stringResource(
                            R.string.fmt_ratio,
                            state.relicsFound,
                            state.relicsTotal
                        ),
                        accent = GoldLight
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_pairs),
                        value = state.pairsLinked.toString(),
                        accent = Jade,
                        visible = state.pairsLinked > 0
                    ),
                    StatItem(
                        label = stringResource(R.string.stat_streak),
                        value = stringResource(multiplierRes(state.bestMultiplierStep)),
                        accent = Ember,
                        visible = state.bestMultiplierStep > 0
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.result_recovered),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (state.recovered.isEmpty()) {
                    EmptyStateMessage(text = stringResource(R.string.result_nothing))
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        state.recovered.forEach { relicId ->
                            val relic = SampleData.relic(relicId)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            Surface2.copy(alpha = 0.9f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            1.dp,
                                            Gold.copy(alpha = 0.35f),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(
                                            relicSpriteRes(relic.setId)
                                        ),
                                        contentDescription = stringResource(
                                            R.string.cd_relic,
                                            stringResource(relic.nameRes)
                                        ),
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(relic.nameRes),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 8.sp,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    modifier = Modifier.size(width = 60.dp, height = 24.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (state.hasNextTrail) {
                PrimaryButton(
                    text = stringResource(R.string.result_next),
                    onClick = { onReplay(state.levelIndex + 1) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = stringResource(R.string.result_again),
                    onClick = { onReplay(state.levelIndex) },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    text = stringResource(R.string.result_again),
                    onClick = { onReplay(state.levelIndex) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.menu_collection),
                    onClick = onCollection,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.action_menu),
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}
