package com.Puqbafu0.Sogsok.domain.model

sealed interface SlabContent {
    data object Soil : SlabContent
    data class Buried(val relicId: Int) : SlabContent
    data class Paw(val animal: AnimalTrack) : SlabContent
}

enum class SlabState {
    HIDDEN,
    SCANNED,
    DUG
}

data class Slab(
    val index: Int,
    val content: SlabContent,
    val state: SlabState,
    val clue: Int,
    val linked: Boolean
)

data class TrackPair(
    val animal: AnimalTrack,
    val first: Int,
    val second: Int,
    val bridge: Int
)
