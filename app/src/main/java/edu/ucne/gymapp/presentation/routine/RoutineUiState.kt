package edu.ucne.gymapp.presentation.routine

import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.RoutineExercise

data class RoutineUiState(
    val routineId: Int = 0,
    val name: String = "",
    val description: String = "",
    val estimatedDuration: Int = 30,
    val difficulty: String = "Principiante",
    val targetMuscleGroups: String = "",
    val isActive: Boolean = false,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val timesCompletedUpdated: Boolean = false,

    val routines: List<Routine> = emptyList(),
    val activeRoutines: List<Routine> = emptyList(),
    val popularRoutines: List<Routine> = emptyList(),
    val recentRoutines: List<Routine> = emptyList(),
    val filteredRoutines: List<Routine> = emptyList(),

    val selectedRoutine: Routine? = null,
    val selectedDifficulty: String = "",
    val showRoutineDetails: Boolean = false,
    val routineToView: Routine? = null,

    val muscleGroups: List<MuscleGroup> = emptyList(),
    val selectedMuscleGroups: List<MuscleGroup> = emptyList(),
    val exercises: List<Exercise> = emptyList(),
    val selectedExercises: List<Exercise> = emptyList(),
    val routineExercises: List<RoutineExercise> = emptyList(),

    val showExerciseSelection: Boolean = false,
    val currentStep: RoutineCreationStep = RoutineCreationStep.BASIC_INFO,
    val exerciseSearchQuery: String = "",
    val filteredExercises: List<Exercise> = emptyList()
)

enum class RoutineCreationStep {
    BASIC_INFO,
    MUSCLE_GROUP,
    EXERCISE,
    EXERCISE_CONFIG,
    REVIEW
}