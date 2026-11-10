package com.Puqbafu0.Sogsok.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.core.di.AppViewModelFactory
import com.Puqbafu0.Sogsok.core.theme.BgBase
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldDeep
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.GoldRim
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.OnGold
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight
import com.Puqbafu0.Sogsok.core.theme.StoneCold
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.SurfaceWarm
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextPrimary
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.SlabTile
import com.Puqbafu0.Sogsok.core.ui.TorchMeter
import com.Puqbafu0.Sogsok.core.ui.animalNameRes
import com.Puqbafu0.Sogsok.core.ui.formatScore
import com.Puqbafu0.Sogsok.core.ui.multiplierRes
import com.Puqbafu0.Sogsok.core.ui.relicSpriteRes
import com.Puqbafu0.Sogsok.data.sample.SampleData
import com.Puqbafu0.Sogsok.domain.model.BoardGeometry
import com.Puqbafu0.Sogsok.domain.model.SlabContent
import com.Puqbafu0.Sogsok.presentation.dialog.PauseDialog
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sin

@Composable
fun GameScreen(
    levelIndex: Int,
    attempt: Int,
    onRoundFinished: (Int) -> Unit,
    onRestart: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(levelIndex, attempt) {
        viewModel.load(levelIndex, attempt)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.RoundFinished -> onRoundFinished(event.levelIndex)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    BackHandler {
        if (state.paused) viewModel.closePause() else viewModel.openPause()
    }

    val torchesLost = state.torchesTotal - state.torches
    val shake = remember { Animatable(0f) }
    LaunchedEffect(torchesLost) {
        if (torchesLost > 0 && !state.reducedMotion) {
            shake.snapTo(0f)
            shake.animateTo(1f, tween(durationMillis = 260, easing = LinearEasing))
        }
    }
    val shakeOffset = if (shake.value < 1f) {
        sin(shake.value * PI.toFloat() * 6f) * (1f - shake.value) * 16f
    } else {
        0f
    }

    AppBackground(
        stops = listOf(
            BgBase.copy(alpha = 0.80f),
            SurfaceWarm.copy(alpha = 0.55f),
            BgBase.copy(alpha = 0.96f)
        ),
        artAlpha = 0.26f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                levelIndex = state.levelIndex,
                torches = state.torches,
                torchesTotal = state.torchesTotal,
                onPause = { viewModel.openPause() },
                onMenu = onMenu
            )

            StatStrip(
                score = state.score,
                relicsFound = state.relicsFound,
                relicsTotal = state.relicsTotal,
                multiplierStep = state.multiplierStep
            )

            ClueBanner(banner = state.banner)

            BuriedStrip(sets = state.buriedSets)

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                val frame = (GameConfig.BOARD_PAD_DP + GameConfig.BOARD_BORDER_DP).dp
                val gap = GameConfig.BOARD_GAP_DP.dp
                val available = min(
                    maxWidth - GameConfig.BOARD_SIDE_MARGIN_DP.dp,
                    GameConfig.BOARD_MAX_WIDTH_DP.dp
                )
                val inner = available - frame * 2 - gap * (GameConfig.GRID - 1)
                val tile = floor((inner / GameConfig.GRID).value).dp
                val boardWidth = tile * GameConfig.GRID + gap * (GameConfig.GRID - 1) + frame * 2
                Board(
                    state = state,
                    tile = tile,
                    gap = gap,
                    frame = frame,
                    boardWidth = boardWidth,
                    shakeOffset = shakeOffset,
                    onSlab = { index -> viewModel.onSlabTapped(index) }
                )
            }

            ControlBar(
                mode = state.mode,
                scans = state.scans,
                enabled = !state.finished && !state.paused,
                onMode = { mode -> viewModel.onModeSelected(mode) },
                onFlip = { viewModel.onFlip() }
            )
        }

        if (state.paused) {
            PauseDialog(
                levelIndex = state.levelIndex,
                torches = state.torches,
                relicsFound = state.relicsFound,
                onResume = { viewModel.closePause() },
                onRestart = {
                    viewModel.closePause()
                    onRestart(state.levelIndex)
                },
                onMenu = onMenu
            )
        }
    }
}

@Composable
private fun GameHeader(
    levelIndex: Int,
    torches: Int,
    torchesTotal: Int,
    onPause: () -> Unit,
    onMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(BgBase.copy(alpha = 0.78f))
            .padding(start = 14.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TorchMeter(lit = torches, total = torchesTotal)
        Text(
            text = stringResource(R.string.game_level, levelIndex + 1),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 18.sp,
            color = GoldLight
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
            TextButton(
                onClick = onMenu,
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.action_menu),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gold.copy(alpha = 0.20f))
    )
}

@Composable
private fun StatStrip(
    score: Int,
    relicsFound: Int,
    relicsTotal: Int,
    multiplierStep: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .background(BgBase.copy(alpha = 0.42f))
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatScore(score),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp,
            color = Gold,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )
        Text(
            text = stringResource(R.string.fmt_ratio, relicsFound, relicsTotal),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = Jade
        )
        Text(
            text = stringResource(multiplierRes(multiplierStep)),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            color = if (multiplierStep > 0) Ember else TextMuted
        )
    }
}

