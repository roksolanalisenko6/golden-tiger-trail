package com.Puqbafu0.Sogsok.presentation.result

data class ResultUiState(
    val levelIndex: Int = 0,
    val won: Boolean = false,
    val score: Int = 0,
    val relicsFound: Int = 0,
    val relicsTotal: Int = 0,
    val stars: Int = 0,
    val pairsLinked: Int = 0,
    val bestMultiplierStep: Int = 0,
    val recovered: List<Int> = emptyList(),
    val hasNextTrail: Boolean = false,
    val reducedMotion: Boolean = false
)
