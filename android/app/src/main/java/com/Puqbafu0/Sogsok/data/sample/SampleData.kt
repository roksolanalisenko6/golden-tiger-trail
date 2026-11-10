package com.Puqbafu0.Sogsok.data.sample

import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.Relic
import com.Puqbafu0.Sogsok.domain.model.RelicSet

object SampleData {

    private val setNames = listOf(
        R.string.set_1,
        R.string.set_2,
        R.string.set_3,
        R.string.set_4,
        R.string.set_5
    )

    private val relicNames = listOf(
        R.string.relic_1,
        R.string.relic_2,
        R.string.relic_3,
        R.string.relic_4,
        R.string.relic_5,
        R.string.relic_6,
        R.string.relic_7,
        R.string.relic_8,
        R.string.relic_9,
        R.string.relic_10,
        R.string.relic_11,
        R.string.relic_12,
        R.string.relic_13,
        R.string.relic_14,
        R.string.relic_15
    )

    val relicSets: List<RelicSet> = setNames.mapIndexed { setId, nameRes ->
        RelicSet(
            id = setId,
            nameRes = nameRes,
            relics = (0 until GameConfig.RELICS_PER_SET).map { slot ->
                val id = setId * GameConfig.RELICS_PER_SET + slot
                Relic(id = id, setId = setId, nameRes = relicNames[id])
            }
        )
    }

    val relics: List<Relic> = relicSets.flatMap { it.relics }

    fun relic(id: Int): Relic = relics[id.coerceIn(0, relics.lastIndex)]

    fun relicSet(id: Int): RelicSet = relicSets[id.coerceIn(0, relicSets.lastIndex)]
}
