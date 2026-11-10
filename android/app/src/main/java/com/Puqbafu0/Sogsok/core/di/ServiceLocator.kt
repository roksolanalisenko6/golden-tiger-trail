package com.Puqbafu0.Sogsok.core.di

import android.content.Context
import com.Puqbafu0.Sogsok.data.local.PreferencesStore
import com.Puqbafu0.Sogsok.data.repository.ProgressRepositoryImpl
import com.Puqbafu0.Sogsok.data.repository.SettingsRepositoryImpl
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import com.Puqbafu0.Sogsok.domain.repository.SettingsRepository
import com.Puqbafu0.Sogsok.domain.usecase.DigSlabUseCase
import com.Puqbafu0.Sogsok.domain.usecase.GenerateBoardUseCase
import com.Puqbafu0.Sogsok.domain.usecase.ScanSlabUseCase
import com.Puqbafu0.Sogsok.domain.usecase.ScoreRoundUseCase

object ServiceLocator {

    private var progressInternal: ProgressRepository? = null
    private var settingsInternal: SettingsRepository? = null

    val generateBoard: GenerateBoardUseCase by lazy { GenerateBoardUseCase() }
    val scanSlab: ScanSlabUseCase by lazy { ScanSlabUseCase() }
    val digSlab: DigSlabUseCase by lazy { DigSlabUseCase() }
    val scoreRound: ScoreRoundUseCase by lazy { ScoreRoundUseCase() }

    val progressRepository: ProgressRepository
        get() = requireNotNull(progressInternal) { MISSING_INIT }

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settingsInternal) { MISSING_INIT }

    fun init(context: Context) {
        if (progressInternal != null && settingsInternal != null) return
        val store = PreferencesStore(context)
        progressInternal = ProgressRepositoryImpl(store)
        settingsInternal = SettingsRepositoryImpl(store)
    }

    private const val MISSING_INIT = "ServiceLocator.init was not called"
}
