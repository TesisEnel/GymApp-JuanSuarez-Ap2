package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_exercises")
data class RoutineExercise(
    @PrimaryKey(autoGenerate = true)
    val routineExerciseId: Int = 0,
    val routineId: Int,
    val exerciseId: Int,
    val order: Int,
    val sets: Int,
    val reps: String,
    val weight: Float? = null,
    val restTime: Int = 90,
    val notes: String? = null
)
