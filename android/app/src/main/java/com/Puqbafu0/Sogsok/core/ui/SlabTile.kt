package com.Puqbafu0.Sogsok.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.PitDark
import com.Puqbafu0.Sogsok.core.theme.SlabFace
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight
import com.Puqbafu0.Sogsok.core.theme.SlabShade
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.SurfaceWarm
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.domain.model.Slab
import com.Puqbafu0.Sogsok.domain.model.SlabContent
import com.Puqbafu0.Sogsok.domain.model.SlabState

@Composable
fun SlabTile(
    slab: Slab,
    label: String,
    aimed: Boolean,
    showLabel: Boolean,
    reducedMotion: Boolean,
    tileSize: Dp,
    relicSetId: Int,
    onTap: () -> Unit
) {
    val opened = slab.state != SlabState.HIDDEN
    val angle by animateFloatAsState(
        targetValue = if (opened) 180f else 0f,
        animationSpec = if (reducedMotion) {
            tween(durationMillis = 90)
        } else {
            tween(durationMillis = 280, easing = FastOutSlowInEasing)
        },
        label = "slab_flip"
    )
    val front = angle < 90f
    val shape = RoundedCornerShape(10.dp)
    val content = slab.content
    val stateText = when {
        !opened -> stringResource(R.string.cd_slab_hidden)
        slab.state == SlabState.DUG && content is SlabContent.Buried ->
            stringResource(R.string.cd_slab_relic)
        slab.state == SlabState.DUG -> stringResource(R.string.cd_slab_empty)
        content is SlabContent.Paw ->
            stringResource(R.string.cd_slab_track, stringResource(animalNameRes(content.animal)))
        else -> stringResource(R.string.cd_slab_clue, slab.clue)
    }
    val slabDescription = stringResource(R.string.cd_slab, label)
    val aimedText = stringResource(R.string.cd_slab_aimed)
    Box(
        modifier = Modifier
            .size(tileSize)
            .graphicsLayer {
                rotationY = if (front) angle else angle - 180f
                cameraDistance = 16f * density
            }
            .clip(shape)
            .clickable(onClick = onTap)
            .semantics {
                contentDescription = slabDescription
                stateDescription = if (aimed) aimedText else stateText
            },
        contentAlignment = Alignment.Center
    ) {
        if (front) {
            HiddenFace(shape = shape, aimed = aimed, label = label, showLabel = showLabel)
        } else {
            OpenFace(
                slab = slab,
                shape = shape,
                aimed = aimed,
                label = label,
                showLabel = showLabel,
                relicSetId = relicSetId
            )
        }
    }
}

@Composable
private fun HiddenFace(
    shape: RoundedCornerShape,
    aimed: Boolean,
    label: String,
    showLabel: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(SlabHighlight.copy(alpha = 0.55f), SlabFace, SlabShade)
                )
            )
            .border(
                width = if (aimed) 2.dp else 1.dp,
                color = if (aimed) GoldLight else SlabShade,
                shape = shape
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(top = 3.dp)
                .height(2.dp)
                .background(SlabHighlight.copy(alpha = 0.6f))
        )
        if (showLabel) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextMuted,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun OpenFace(
    slab: Slab,
    shape: RoundedCornerShape,
    aimed: Boolean,
    label: String,
    showLabel: Boolean,
    relicSetId: Int
) {
    val content = slab.content
    val dug = slab.state == SlabState.DUG
    val relic = content as? SlabContent.Buried
    val paw = content as? SlabContent.Paw
    val background = when {
        dug && relic != null -> Brush.verticalGradient(listOf(SurfaceWarm, SlabShade))
        dug -> Brush.verticalGradient(listOf(PitDark, Color.Black))
        else -> Brush.verticalGradient(listOf(Surface2, SlabShade))
    }
    val borderColor = when {
        dug && relic != null -> Gold
        slab.linked -> Jade
        aimed -> GoldLight
        else -> SlabHighlight
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(background)
            .border(
                width = if (dug && relic != null || slab.linked || aimed) 2.dp else 1.dp,
                color = borderColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            dug && relic != null -> Image(
                painter = painterResource(relicSpriteRes(relicSetId)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
            dug -> Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Ember.copy(alpha = 0.45f), RoundedCornerShape(3.dp))
            )
            paw != null -> PawGlyph(
                animal = paw.animal,
                tint = if (slab.linked) Jade else Jade.copy(alpha = 0.72f),
                modifier = Modifier.size(26.dp)
            )
            else -> Text(
                text = slab.clue.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = clueColor(slab.clue)
            )
        }
        if (showLabel) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextMuted,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            )
        }
    }
}

private fun clueColor(clue: Int): Color = when (clue) {
    0 -> TextMuted
    1 -> GoldLight
    2 -> Gold
    else -> Ember
}
