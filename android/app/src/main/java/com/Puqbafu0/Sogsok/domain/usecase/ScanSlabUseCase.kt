package com.Puqbafu0.Sogsok.domain.usecase

import com.Puqbafu0.Sogsok.domain.model.AnimalTrack
import com.Puqbafu0.Sogsok.domain.model.Slab
import com.Puqbafu0.Sogsok.domain.model.SlabContent
import com.Puqbafu0.Sogsok.domain.model.SlabState
import com.Puqbafu0.Sogsok.domain.model.TrackPair

data class ScanResult(
    val slabs: List<Slab>,
    val clue: Int?,
    val animal: AnimalTrack?,
    val linkedPair: TrackPair?
)

class ScanSlabUseCase {

    operator fun invoke(
        slabs: List<Slab>,
        pairs: List<TrackPair>,
        index: Int
    ): ScanResult {
        val target = slabs[index]
        var updated = slabs.map { slab ->
            if (slab.index == index) slab.copy(state = SlabState.SCANNED) else slab
        }
        val content = target.content
        if (content !is SlabContent.Paw) {
            return ScanResult(updated, target.clue, null, null)
        }
        val pair = pairs.firstOrNull { it.first == index || it.second == index }
            ?: return ScanResult(updated, null, content.animal, null)
        val partner = if (pair.first == index) pair.second else pair.first
        val partnerOpen = updated[partner].state != SlabState.HIDDEN
        if (!partnerOpen) {
            return ScanResult(updated, null, content.animal, null)
        }
        updated = updated.map { slab ->
            when (slab.index) {
                pair.first, pair.second -> slab.copy(
                    state = if (slab.state == SlabState.DUG) {
                        SlabState.DUG
                    } else {
                        SlabState.SCANNED
                    },
                    linked = true
                )
                pair.bridge -> if (slab.state == SlabState.HIDDEN) {
                    slab.copy(state = SlabState.SCANNED)
                } else {
                    slab
                }
                else -> slab
            }
        }
        return ScanResult(updated, null, content.animal, pair)
    }
}
