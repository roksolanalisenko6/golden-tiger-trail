package com.Puqbafu0.Sogsok.domain.usecase

import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.Slab
import com.Puqbafu0.Sogsok.domain.model.SlabContent
import com.Puqbafu0.Sogsok.domain.model.SlabState

data class DigResult(
    val slabs: List<Slab>,
    val relicId: Int?,
    val gained: Int,
    val multiplierStep: Int,
    val torchLost: Boolean
)

class DigSlabUseCase {

    operator fun invoke(
        slabs: List<Slab>,
        index: Int,
        multiplierStep: Int
    ): DigResult {
        val target = slabs[index]
        val updated = slabs.map { slab ->
            if (slab.index == index) slab.copy(state = SlabState.DUG) else slab
        }
        val content = target.content
        if (content !is SlabContent.Buried) {
            return DigResult(updated, null, 0, 0, true)
        }
        val gained = (GameConfig.RELIC_SCORE * multiplierOf(multiplierStep)).toInt()
        val nextStep = (multiplierStep + 1).coerceAtMost(GameConfig.MAX_MULTIPLIER_STEP)
        return DigResult(updated, content.relicId, gained, nextStep, false)
    }

    companion object {
        fun multiplierOf(step: Int): Float = when (step) {
            0 -> 1f
            1 -> 1.5f
            else -> 2f
        }
    }
}
