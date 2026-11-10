package com.Puqbafu0.Sogsok.data.repository

import com.Puqbafu0.Sogsok.data.local.PreferencesStore
import com.Puqbafu0.Sogsok.domain.model.ExpeditionMode
import com.Puqbafu0.Sogsok.domain.model.GameSettings
import com.Puqbafu0.Sogsok.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val preferences: PreferencesStore) : SettingsRepository {

    override fun settings(): GameSettings {
        val explorer = preferences.readBoolean(KEY_EXPLORER, false)
        return GameSettings(
            mode = if (explorer) ExpeditionMode.EXPLORER else ExpeditionMode.GUIDED,
            slabLabels = preferences.readBoolean(KEY_LABELS, false),
            reducedMotion = preferences.readBoolean(KEY_MOTION, false)
        )
    }

    override fun store(settings: GameSettings) {
        preferences.writeBoolean(KEY_EXPLORER, settings.mode == ExpeditionMode.EXPLORER)
        preferences.writeBoolean(KEY_LABELS, settings.slabLabels)
        preferences.writeBoolean(KEY_MOTION, settings.reducedMotion)
    }

    private companion object {
        const val KEY_EXPLORER = "opt_mode_explorer"
        const val KEY_LABELS = "opt_slab_labels"
        const val KEY_MOTION = "opt_reduced_motion"
    }
}
