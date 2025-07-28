package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val routineId: Int = 0,
    val userId: Int = 0,
    val name: String = "",
    val description: String = "",
    val estimatedDuration: Int = 0,
    val difficulty: String = "Principiante",
    val targetMuscleGroups: String = "",
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val timesCompleted: Int = 0
)
