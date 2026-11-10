package com.Puqbafu0.Sogsok.domain.usecase

import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.RoundOutcome
import com.Puqbafu0.Sogsok.domain.model.RoundSummary

class ScoreRoundUseCase {

    operator fun invoke(
        levelIndex: Int,
        outcome: RoundOutcome,
        runningScore: Int,
        torchesLeft: Int,
        relicsFound: Int,
        relicsTotal: Int,
        pairsLinked: Int,
        bestMultiplierStep: Int,
        recoveredIds: List<Int>
    ): RoundSummary {
        val won = outcome == RoundOutcome.WON
        val bonus = if (won) GameConfig.CLEAR_BONUS_PER_TORCH * torchesLeft else 0
        val stars = if (won) torchesLeft.coerceIn(0, 3) else 0
        return RoundSummary(
            levelIndex = levelIndex,
            outcome = outcome,
            score = runningScore + bonus,
            relicsFound = relicsFound,
            relicsTotal = relicsTotal,
            torchesLeft = torchesLeft,
            stars = stars,
            pairsLinked = pairsLinked,
            bestMultiplierStep = bestMultiplierStep,
            recoveredIds = recoveredIds
        )
    }
}
