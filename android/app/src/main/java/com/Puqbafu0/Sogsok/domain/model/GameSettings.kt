package com.Puqbafu0.Sogsok.domain.model

enum class ExpeditionMode {
    GUIDED,
    EXPLORER
}

data class GameSettings(
    val mode: ExpeditionMode,
    val slabLabels: Boolean,
    val reducedMotion: Boolean
) {
    companion object {
        fun default(): GameSettings = GameSettings(
            mode = ExpeditionMode.GUIDED,
            slabLabels = false,
            reducedMotion = false
        )
    }
}
