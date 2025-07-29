package edu.ucne.gymapp.presentation.userpreferences

import edu.ucne.gymapp.data.local.entities.UserPreferences

data class UserPreferencesUiState(
    val userId: Int = 0,
    val defaultRestTime: Int = 90,
    val weightUnit: String = "kg",
    val autoVideoPlay: Boolean = true,
    val videoQuality: String = "HD",
    val notificationsEnabled: Boolean = true,
    val darkMode: Boolean = false,
    val keepScreenOn: Boolean = true,
    val userPreferences: UserPreferences? = null,
    val availableWeightUnits: List<String> = listOf("kg", "lbs"),
    val availableVideoQualities: List<String> = listOf("HD", "SD", "4K"),
    val restTimeOptions: List<Int> = listOf(30, 60, 90, 120, 150, 180),
    val hasUnsavedChanges: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val preferencesReset: Boolean = false
)