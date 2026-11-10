package com.Puqbafu0.Sogsok.presentation.collection

import com.Puqbafu0.Sogsok.core.config.GameConfig

data class CollectionUiState(
    val recovered: Set<Int> = emptySet(),
    val totalRelics: Int = GameConfig.TOTAL_RELICS,
    val completedSets: Int = 0
)
