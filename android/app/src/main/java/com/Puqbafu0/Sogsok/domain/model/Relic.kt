package com.Puqbafu0.Sogsok.domain.model

data class Relic(
    val id: Int,
    val setId: Int,
    val nameRes: Int
)

data class RelicSet(
    val id: Int,
    val nameRes: Int,
    val relics: List<Relic>
)
