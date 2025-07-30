package edu.ucne.gymapp.presentation.musclegroup

import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.relation.MuscleGroupWithExercises

data class MuscleGroupUiState(
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val selectedMuscleGroup: MuscleGroup? = null,
    val selectedMuscleGroups: List<MuscleGroup> = emptyList(),
    val muscleGroupWithExercises: MuscleGroupWithExercises? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)