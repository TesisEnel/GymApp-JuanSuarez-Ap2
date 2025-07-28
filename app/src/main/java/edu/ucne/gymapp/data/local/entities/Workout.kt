package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Int = 0,
    val userId: Int,
    val routineId: Int?,
    val name: String,
    val startTime: Long,
    val endTime: Long? = null,
    val totalDuration: Int = 0,
    val status: String,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
