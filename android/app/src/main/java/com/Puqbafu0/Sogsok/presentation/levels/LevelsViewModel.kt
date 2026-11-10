package com.Puqbafu0.Sogsok.presentation.levels

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.LevelTable
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LevelsViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelsUiState())
    val state: StateFlow<LevelsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.progress()
        _state.update {
            it.copy(
                unlockedLevel = progress.unlockedLevel,
                stars = progress.stars,
                relicsPerLevel = (0 until GameConfig.LEVELS).map { level ->
                    LevelTable.configFor(level).relics
                },
                lockedNotice = null
            )
        }
    }

    fun onLockedTrail(levelIndex: Int) {
        _state.update { it.copy(lockedNotice = levelIndex) }
    }

    fun dismissNotice() {
        _state.update { it.copy(lockedNotice = null) }
    }
}
