package edu.ucne.gymapp.presentation.userpreferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.UserPreferences
import edu.ucne.gymapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserPreferencesUiState())
    val state = _state.asStateFlow()

    private fun loadUserPreferences(userId: Int) {
        viewModelScope.launch {
            userPreferencesRepository.getPreferencesByUser(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        val preferences = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userPreferences = preferences,
                                userId = userId,
                                defaultRestTime = preferences?.defaultRestTime ?: 90,
                                weightUnit = preferences?.weightUnit ?: "kg",
                                autoVideoPlay = preferences?.autoVideoPlay ?: true,
                                videoQuality = preferences?.videoQuality ?: "HD",
                                notificationsEnabled = preferences?.notificationsEnabled ?: true,
                                darkMode = preferences?.darkMode ?: false,
                                keepScreenOn = preferences?.keepScreenOn ?: true,
                                hasUnsavedChanges = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar las preferencias del usuario"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createUserPreferences() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.userId == 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "ID de usuario no válido"
                    )
                }
                return@launch
            }

            val preferences = UserPreferences(
                userId = currentState.userId,
                defaultRestTime = currentState.defaultRestTime,
                weightUnit = currentState.weightUnit,
                autoVideoPlay = currentState.autoVideoPlay,
                videoQuality = currentState.videoQuality,
                notificationsEnabled = currentState.notificationsEnabled,
                darkMode = currentState.darkMode,
                keepScreenOn = currentState.keepScreenOn
            )

            userPreferencesRepository.insertPreferences(preferences).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isCreated = true,
                                hasUnsavedChanges = false,
                                successMessage = "Preferencias creadas exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear las preferencias",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateUserPreferences() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.userPreferences == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay preferencias para actualizar"
                    )
                }
                return@launch
            }

            val updatedPreferences = currentState.userPreferences.copy(
                defaultRestTime = currentState.defaultRestTime,
                weightUnit = currentState.weightUnit,
                autoVideoPlay = currentState.autoVideoPlay,
                videoQuality = currentState.videoQuality,
                notificationsEnabled = currentState.notificationsEnabled,
                darkMode = currentState.darkMode,
                keepScreenOn = currentState.keepScreenOn
            )

            userPreferencesRepository.updatePreferences(updatedPreferences).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isUpdated = true,
                                hasUnsavedChanges = false,
                                userPreferences = updatedPreferences,
                                successMessage = "Preferencias actualizadas exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar las preferencias",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveAllPreferences() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.userId == 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "ID de usuario no válido"
                    )
                }
                return@launch
            }

            val preferences = UserPreferences(
                userId = currentState.userId,
                defaultRestTime = currentState.defaultRestTime,
                weightUnit = currentState.weightUnit,
                autoVideoPlay = currentState.autoVideoPlay,
                videoQuality = currentState.videoQuality,
                notificationsEnabled = currentState.notificationsEnabled,
                darkMode = currentState.darkMode,
                keepScreenOn = currentState.keepScreenOn
            )

            userPreferencesRepository.insertOrUpdatePreferences(preferences).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                hasUnsavedChanges = false,
                                userPreferences = preferences,
                                successMessage = "Preferencias guardadas exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al guardar las preferencias",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun resetToDefaults() {
        _state.update {
            it.copy(
                defaultRestTime = 90,
                weightUnit = "kg",
                autoVideoPlay = true,
                videoQuality = "HD",
                notificationsEnabled = true,
                darkMode = false,
                keepScreenOn = true,
                hasUnsavedChanges = true,
                preferencesReset = true,
                successMessage = "Preferencias restablecidas a valores por defecto",
                errorMessage = null
            )
        }
    }

    private fun isValidRestTime(restTime: Int): Boolean {
        return restTime > 0 && restTime <= 300
    }

    private fun isValidWeightUnit(weightUnit: String): Boolean {
        return _state.value.availableWeightUnits.contains(weightUnit)
    }

    private fun isValidVideoQuality(videoQuality: String): Boolean {
        return _state.value.availableVideoQualities.contains(videoQuality)
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                _state.update {
                    UserPreferencesUiState(
                        availableWeightUnits = it.availableWeightUnits,
                        availableVideoQualities = it.availableVideoQualities,
                        restTimeOptions = it.restTimeOptions,

                        successMessage = "Sesión cerrada exitosamente"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMessage = "Error al cerrar sesión: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: UserPreferencesEvent) {
        when (event) {
            is UserPreferencesEvent.UserChange -> {
                _state.update {
                    it.copy(
                        userId = event.userId,
                        hasUnsavedChanges = false
                    )
                }
            }
            is UserPreferencesEvent.DefaultRestTimeChange -> {
                if (isValidRestTime(event.defaultRestTime)) {
                    _state.update {
                        it.copy(
                            defaultRestTime = event.defaultRestTime,
                            hasUnsavedChanges = true,
                            errorMessage = null
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorMessage = "El tiempo de descanso debe estar entre 1 y 300 segundos"
                        )
                    }
                }
            }
            is UserPreferencesEvent.WeightUnitChange -> {
                if (isValidWeightUnit(event.weightUnit)) {
                    _state.update {
                        it.copy(
                            weightUnit = event.weightUnit,
                            hasUnsavedChanges = true,
                            errorMessage = null
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorMessage = "Unidad de peso no válida"
                        )
                    }
                }
            }
            is UserPreferencesEvent.AutoVideoPlayChange -> {
                _state.update {
                    it.copy(
                        autoVideoPlay = event.autoVideoPlay,
                        hasUnsavedChanges = true
                    )
                }
            }
            is UserPreferencesEvent.VideoQualityChange -> {
                if (isValidVideoQuality(event.videoQuality)) {
                    _state.update {
                        it.copy(
                            videoQuality = event.videoQuality,
                            hasUnsavedChanges = true,
                            errorMessage = null
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorMessage = "Calidad de video no válida"
                        )
                    }
                }
            }
            is UserPreferencesEvent.NotificationsEnabledChange -> {
                _state.update {
                    it.copy(
                        notificationsEnabled = event.notificationsEnabled,
                        hasUnsavedChanges = true
                    )
                }
            }
            is UserPreferencesEvent.DarkModeChange -> {
                _state.update {
                    it.copy(
                        darkMode = event.darkMode,
                        hasUnsavedChanges = true
                    )
                }
            }
            is UserPreferencesEvent.KeepScreenOnChange -> {
                _state.update {
                    it.copy(
                        keepScreenOn = event.keepScreenOn,
                        hasUnsavedChanges = true
                    )
                }
            }
            is UserPreferencesEvent.LoadUserPreferences -> {
                loadUserPreferences(event.userId)
            }
            UserPreferencesEvent.ResetToDefaults -> {
                resetToDefaults()
            }
            UserPreferencesEvent.CreateUserPreferences -> {
                createUserPreferences()
            }
            UserPreferencesEvent.UpdateUserPreferences -> {
                updateUserPreferences()
            }
            UserPreferencesEvent.SaveAllPreferences -> {
                saveAllPreferences()
            }
            UserPreferencesEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            UserPreferencesEvent.Logout -> {
                logout()
            }
            UserPreferencesEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null,
                        isCreated = false,
                        isUpdated = false,
                        preferencesReset = false
                    )
                }
            }
        }
    }
}
