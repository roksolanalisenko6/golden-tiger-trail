package com.Puqbafu0.Sogsok.domain.repository

import com.Puqbafu0.Sogsok.domain.model.PlayerProgress
import com.Puqbafu0.Sogsok.domain.model.RoundSummary

interface ProgressRepository {
    fun progress(): PlayerProgress
    fun applyRound(summary: RoundSummary): PlayerProgress
    fun lastRound(): RoundSummary?
    fun clear()
}
