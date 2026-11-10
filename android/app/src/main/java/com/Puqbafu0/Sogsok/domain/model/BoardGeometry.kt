package com.Puqbafu0.Sogsok.domain.model

import com.Puqbafu0.Sogsok.core.config.GameConfig

object BoardGeometry {

    private val columns = listOf("A", "B", "C", "D", "E")

    fun row(index: Int): Int = index / GameConfig.GRID

    fun column(index: Int): Int = index % GameConfig.GRID

    fun label(index: Int): String = columns[column(index)] + (row(index) + 1).toString()

    fun neighbours(index: Int): List<Int> {
        val result = mutableListOf<Int>()
        val currentRow = row(index)
        val currentColumn = column(index)
        for (deltaRow in -1..1) {
            for (deltaColumn in -1..1) {
                if (deltaRow == 0 && deltaColumn == 0) continue
                val nextRow = currentRow + deltaRow
                val nextColumn = currentColumn + deltaColumn
                if (nextRow in 0 until GameConfig.GRID && nextColumn in 0 until GameConfig.GRID) {
                    result.add(nextRow * GameConfig.GRID + nextColumn)
                }
            }
        }
        return result
    }
}
