package edu.ucne.gymapp.presentation.musclegroup

import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.relation.MuscleGroupWithExercises

data class MuscleGroupUiState(
    val name: String = "",
    val description: String = "",
    val iconResource: String? = null,
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val orderedMuscleGroups: List<MuscleGroup> = emptyList(),
    val selectedMuscleGroup: MuscleGroup? = null,
    val selectedMuscleGroups: List<MuscleGroup> = emptyList(),
    val muscleGroupWithExercises: MuscleGroupWithExercises? = null,
    val muscleGroupsWithExercises: List<MuscleGroupWithExercises> = emptyList(),
    val multipleMuscleGroups: List<MuscleGroup> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false
)