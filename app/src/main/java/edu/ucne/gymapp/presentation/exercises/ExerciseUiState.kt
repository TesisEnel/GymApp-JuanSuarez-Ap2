package edu.ucne.gymapp.presentation.exercises

import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.relation.ExerciseWithMuscleGroup

data class ExerciseUiState(
    val name: String = "",
    val description: String = "",
    val instructions: String = "",
    val muscleGroupId: Int = 0,
    val difficulty: String = "Principiante",
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
    val videoDuration: Int? = null,
    val isVideoAvailable: Boolean = false,
    val equipment: String? = null,
    val tips: String? = null,
    val commonMistakes: String? = null,
    val exercises: List<Exercise> = emptyList(),
    val selectedExercise: Exercise? = null,
    val canEditOrDeleteSelected: Boolean = false,
    val exerciseWithMuscleGroup: ExerciseWithMuscleGroup? = null,
    val filteredExercises: List<Exercise> = emptyList(),
    val selectedDifficulties: List<String> = emptyList(),
    val selectedMuscleGroups: List<Int> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false
)