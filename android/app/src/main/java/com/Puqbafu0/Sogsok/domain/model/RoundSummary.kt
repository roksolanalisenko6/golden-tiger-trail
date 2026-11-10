package com.Puqbafu0.Sogsok.domain.model

enum class RoundOutcome {
    WON,
    LOST
}

data class RoundSummary(
    val levelIndex: Int,
    val outcome: RoundOutcome,
    val score: Int,
    val relicsFound: Int,
    val relicsTotal: Int,
    val torchesLeft: Int,
    val stars: Int,
    val pairsLinked: Int,
    val bestMultiplierStep: Int,
    val recoveredIds: List<Int>
)
