package com.Puqbafu0.Sogsok.domain.model

data class BoardState(
    val seed: Long,
    val slabs: List<Slab>,
    val pairs: List<TrackPair>,
    val buriedRelicIds: List<Int>,
    val generated: Boolean
)
