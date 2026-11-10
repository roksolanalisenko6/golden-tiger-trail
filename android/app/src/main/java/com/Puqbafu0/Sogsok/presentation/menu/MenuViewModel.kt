package com.Puqbafu0.Sogsok.presentation.menu

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.progress()
        _state.update {
            it.copy(
                unlockedLevel = progress.unlockedLevel,
                bestScore = progress.bestScore,
                completedSets = progress.completedSets,
                recovered = progress.recovered.size,
                rank = progress.levelsCleared + 1
            )
        }
    }
}
