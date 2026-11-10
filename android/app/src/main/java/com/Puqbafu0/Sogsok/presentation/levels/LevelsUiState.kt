package com.Puqbafu0.Sogsok.presentation.levels

data class LevelsUiState(
    val unlockedLevel: Int = 0,
    val stars: List<Int> = emptyList(),
    val relicsPerLevel: List<Int> = emptyList(),
    val lockedNotice: Int? = null
)
