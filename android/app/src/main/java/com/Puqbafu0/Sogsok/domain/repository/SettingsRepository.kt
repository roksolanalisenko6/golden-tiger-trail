package com.Puqbafu0.Sogsok.domain.repository

import com.Puqbafu0.Sogsok.domain.model.GameSettings

interface SettingsRepository {
    fun settings(): GameSettings
    fun store(settings: GameSettings)
}
