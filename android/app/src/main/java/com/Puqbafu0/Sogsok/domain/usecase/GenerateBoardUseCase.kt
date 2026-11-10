package com.Puqbafu0.Sogsok.domain.usecase

import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.AnimalTrack
import com.Puqbafu0.Sogsok.domain.model.BoardGeometry
import com.Puqbafu0.Sogsok.domain.model.BoardState
import com.Puqbafu0.Sogsok.domain.model.LevelConfig
import com.Puqbafu0.Sogsok.domain.model.Slab
import com.Puqbafu0.Sogsok.domain.model.SlabContent
import com.Puqbafu0.Sogsok.domain.model.SlabState
import com.Puqbafu0.Sogsok.domain.model.TrackPair
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

private data class Layout(
    val relicCells: List<Int>,
    val pairs: List<TrackPair>
)

class GenerateBoardUseCase {

    operator fun invoke(
        config: LevelConfig,
        attempt: Int,
        recovered: Set<Int>
    ): BoardState {
        val baseSeed = config.index.toLong() * 7919L xor (attempt.toLong() * 104729L)
        for (pass in 0 until GameConfig.GENERATION_ATTEMPTS) {
            val random = Random(baseSeed + pass)
            val layout = build(config, random)
            if (layout != null && solvable(layout)) {
                return assemble(baseSeed + pass, layout, config, recovered, random, true)
            }
        }
        val fallback = fallbackLayout(config)
        return assemble(baseSeed, fallback, config, recovered, Random(baseSeed), false)
    }

    private fun build(config: LevelConfig, random: Random): Layout? {
        val cells = (0 until GameConfig.CELLS).toMutableList()
        cells.shuffle(random)
        val relicCells = cells.take(config.relics)
        val reserved = relicCells.toMutableSet()
        val pairs = mutableListOf<TrackPair>()
        val animals = AnimalTrack.entries
        val wanted = config.trackPairs.coerceAtMost(animals.size)
        for (slot in 0 until wanted) {
            val found = findPair(reserved, random) ?: return null
            pairs.add(TrackPair(animals[slot], found[0], found[1], found[2]))
            reserved.add(found[0])
            reserved.add(found[1])
            reserved.add(found[2])
        }
        if (pairs.size < wanted) return null
        return Layout(relicCells, pairs)
    }

    private fun findPair(reserved: Set<Int>, random: Random): List<Int>? {
        val free = (0 until GameConfig.CELLS).filter { !reserved.contains(it) }
        if (free.size < 3) return null
        for (tries in 0 until 80) {
            val first = free.random(random)
            val second = free.random(random)
            if (first == second) continue
            val chebyshev = max(
                abs(BoardGeometry.row(first) - BoardGeometry.row(second)),
                abs(BoardGeometry.column(first) - BoardGeometry.column(second))
            )
            if (chebyshev < 2) continue
            val bridgeRow = (BoardGeometry.row(first) + BoardGeometry.row(second)) / 2
            val bridgeColumn = (BoardGeometry.column(first) + BoardGeometry.column(second)) / 2
            val bridge = bridgeRow * GameConfig.GRID + bridgeColumn
            if (bridge == first || bridge == second) continue
            if (reserved.contains(bridge)) continue
            return listOf(first, second, bridge)
        }
        return null
    }

    private fun solvable(layout: Layout): Boolean {
        val relics = layout.relicCells.toSet()
        val tracks = layout.pairs.flatMap { listOf(it.first, it.second) }.toSet()
        val blocked = relics + tracks
        for (cell in layout.relicCells) {
            val readable = BoardGeometry.neighbours(cell).any { !blocked.contains(it) }
            if (!readable) return false
        }
        for (rowStart in 0 until GameConfig.GRID - 2) {
            for (columnStart in 0 until GameConfig.GRID - 2) {
                var packed = true
                for (deltaRow in 0 until 3) {
                    for (deltaColumn in 0 until 3) {
                        val cell = (rowStart + deltaRow) * GameConfig.GRID + columnStart + deltaColumn
                        if (!relics.contains(cell)) packed = false
                    }
                }
                if (packed) return false
            }
        }
        return true
    }

    private fun assemble(
        seed: Long,
        layout: Layout,
        config: LevelConfig,
        recovered: Set<Int>,
        random: Random,
        generated: Boolean
    ): BoardState {
        val missing = (0 until GameConfig.TOTAL_RELICS)
            .filter { !recovered.contains(it) }
            .shuffled(random)
        val owned = (0 until GameConfig.TOTAL_RELICS)
            .filter { recovered.contains(it) }
            .shuffled(random)
        val relicIds = (missing + owned).take(config.relics)
        val relicByCell = HashMap<Int, Int>()
        layout.relicCells.forEachIndexed { position, cell ->
            relicByCell[cell] = relicIds.getOrElse(position) { position }
        }
        val pawByCell = HashMap<Int, AnimalTrack>()
        layout.pairs.forEach { pair ->
            pawByCell[pair.first] = pair.animal
            pawByCell[pair.second] = pair.animal
        }
        val relicCellSet = layout.relicCells.toSet()
        val slabs = (0 until GameConfig.CELLS).map { index ->
            val content = when {
                relicByCell.containsKey(index) -> SlabContent.Buried(relicByCell.getValue(index))
                pawByCell.containsKey(index) -> SlabContent.Paw(pawByCell.getValue(index))
                else -> SlabContent.Soil
            }
            Slab(
                index = index,
                content = content,
                state = SlabState.HIDDEN,
                clue = BoardGeometry.neighbours(index).count { relicCellSet.contains(it) },
                linked = false
            )
        }
        return BoardState(
            seed = seed,
            slabs = slabs,
            pairs = layout.pairs,
            buriedRelicIds = relicIds,
            generated = generated
        )
    }

    private fun fallbackLayout(config: LevelConfig): Layout {
        val relicCells = if (config.relics >= 4) listOf(1, 8, 16, 23) else listOf(2, 11, 22)
        val reserved = relicCells.toMutableSet()
        val candidates = listOf(
            listOf(5, 9, 7),
            listOf(0, 4, 2),
            listOf(15, 19, 17),
            listOf(10, 14, 12)
        )
        val animals = AnimalTrack.entries
        val pairs = mutableListOf<TrackPair>()
        for (candidate in candidates) {
            if (pairs.size >= config.trackPairs) break
            if (pairs.size >= animals.size) break
            if (candidate.any { reserved.contains(it) }) continue
            pairs.add(
                TrackPair(
                    animal = animals[pairs.size],
                    first = candidate[0],
                    second = candidate[1],
                    bridge = candidate[2]
                )
            )
            reserved.addAll(candidate)
        }
        return Layout(relicCells, pairs)
    }
}
