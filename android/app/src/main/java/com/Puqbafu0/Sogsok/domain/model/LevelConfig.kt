package com.Puqbafu0.Sogsok.domain.model

import com.Puqbafu0.Sogsok.core.config.GameConfig

data class LevelConfig(
    val index: Int,
    val relics: Int,
    val trackPairs: Int,
    val scans: Int,
    val torches: Int
)

object LevelTable {

    fun configFor(levelIndex: Int): LevelConfig {
        val safe = levelIndex.coerceIn(0, GameConfig.LEVELS - 1)
        val band = safe / 3
        val relics = if (band >= 2) 4 else 3
        val trackPairs = when (band) {
            0 -> 4
            1 -> 3
            2 -> 3
            else -> 2
        }
        val scans = when (band) {
            0 -> 5
            1 -> 4
            2 -> 3
            else -> 2
        }
        return LevelConfig(
            index = safe,
            relics = relics,
            trackPairs = trackPairs,
            scans = scans,
            torches = GameConfig.TORCHES
        )
    }
}
