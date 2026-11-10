package com.Puqbafu0.Sogsok.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.data.sample.SampleData
import com.Puqbafu0.Sogsok.domain.model.BoardGeometry
import com.Puqbafu0.Sogsok.domain.model.ExpeditionMode
import com.Puqbafu0.Sogsok.domain.model.LevelTable
import com.Puqbafu0.Sogsok.domain.model.RoundOutcome
import com.Puqbafu0.Sogsok.domain.model.Slab
import com.Puqbafu0.Sogsok.domain.model.SlabContent
import com.Puqbafu0.Sogsok.domain.model.SlabState
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import com.Puqbafu0.Sogsok.domain.repository.SettingsRepository
import com.Puqbafu0.Sogsok.domain.usecase.DigSlabUseCase
import com.Puqbafu0.Sogsok.domain.usecase.GenerateBoardUseCase
import com.Puqbafu0.Sogsok.domain.usecase.ScanSlabUseCase
import com.Puqbafu0.Sogsok.domain.usecase.ScoreRoundUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateBoard: GenerateBoardUseCase,
    private val scanSlab: ScanSlabUseCase,
    private val digSlab: DigSlabUseCase,
    private val scoreRound: ScoreRoundUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var bannerJob: Job? = null
    private var endJob: Job? = null
    private val recoveredThisRound = mutableListOf<Int>()

    fun load(levelIndex: Int, attempt: Int) {
        val current = _state.value
        if (current.started && current.attempt == attempt && current.levelIndex == levelIndex) {
            return
        }
        bannerJob?.cancel()
        endJob?.cancel()
        recoveredThisRound.clear()

        val settings = settingsRepository.settings()
        val progress = progressRepository.progress()
        val config = LevelTable.configFor(levelIndex)
        val board = generateBoard(config, attempt, progress.recovered)
        val random = Random(board.seed)

        var slabs = board.slabs
        var scans = config.scans + progress.scanBonus
        if (settings.mode == ExpeditionMode.EXPLORER) {
            scans += 1
        } else {
            val soil = slabs.filter { it.content is SlabContent.Soil }
            if (soil.isNotEmpty()) {
                val pick = soil.random(random).index
                slabs = slabs.map { slab ->
                    if (slab.index == pick) slab.copy(state = SlabState.SCANNED) else slab
                }
            }
        }

        _state.value = GameUiState(
            levelIndex = levelIndex,
            attempt = attempt,
            started = true,
            slabs = slabs,
            pairs = board.pairs,
            buriedSets = board.buriedRelicIds.map { SampleData.relic(it).setId },
            cursor = firstHidden(slabs, GameConfig.CELLS / 2),
            mode = PlayMode.DIG,
            torches = config.torches,
            torchesTotal = config.torches,
            scans = scans,
            relicsFound = 0,
            relicsTotal = config.relics,
            pairsLinked = 0,
            score = 0,
            multiplierStep = 0,
            bestMultiplierStep = 0,
            banner = GameBanner.Hint,
            paused = false,
            finished = false,
            showLabels = settings.slabLabels,
            reducedMotion = settings.reducedMotion
        )
    }

    fun onSlabTapped(index: Int) {
        val current = _state.value
        if (!current.started || current.paused || current.finished) return
        val slab = current.slabs.getOrNull(index) ?: return
        if (slab.state != SlabState.HIDDEN) {
            showBanner(GameBanner.AlreadyOpen(BoardGeometry.label(index)))
            return
        }
        if (current.cursor == index) {
            commit(index)
        } else {
            _state.update { it.copy(cursor = index) }
            showBanner(GameBanner.Aimed(BoardGeometry.label(index)))
        }
    }

    fun onFlip() {
        val current = _state.value
        if (!current.started || current.paused || current.finished) return
        commit(current.cursor)
    }

    fun onModeSelected(mode: PlayMode) {
        val current = _state.value
        if (current.finished) return
        if (mode == PlayMode.SCAN && current.scans <= 0) {
            showBanner(GameBanner.NoScans)
            return
        }
        _state.update { it.copy(mode = mode) }
    }

    fun openPause() {
        if (_state.value.finished) return
        bannerJob?.cancel()
        _state.update { it.copy(paused = true) }
    }

    fun closePause() {
        _state.update { it.copy(paused = false) }
    }

    fun pause() {
        bannerJob?.cancel()
    }

    private fun commit(index: Int) {
        val current = _state.value
        val slab = current.slabs.getOrNull(index) ?: return
        if (slab.state != SlabState.HIDDEN) {
            _state.update { it.copy(cursor = firstHidden(it.slabs, index)) }
            showBanner(GameBanner.AlreadyOpen(BoardGeometry.label(index)))
            return
        }
        if (current.mode == PlayMode.SCAN) {
            resolveScan(current, index)
        } else {
            resolveDig(current, index)
        }
    }

    private fun resolveScan(current: GameUiState, index: Int) {
        if (current.scans <= 0) {
            _state.update { it.copy(mode = PlayMode.DIG) }
            showBanner(GameBanner.NoScans)
            return
        }
        val result = scanSlab(current.slabs, current.pairs, index)
        val linked = result.linkedPair
        var scans = current.scans - 1
        var score = current.score
        var pairsLinked = current.pairsLinked
        val banner: GameBanner
        if (linked != null) {
            scans += 1
            score += GameConfig.PAIR_SCORE
            pairsLinked += 1
            banner = GameBanner.PairLinked
        } else if (result.animal != null) {
            banner = GameBanner.Track(result.animal, BoardGeometry.label(index))
        } else {
            val clue = result.clue ?: 0
            banner = if (clue > 0) {
                GameBanner.Clue(clue, BoardGeometry.label(index))
            } else {
                GameBanner.Quiet(BoardGeometry.label(index))
            }
        }
        _state.update {
            it.copy(
                slabs = result.slabs,
                scans = scans,
                score = score,
                pairsLinked = pairsLinked,
                cursor = firstHidden(result.slabs, index),
                mode = if (scans <= 0) PlayMode.DIG else it.mode
            )
        }
        showBanner(banner)
    }

    private fun resolveDig(current: GameUiState, index: Int) {
        val result = digSlab(current.slabs, index, current.multiplierStep)
        val relicId = result.relicId
        if (relicId != null) {
            recoveredThisRound.add(relicId)
            val found = current.relicsFound + 1
            val step = result.multiplierStep
            _state.update {
                it.copy(
                    slabs = result.slabs,
                    score = it.score + result.gained,
                    relicsFound = found,
                    multiplierStep = step,
                    bestMultiplierStep = maxOf(it.bestMultiplierStep, step),
                    cursor = firstHidden(result.slabs, index)
                )
            }
            showBanner(GameBanner.RelicFound(SampleData.relic(relicId).nameRes))
            if (found >= current.relicsTotal) {
                finishRound(RoundOutcome.WON)
            }
            return
        }
        val torches = current.torches - 1
        _state.update {
            it.copy(
                slabs = result.slabs,
                torches = torches,
                multiplierStep = 0,
                cursor = firstHidden(result.slabs, index)
            )
        }
        showBanner(GameBanner.EmptySoil(BoardGeometry.label(index)))
        if (torches <= 0) {
            finishRound(RoundOutcome.LOST)
        }
    }

    private fun finishRound(outcome: RoundOutcome) {
        val current = _state.value
        if (current.finished) return
        bannerJob?.cancel()
        val summary = scoreRound(
            levelIndex = current.levelIndex,
            outcome = outcome,
            runningScore = current.score,
            torchesLeft = current.torches,
            relicsFound = current.relicsFound,
            relicsTotal = current.relicsTotal,
            pairsLinked = current.pairsLinked,
            bestMultiplierStep = current.bestMultiplierStep,
            recoveredIds = recoveredThisRound.toList()
        )
        progressRepository.applyRound(summary)
        _state.update { it.copy(finished = true, score = summary.score) }
        val level = current.levelIndex
        endJob = viewModelScope.launch {
            delay(GameConfig.ROUND_END_DELAY_MS)
            _events.send(GameEvent.RoundFinished(level))
        }
    }

    private fun showBanner(banner: GameBanner) {
        bannerJob?.cancel()
        _state.update { it.copy(banner = banner) }
        if (banner is GameBanner.Hint) return
        bannerJob = viewModelScope.launch {
            delay(GameConfig.BANNER_RESTORE_MS)
            _state.update { it.copy(banner = GameBanner.Hint) }
        }
    }

    private fun firstHidden(slabs: List<Slab>, from: Int): Int {
        if (slabs.isEmpty()) return 0
        for (step in 0 until slabs.size) {
            val candidate = (from + step) % slabs.size
            if (slabs[candidate].state == SlabState.HIDDEN) return candidate
        }
        return from.coerceIn(0, slabs.lastIndex)
    }

    override fun onCleared() {
        bannerJob?.cancel()
        endJob?.cancel()
        super.onCleared()
    }
}
