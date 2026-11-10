package com.Puqbafu0.Sogsok.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Puqbafu0.Sogsok.presentation.app.AppViewModel
import com.Puqbafu0.Sogsok.presentation.collection.CollectionViewModel
import com.Puqbafu0.Sogsok.presentation.game.GameViewModel
import com.Puqbafu0.Sogsok.presentation.levels.LevelsViewModel
import com.Puqbafu0.Sogsok.presentation.menu.MenuViewModel
import com.Puqbafu0.Sogsok.presentation.progress.ProgressViewModel
import com.Puqbafu0.Sogsok.presentation.result.ResultViewModel
import com.Puqbafu0.Sogsok.presentation.settings.SettingsViewModel
import com.Puqbafu0.Sogsok.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val progress = ServiceLocator.progressRepository
        val settings = ServiceLocator.settingsRepository
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(progress)
            modelClass.isAssignableFrom(LevelsViewModel::class.java) -> LevelsViewModel(progress)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                progressRepository = progress,
                settingsRepository = settings,
                generateBoard = ServiceLocator.generateBoard,
                scanSlab = ServiceLocator.scanSlab,
                digSlab = ServiceLocator.digSlab,
                scoreRound = ServiceLocator.scoreRound
            )
            modelClass.isAssignableFrom(ResultViewModel::class.java) ->
                ResultViewModel(progress, settings)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(progress)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(progress)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settings, progress)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
