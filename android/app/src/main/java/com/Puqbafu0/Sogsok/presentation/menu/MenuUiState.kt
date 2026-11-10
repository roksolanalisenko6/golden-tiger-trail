package com.Puqbafu0.Sogsok.presentation.menu

import com.Puqbafu0.Sogsok.core.config.GameConfig

data class MenuUiState(
    val unlockedLevel: Int = 0,
    val bestScore: Int = 0,
    val completedSets: Int = 0,
    val recovered: Int = 0,
    val totalRelics: Int = GameConfig.TOTAL_RELICS,
    val rank: Int = 1
)
