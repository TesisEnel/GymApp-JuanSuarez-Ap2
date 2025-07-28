package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_exercises")
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true)
    val workoutExerciseId: Int = 0,
    val workoutId: Int,
    val exerciseId: Int,
    val order: Int,
    val plannedSets: Int,
    val completedSets: Int = 0,
    val status: String,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val notes: String? = null
)
