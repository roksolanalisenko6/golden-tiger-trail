package com.Puqbafu0.Sogsok.presentation.result

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.LevelTable
import com.Puqbafu0.Sogsok.domain.model.RoundOutcome
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import com.Puqbafu0.Sogsok.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultUiState())
    val state: StateFlow<ResultUiState> = _state.asStateFlow()

    fun refresh(levelIndex: Int) {
        val summary = progressRepository.lastRound()
        val settings = settingsRepository.settings()
        if (summary == null) {
            _state.value = ResultUiState(
                levelIndex = levelIndex,
                relicsTotal = LevelTable.configFor(levelIndex).relics,
                hasNextTrail = levelIndex + 1 < GameConfig.LEVELS,
                reducedMotion = settings.reducedMotion
            )
            return
        }
        _state.value = ResultUiState(
            levelIndex = summary.levelIndex,
            won = summary.outcome == RoundOutcome.WON,
            score = summary.score,
            relicsFound = summary.relicsFound,
            relicsTotal = summary.relicsTotal,
            stars = summary.stars,
            pairsLinked = summary.pairsLinked,
            bestMultiplierStep = summary.bestMultiplierStep,
            recovered = summary.recoveredIds,
            hasNextTrail = summary.outcome == RoundOutcome.WON &&
                summary.levelIndex + 1 < GameConfig.LEVELS,
            reducedMotion = settings.reducedMotion
        )
    }
}