@Composable
private fun ClueBanner(banner: GameBanner) {
    val text = when (banner) {
        is GameBanner.Hint -> stringResource(R.string.game_hint)
        is GameBanner.PairLinked -> stringResource(R.string.game_banner_pair)
        is GameBanner.NoScans -> stringResource(R.string.game_banner_noscan)
        is GameBanner.Clue -> stringResource(
            R.string.game_banner_clue,
            banner.count,
            banner.label
        )
        is GameBanner.Quiet -> stringResource(R.string.game_banner_quiet, banner.label)
        is GameBanner.Track -> stringResource(
            R.string.game_banner_track,
            stringResource(animalNameRes(banner.animal)),
            banner.label
        )
        is GameBanner.RelicFound -> stringResource(
            R.string.game_banner_relic,
            stringResource(banner.nameRes)
        )
        is GameBanner.EmptySoil -> stringResource(R.string.game_banner_empty, banner.label)
        is GameBanner.Aimed -> stringResource(R.string.game_banner_aim, banner.label)
        is GameBanner.AlreadyOpen -> stringResource(R.string.game_banner_done, banner.label)
    }
    val accent = when (banner) {
        is GameBanner.RelicFound -> GoldLight
        is GameBanner.PairLinked -> Jade
        is GameBanner.EmptySoil -> Ember
        is GameBanner.NoScans -> Ember
        else -> TextSecondary
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(SurfaceWarm.copy(alpha = 0.92f), RoundedCornerShape(12.dp))
            .border(1.dp, Gold.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = accent,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun BuriedStrip(sets: List<Int>) {
    if (sets.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.game_buried),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            sets.forEach { setId ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Surface2.copy(alpha = 0.85f), RoundedCornerShape(10.dp))
                        .border(1.dp, Gold.copy(alpha = 0.28f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(relicSpriteRes(setId)),
                        contentDescription = stringResource(
                            R.string.cd_relic,
                            stringResource(SampleData.relicSet(setId).nameRes)
                        ),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Board(
    state: GameUiState,
    tile: Dp,
    gap: Dp,
    frame: Dp,
    boardWidth: Dp,
    shakeOffset: Float,
    onSlab: (Int) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWarm,
        border = BorderStroke(
            width = GameConfig.BOARD_BORDER_DP.dp,
            brush = Brush.linearGradient(listOf(Gold, GoldRim))
        ),
        shadowElevation = 16.dp,
        modifier = Modifier
            .width(boardWidth)
            .graphicsLayer { translationX = shakeOffset }
            .alpha(if (state.paused) 0.45f else 1f)
    ) {
        Box(modifier = Modifier.padding(frame)) {
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                for (row in 0 until GameConfig.GRID) {
                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                        for (column in 0 until GameConfig.GRID) {
                            val index = row * GameConfig.GRID + column
                            val slab = state.slabs.getOrNull(index)
                            if (slab != null) {
                                val content = slab.content
                                SlabTile(
                                    slab = slab,
                                    label = BoardGeometry.label(index),
                                    aimed = state.cursor == index && !state.finished,
                                    showLabel = state.showLabels,
                                    reducedMotion = state.reducedMotion,
                                    tileSize = tile,
                                    relicSetId = if (content is SlabContent.Buried) {
                                        SampleData.relic(content.relicId).setId
                                    } else {
                                        0
                                    },
                                    onTap = { onSlab(index) }
                                )
                            } else {
                                Box(modifier = Modifier.size(tile))
                            }
                        }
                    }
                }
            }
            val span = tile * GameConfig.GRID + gap * (GameConfig.GRID - 1)
            Canvas(modifier = Modifier.size(width = span, height = span)) {
                val step = tile.toPx() + gap.toPx()
                val half = tile.toPx() / 2f
                state.pairs.forEach { pair ->
                    val firstOpen = state.slabs.getOrNull(pair.first)?.linked == true
                    val secondOpen = state.slabs.getOrNull(pair.second)?.linked == true
                    if (firstOpen && secondOpen) {
                        val start = Offset(
                            BoardGeometry.column(pair.first) * step + half,
                            BoardGeometry.row(pair.first) * step + half
                        )
                        val end = Offset(
                            BoardGeometry.column(pair.second) * step + half,
                            BoardGeometry.row(pair.second) * step + half
                        )
                        drawLine(
                            color = Jade.copy(alpha = 0.75f),
                            start = start,
                            end = end,
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlBar(
    mode: PlayMode,
    scans: Int,
    enabled: Boolean,
    onMode: (PlayMode) -> Unit,
    onFlip: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Surface1.copy(alpha = 0.96f),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.28f)),
        shadowElevation = 20.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 6.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ModeSegment(
                    text = stringResource(R.string.game_mode_dig),
                    selected = mode == PlayMode.DIG,
                    accent = Gold,
                    enabled = enabled,
                    onClick = { onMode(PlayMode.DIG) },
                    modifier = Modifier.weight(1f)
                )
                ModeSegment(
                    text = stringResource(R.string.game_scan_count, scans),
                    selected = mode == PlayMode.SCAN,
                    accent = Jade,
                    enabled = enabled && scans > 0,
                    onClick = { onMode(PlayMode.SCAN) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onFlip,
                enabled = enabled,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldDeep,
                    contentColor = OnGold,
                    disabledContainerColor = StoneCold,
                    disabledContentColor = TextMuted
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(R.string.game_open),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun ModeSegment(
    text: String,
    selected: Boolean,
    accent: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stateLabel = stringResource(if (selected) R.string.settings_on else R.string.settings_off)
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) accent else Surface2,
            contentColor = if (selected) OnGold else TextPrimary,
            disabledContainerColor = StoneCold,
            disabledContentColor = TextMuted
        ),
        border = BorderStroke(1.dp, if (selected) accent else SlabHighlight),
        modifier = modifier
            .height(48.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}
