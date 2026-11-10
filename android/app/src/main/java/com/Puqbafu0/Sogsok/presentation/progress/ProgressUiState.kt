package com.Puqbafu0.Sogsok.presentation.progress

data class ProgressUiState(
    val bestScore: Int = 0,
    val totalScore: Int = 0,
    val levelsCleared: Int = 0,
    val starsEarned: Int = 0,
    val starsPossible: Int = 36,
    val recovered: Int = 0,
    val totalRelics: Int = 15,
    val bestMultiplierStep: Int = 0,
    val roundsPlayed: Int = 0,
    val stars: List<Int> = emptyList()
) {
    val completion: Float
        get() = if (starsPossible == 0) 0f else starsEarned.toFloat() / starsPossible.toFloat()
}
