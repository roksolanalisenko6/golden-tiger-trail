package com.Puqbafu0.Sogsok.presentation.settings

import com.Puqbafu0.Sogsok.domain.model.ExpeditionMode

data class SettingsUiState(
    val mode: ExpeditionMode = ExpeditionMode.GUIDED,
    val slabLabels: Boolean = false,
    val reducedMotion: Boolean = false,
    val confirmReset: Boolean = false
)
