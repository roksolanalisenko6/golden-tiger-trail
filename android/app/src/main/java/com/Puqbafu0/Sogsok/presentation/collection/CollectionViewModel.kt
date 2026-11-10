package com.Puqbafu0.Sogsok.presentation.collection

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.progress()
        _state.update {
            it.copy(
                recovered = progress.recovered,
                completedSets = progress.completedSets
            )
        }
    }
}
