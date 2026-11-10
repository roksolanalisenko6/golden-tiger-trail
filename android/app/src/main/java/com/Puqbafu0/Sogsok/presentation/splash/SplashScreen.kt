package com.Puqbafu0.Sogsok.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
import com.Puqbafu0.Sogsok.core.theme.BgDeep
import com.Puqbafu0.Sogsok.core.theme.BgNight
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.SurfaceWarm
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.PawGlyph
import com.Puqbafu0.Sogsok.domain.model.AnimalTrack

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    val transition = rememberInfiniteTransition(label = "splash_loop")
    val halo by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_halo"
    )
    val rock by transition.animateFloat(
        initialValue = -0.6f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_rock"
    )
    val trail by transition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splash_trail"
    )

    AppBackground(
        stops = listOf(BgDeep, Color(0xFF0B0B0F), BgNight),
        artAlpha = 0.30f
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SurfaceWarm.copy(alpha = 0.85f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 0.37f),
                    radius = size.minDimension * 0.62f
                ),
                radius = size.minDimension * 0.62f,
                center = Offset(size.width / 2f, size.height * 0.37f)
            )
            val vine = Gold.copy(alpha = 0.06f)
            for (step in 0 until 6) {
                val y = size.height * (0.12f + step * 0.14f)
                drawLine(
                    color = vine,
                    start = Offset(0f, y),
                    end = Offset(size.width * 0.22f, y + size.height * 0.05f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = vine,
                    start = Offset(size.width, y + size.height * 0.03f),
                    end = Offset(size.width * 0.80f, y + size.height * 0.08f),
                    strokeWidth = 2f
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(226.dp)
                        .alpha(halo)
                        .background(
                            Brush.radialGradient(
                                listOf(Gold.copy(alpha = 0.30f), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .graphicsLayer { rotationZ = rock }
                        .shadow(18.dp, CircleShape, ambientColor = Gold, spotColor = Gold)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Surface2, Color(0xFF141418), BgDeep)
                            )
                        )
                        .border(2.dp, Gold.copy(alpha = 0.55f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(136.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.brand_title),
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                )
                Text(
                    text = stringResource(R.string.brand_title),
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldLight,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val animals = listOf(
                    AnimalTrack.TIGER,
                    AnimalTrack.SERPENT,
                    AnimalTrack.MONKEY,
                    AnimalTrack.HERON,
                    AnimalTrack.TIGER
                )
                animals.forEachIndexed { index, animal ->
                    val lit = trail >= index.toFloat()
                    PawGlyph(
                        animal = animal,
                        tint = if (lit) Jade.copy(alpha = 0.85f) else Gold.copy(alpha = 0.18f),
                        modifier = Modifier
                            .size(22.dp)
                            .offset(y = (index % 2 * 8).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            LinearProgressIndicator(
                color = Gold,
                trackColor = Surface2,
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, Gold.copy(alpha = 0.35f), Color.Transparent)
                    )
                )
        )
    }
}
