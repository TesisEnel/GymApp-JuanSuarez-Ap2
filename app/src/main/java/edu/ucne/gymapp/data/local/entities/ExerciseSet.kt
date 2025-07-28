package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_sets")
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true)
    val exerciseSetId: Int = 0,
    val workoutExerciseId: Int,
    val setNumber: Int,
    val reps: Int,
    val weight: Float? = null,
    val restTime: Int? = null,
    val isCompleted: Boolean = false,
    val difficulty: Int = 5,
    val timestamp: Long = System.currentTimeMillis()
)
