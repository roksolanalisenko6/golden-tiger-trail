package com.Puqbafu0.Sogsok.presentation.progress

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.progress()
        _state.value = ProgressUiState(
            bestScore = progress.bestScore,
            totalScore = progress.totalScore,
            levelsCleared = progress.levelsCleared,
            starsEarned = progress.starsEarned,
            starsPossible = progress.starsPossible,
            recovered = progress.recovered.size,
            totalRelics = GameConfig.TOTAL_RELICS,
            bestMultiplierStep = progress.bestMultiplierStep,
            roundsPlayed = progress.roundsPlayed,
            stars = progress.stars
        )
    }
}
