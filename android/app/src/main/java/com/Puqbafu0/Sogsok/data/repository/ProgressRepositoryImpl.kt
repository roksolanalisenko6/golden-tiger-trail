package com.Puqbafu0.Sogsok.data.repository

import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.data.local.PreferencesStore
import com.Puqbafu0.Sogsok.domain.model.PlayerProgress
import com.Puqbafu0.Sogsok.domain.model.RoundOutcome
import com.Puqbafu0.Sogsok.domain.model.RoundSummary
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val store: PreferencesStore) : ProgressRepository {

    override fun progress(): PlayerProgress {
        val stars = (0 until GameConfig.LEVELS).map { level ->
            store.readInt(KEY_STARS + level, 0).coerceIn(0, 3)
        }
        val recovered = decodeIds(store.readString(KEY_RECOVERED, ""))
        return PlayerProgress(
            unlockedLevel = store.readInt(KEY_UNLOCKED, 0).coerceIn(0, GameConfig.LEVELS - 1),
            stars = stars,
            bestScore = store.readInt(KEY_BEST_SCORE, 0),
            totalScore = store.readInt(KEY_TOTAL_SCORE, 0),
            bestMultiplierStep = store.readInt(KEY_BEST_STEP, 0),
            recovered = recovered,
            roundsPlayed = store.readInt(KEY_ROUNDS, 0)
        )
    }

    override fun applyRound(summary: RoundSummary): PlayerProgress {
        val current = progress()
        val previousStars = current.stars.getOrElse(summary.levelIndex) { 0 }
        if (summary.stars > previousStars) {
            store.writeInt(KEY_STARS + summary.levelIndex, summary.stars)
        }
        if (summary.outcome == RoundOutcome.WON) {
            val next = (summary.levelIndex + 1).coerceAtMost(GameConfig.LEVELS - 1)
            if (next > current.unlockedLevel) {
                store.writeInt(KEY_UNLOCKED, next)
            }
        }
        if (summary.score > current.bestScore) {
            store.writeInt(KEY_BEST_SCORE, summary.score)
        }
        store.writeInt(KEY_TOTAL_SCORE, current.totalScore + summary.score)
        if (summary.bestMultiplierStep > current.bestMultiplierStep) {
            store.writeInt(KEY_BEST_STEP, summary.bestMultiplierStep)
        }
        store.writeInt(KEY_ROUNDS, current.roundsPlayed + 1)
        val merged = current.recovered + summary.recoveredIds
        store.writeString(KEY_RECOVERED, encodeIds(merged))
        writeSummary(summary)
        return progress()
    }

    override fun lastRound(): RoundSummary? {
        if (store.readInt(KEY_LAST_PRESENT, 0) != 1) return null
        val outcome = if (store.readInt(KEY_LAST_WON, 0) == 1) {
            RoundOutcome.WON
        } else {
            RoundOutcome.LOST
        }
        return RoundSummary(
            levelIndex = store.readInt(KEY_LAST_LEVEL, 0),
            outcome = outcome,
            score = store.readInt(KEY_LAST_SCORE, 0),
            relicsFound = store.readInt(KEY_LAST_FOUND, 0),
            relicsTotal = store.readInt(KEY_LAST_TOTAL, 0),
            torchesLeft = store.readInt(KEY_LAST_TORCHES, 0),
            stars = store.readInt(KEY_LAST_STARS, 0),
            pairsLinked = store.readInt(KEY_LAST_PAIRS, 0),
            bestMultiplierStep = store.readInt(KEY_LAST_STEP, 0),
            recoveredIds = decodeIds(store.readString(KEY_LAST_IDS, "")).toList().sorted()
        )
    }

    override fun clear() {
        store.wipe(PREFIX)
    }

    private fun writeSummary(summary: RoundSummary) {
        store.writeInt(KEY_LAST_PRESENT, 1)
        store.writeInt(KEY_LAST_LEVEL, summary.levelIndex)
        store.writeInt(KEY_LAST_WON, if (summary.outcome == RoundOutcome.WON) 1 else 0)
        store.writeInt(KEY_LAST_SCORE, summary.score)
        store.writeInt(KEY_LAST_FOUND, summary.relicsFound)
        store.writeInt(KEY_LAST_TOTAL, summary.relicsTotal)
        store.writeInt(KEY_LAST_TORCHES, summary.torchesLeft)
        store.writeInt(KEY_LAST_STARS, summary.stars)
        store.writeInt(KEY_LAST_PAIRS, summary.pairsLinked)
        store.writeInt(KEY_LAST_STEP, summary.bestMultiplierStep)
        store.writeString(KEY_LAST_IDS, encodeIds(summary.recoveredIds.toSet()))
    }

    private fun encodeIds(ids: Set<Int>): String = ids.sorted().joinToString(separator = ",")

    private fun decodeIds(raw: String): Set<Int> {
        if (raw.isBlank()) return emptySet()
        return raw.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
    }

    private companion object {
        const val PREFIX = "trail_"
        const val KEY_UNLOCKED = "trail_unlocked"
        const val KEY_STARS = "trail_stars_"
        const val KEY_BEST_SCORE = "trail_best_score"
        const val KEY_TOTAL_SCORE = "trail_total_score"
        const val KEY_BEST_STEP = "trail_best_step"
        const val KEY_RECOVERED = "trail_recovered"
        const val KEY_ROUNDS = "trail_rounds"
        const val KEY_LAST_PRESENT = "trail_last_present"
        const val KEY_LAST_LEVEL = "trail_last_level"
        const val KEY_LAST_WON = "trail_last_won"
        const val KEY_LAST_SCORE = "trail_last_score"
        const val KEY_LAST_FOUND = "trail_last_found"
        const val KEY_LAST_TOTAL = "trail_last_total"
        const val KEY_LAST_TORCHES = "trail_last_torches"
        const val KEY_LAST_STARS = "trail_last_stars"
        const val KEY_LAST_PAIRS = "trail_last_pairs"
        const val KEY_LAST_STEP = "trail_last_step"
        const val KEY_LAST_IDS = "trail_last_ids"
    }
}
