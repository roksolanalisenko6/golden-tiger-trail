package com.Puqbafu0.Sogsok.presentation.settings

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.domain.model.ExpeditionMode
import com.Puqbafu0.Sogsok.domain.model.GameSettings
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import com.Puqbafu0.Sogsok.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val settings = settingsRepository.settings()
        _state.update {
            it.copy(
                mode = settings.mode,
                slabLabels = settings.slabLabels,
                reducedMotion = settings.reducedMotion,
                confirmReset = false
            )
        }
    }

    fun onModeSelected(mode: ExpeditionMode) {
        _state.update { it.copy(mode = mode) }
        persist()
    }

    fun onSlabLabels(enabled: Boolean) {
        _state.update { it.copy(slabLabels = enabled) }
        persist()
    }

    fun onReducedMotion(enabled: Boolean) {
        _state.update { it.copy(reducedMotion = enabled) }
        persist()
    }

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        progressRepository.clear()
        _state.update { it.copy(confirmReset = false) }
        persist()
    }

    private fun persist() {
        val current = _state.value
        settingsRepository.store(
            GameSettings(
                mode = current.mode,
                slabLabels = current.slabLabels,
                reducedMotion = current.reducedMotion
            )
        )
    }
}
