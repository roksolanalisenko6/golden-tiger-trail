package com.Puqbafu0.Sogsok.presentation.game

import com.Puqbafu0.Sogsok.domain.model.AnimalTrack
import com.Puqbafu0.Sogsok.domain.model.Slab
import com.Puqbafu0.Sogsok.domain.model.TrackPair

enum class PlayMode {
    DIG,
    SCAN
}

sealed interface GameBanner {
    data object Hint : GameBanner
    data object PairLinked : GameBanner
    data object NoScans : GameBanner
    data class Clue(val count: Int, val label: String) : GameBanner
    data class Quiet(val label: String) : GameBanner
    data class Track(val animal: AnimalTrack, val label: String) : GameBanner
    data class RelicFound(val nameRes: Int) : GameBanner
    data class EmptySoil(val label: String) : GameBanner
    data class Aimed(val label: String) : GameBanner
    data class AlreadyOpen(val label: String) : GameBanner
}

sealed interface GameEvent {
    data class RoundFinished(val levelIndex: Int) : GameEvent
}

data class GameUiState(
    val levelIndex: Int = 0,
    val attempt: Int = 0,
    val started: Boolean = false,
    val slabs: List<Slab> = emptyList(),
    val pairs: List<TrackPair> = emptyList(),
    val buriedSets: List<Int> = emptyList(),
    val cursor: Int = 12,
    val mode: PlayMode = PlayMode.DIG,
    val torches: Int = 3,
    val torchesTotal: Int = 3,
    val scans: Int = 0,
    val relicsFound: Int = 0,
    val relicsTotal: Int = 3,
    val pairsLinked: Int = 0,
    val score: Int = 0,
    val multiplierStep: Int = 0,
    val bestMultiplierStep: Int = 0,
    val banner: GameBanner = GameBanner.Hint,
    val paused: Boolean = false,
    val finished: Boolean = false,
    val showLabels: Boolean = false,
    val reducedMotion: Boolean = false
)
