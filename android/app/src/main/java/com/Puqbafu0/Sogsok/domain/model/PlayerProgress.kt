package com.Puqbafu0.Sogsok.domain.model

import com.Puqbafu0.Sogsok.core.config.GameConfig

data class PlayerProgress(
    val unlockedLevel: Int,
    val stars: List<Int>,
    val bestScore: Int,
    val totalScore: Int,
    val bestMultiplierStep: Int,
    val recovered: Set<Int>,
    val roundsPlayed: Int
) {

    val starsEarned: Int
        get() = stars.sum()

    val starsPossible: Int
        get() = GameConfig.LEVELS * 3

    val levelsCleared: Int
        get() = stars.count { it > 0 }

    val completedSets: Int
        get() = (0 until GameConfig.RELIC_SETS).count { setId -> setCompleted(setId) }

    val scanBonus: Int
        get() = completedSets.coerceAtMost(GameConfig.MAX_SET_BONUS)

    fun setCompleted(setId: Int): Boolean {
        val first = setId * GameConfig.RELICS_PER_SET
        return (first until first + GameConfig.RELICS_PER_SET).all { recovered.contains(it) }
    }

    fun isUnlocked(levelIndex: Int): Boolean = levelIndex <= unlockedLevel

    companion object {
        fun empty(): PlayerProgress = PlayerProgress(
            unlockedLevel = 0,
            stars = List(GameConfig.LEVELS) { 0 },
            bestScore = 0,
            totalScore = 0,
            bestMultiplierStep = 0,
            recovered = emptySet(),
            roundsPlayed = 0
        )
    }
}
