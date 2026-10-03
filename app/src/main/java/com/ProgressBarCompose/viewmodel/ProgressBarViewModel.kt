package com.ProgressBarCompose.viewmodel

import androidx.lifecycle.ViewModel
import com.ProgressBarCompose.data.ProgressBarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressBarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressBarUiState())

    val uiState: StateFlow<ProgressBarUiState> = _uiState.asStateFlow()
}