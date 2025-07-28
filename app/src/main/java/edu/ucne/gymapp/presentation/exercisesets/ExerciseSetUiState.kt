package edu.ucne.gymapp.presentation.exercisesets

import edu.ucne.gymapp.data.local.entities.ExerciseSet

data class ExerciseSetUiState(
    val workoutExerciseId: Int = 0,
    val setNumber: Int = 1,
    val reps: Int = 0,
    val weight: Float? = null,
    val restTime: Int? = 90,
    val difficulty: Int = 5,
    val isCompleted: Boolean = false,
    val sets: List<ExerciseSet> = emptyList(),
    val completedSets: List<ExerciseSet> = emptyList(),
    val pendingSets: List<ExerciseSet> = emptyList(),
    val selectedSet: ExerciseSet? = null,
    val multipleSets: List<ExerciseSet> = emptyList(),
    val currentSetIndex: Int = 0,
    val totalSets: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val isMarkedCompleted: Boolean = false,
    val progressUpdated: Boolean = false
)