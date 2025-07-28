package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey(autoGenerate = true)
    val userPreferenceId: Int = 0,
    val userId: Int,
    val defaultRestTime: Int = 90,
    val weightUnit: String,
    val autoVideoPlay: Boolean = true,
    val videoQuality: String,
    val notificationsEnabled: Boolean = true,
    val darkMode: Boolean = false,
    val keepScreenOn: Boolean = true
)
