package edu.ucne.gymapp.presentation.routinexercise

import edu.ucne.gymapp.data.local.entities.RoutineExercise

data class RoutineExerciseUiState(
    val routineId: Int = 0,
    val exerciseId: Int = 0,
    val order: Int = 1,
    val sets: Int = 3,
    val reps: String = "10",
    val weight: Float? = null,
    val restTime: Int = 90,
    val notes: String? = null,
    val routineExercises: List<RoutineExercise> = emptyList(),
    val selectedRoutineExercise: RoutineExercise? = null,
    val exercisesInRoutine: List<RoutineExercise> = emptyList(),
    val routinesWithExercise: List<RoutineExercise> = emptyList(),
    val isReordering: Boolean = false,
    val draggedItem: RoutineExercise? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val exerciseAdded: Boolean = false,
    val exerciseRemoved: Boolean = false,
    val exercisesReordered: Boolean = false
)
