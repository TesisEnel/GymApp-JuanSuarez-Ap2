package edu.ucne.gymapp.presentation.userpreferences

sealed interface UserPreferencesEvent {
    data class UserChange(val userId: Int) : UserPreferencesEvent
    data class DefaultRestTimeChange(val defaultRestTime: Int) : UserPreferencesEvent
    data class WeightUnitChange(val weightUnit: String) : UserPreferencesEvent
    data class AutoVideoPlayChange(val autoVideoPlay: Boolean) : UserPreferencesEvent
    data class VideoQualityChange(val videoQuality: String) : UserPreferencesEvent
    data class NotificationsEnabledChange(val notificationsEnabled: Boolean) : UserPreferencesEvent
    data class DarkModeChange(val darkMode: Boolean) : UserPreferencesEvent
    data class KeepScreenOnChange(val keepScreenOn: Boolean) : UserPreferencesEvent
    data class LoadUserPreferences(val userId: Int) : UserPreferencesEvent
    data object ResetToDefaults : UserPreferencesEvent
    data object CreateUserPreferences : UserPreferencesEvent
    data object UpdateUserPreferences : UserPreferencesEvent
    data object SaveAllPreferences : UserPreferencesEvent
    data object ClearError : UserPreferencesEvent
    data object ClearMessages : UserPreferencesEvent
    data object Logout : UserPreferencesEvent
}