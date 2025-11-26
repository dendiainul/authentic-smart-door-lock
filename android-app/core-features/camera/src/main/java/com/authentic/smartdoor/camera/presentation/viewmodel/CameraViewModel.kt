package com.authentic.smartdoor.camera.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.authentic.smartdoor.storage.remote.datasource.CameraRemoteDataSource
import com.authentic.smartdoor.storage.preferences.PreferencesManager

data class CameraUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val doorId: Int? = null,
    val streamUrl: String? = null,
    val sessionExpired: Boolean = false
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val remote: CameraRemoteDataSource,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState(isLoading = false))
    val uiState: StateFlow<CameraUiState> = _uiState

    fun loadStream(doorId: String) {
        val idInt = doorId.toIntOrNull()
        if (idInt == null) {
            _uiState.value = CameraUiState(
                isLoading = false,
                doorId = null,
                streamUrl = null,
                errorMessage = "ID pintu tidak valid"
            )
            return
        }
        loadStream(idInt)
    }

    fun loadStream(doorId: Int) {
        _uiState.value = CameraUiState(isLoading = true, doorId = doorId)
        viewModelScope.launch {
            try {
                val res = remote.getCameraStream(doorId)
                if (res.success && res.data != null) {
                    val data = res.data!!
                    _uiState.value = CameraUiState(
                        isLoading = false,
                        doorId = doorId,
                        streamUrl = data.stream_url,
                        errorMessage = null,
                        sessionExpired = false
                    )
                } else {
                    val expired = !preferences.isLoggedIn()
                    _uiState.value = CameraUiState(
                        isLoading = false,
                        doorId = doorId,
                        streamUrl = null,
                        errorMessage = if (expired) "Sesi telah habis" else res.message ?: "Gagal memuat stream kamera",
                        sessionExpired = expired
                    )
                }
            } catch (e: Exception) {
                val expired = !preferences.isLoggedIn()
                _uiState.value = CameraUiState(
                    isLoading = false,
                    doorId = doorId,
                    streamUrl = null,
                    errorMessage = if (expired) "Sesi telah habis" else e.message ?: "Terjadi kesalahan tak terduga",
                    sessionExpired = expired
                )
            }
        }
    }
}