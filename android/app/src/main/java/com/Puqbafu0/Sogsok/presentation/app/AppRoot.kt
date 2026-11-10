package com.Puqbafu0.Sogsok.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Puqbafu0.Sogsok.core.di.AppViewModelFactory
import com.Puqbafu0.Sogsok.core.navigation.Screen
import com.Puqbafu0.Sogsok.core.theme.BgBase
import com.Puqbafu0.Sogsok.presentation.collection.CollectionScreen
import com.Puqbafu0.Sogsok.presentation.game.GameScreen
import com.Puqbafu0.Sogsok.presentation.levels.LevelsScreen
import com.Puqbafu0.Sogsok.presentation.menu.MenuScreen
import com.Puqbafu0.Sogsok.presentation.progress.ProgressScreen
import com.Puqbafu0.Sogsok.presentation.result.ResultScreen
import com.Puqbafu0.Sogsok.presentation.settings.SettingsScreen
import com.Puqbafu0.Sogsok.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack && screen !is Screen.Game) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                if (targetState is Screen.Game) {
                    (slideInVertically(tween(300)) { height -> height / 12 } + fadeIn(tween(300)))
                        .togetherWith(fadeOut(tween(200)))
                } else {
                    fadeIn(tween(260)) togetherWith fadeOut(tween(200))
                }
            },
            label = "app_screen"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.onSplashFinished() }
                )

                is Screen.Menu -> MenuScreen(
                    onPlay = { levelIndex -> appViewModel.openTrail(levelIndex) },
                    onLevels = { appViewModel.openRoute(Screen.Levels) },
                    onCollection = { appViewModel.openRoute(Screen.Collection) },
                    onProgress = { appViewModel.openRoute(Screen.Progress) },
                    onSettings = { appViewModel.openRoute(Screen.Settings) }
                )

                is Screen.Levels -> LevelsScreen(
                    onOpenTrail = { levelIndex -> appViewModel.openTrail(levelIndex) },
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Game -> GameScreen(
                    levelIndex = current.levelIndex,
                    attempt = current.attempt,
                    onRoundFinished = { levelIndex -> appViewModel.showResult(levelIndex) },
                    onRestart = { levelIndex -> appViewModel.replayTrail(levelIndex) },
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Result -> ResultScreen(
                    levelIndex = current.levelIndex,
                    onReplay = { levelIndex -> appViewModel.replayTrail(levelIndex) },
                    onCollection = { appViewModel.openRoute(Screen.Collection) },
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Collection -> CollectionScreen(
                    onMenu = { appViewModel.back() }
                )

                is Screen.Progress -> ProgressScreen(
                    onMenu = { appViewModel.back() }
                )

                is Screen.Settings -> SettingsScreen(
                    onMenu = { appViewModel.back() }
                )
            }
        }
    }
}
