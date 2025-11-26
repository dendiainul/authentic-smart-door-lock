package com.authentic.smartdoor.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    // TODO: Inject repository when available
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()

    fun handleEvent(event: NotificationSettingsEvent) {
        when (event) {
            is NotificationSettingsEvent.LoadSettings -> {
                loadSettings()
            }
            is NotificationSettingsEvent.ToggleAccessSuccess -> {
                toggleAccessSuccess()
            }
            is NotificationSettingsEvent.ToggleAccessFailed -> {
                toggleAccessFailed()
            }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Load settings from repository
                // For now, use default values
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    accessSuccessEnabled = true,
                    accessFailedEnabled = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun toggleAccessSuccess() {
        _uiState.value = _uiState.value.copy(
            accessSuccessEnabled = !_uiState.value.accessSuccessEnabled
        )
        // TODO: Save to repository
    }

    private fun toggleAccessFailed() {
        _uiState.value = _uiState.value.copy(
            accessFailedEnabled = !_uiState.value.accessFailedEnabled
        )
        // TODO: Save to repository
    }
}

sealed class NotificationSettingsEvent {
    object LoadSettings : NotificationSettingsEvent()
    object ToggleAccessSuccess : NotificationSettingsEvent()
    object ToggleAccessFailed : NotificationSettingsEvent()
}

data class NotificationSettingsUiState(
    val isLoading: Boolean = false,
    val accessSuccessEnabled: Boolean = true,
    val accessFailedEnabled: Boolean = true,
    val errorMessage: String? = null
)
