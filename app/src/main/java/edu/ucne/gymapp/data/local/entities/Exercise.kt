package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0,
    val name: String,
    val description: String,
    val instructions: String,
    val muscleGroupId: Int,
    val difficulty: String,
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
    val videoDuration: Int? = null,
    val isVideoAvailable: Boolean = false,
    val equipment: String? = null,
    val tips: String? = null,
    val commonMistakes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val popularity: Int = 0
)
