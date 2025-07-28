package edu.ucne.gymapp.presentation.routine

import edu.ucne.gymapp.data.local.entities.Routine

data class RoutineUiState(
    val name: String = "",
    val description: String = "",
    val estimatedDuration: Int = 30,
    val difficulty: String = "Principiante",
    val targetMuscleGroups: String = "",
    val isActive: Boolean = false,
    val routines: List<Routine> = emptyList(),
    val activeRoutines: List<Routine> = emptyList(),
    val selectedRoutine: Routine? = null,
    val filteredRoutines: List<Routine> = emptyList(),
    val recentRoutines: List<Routine> = emptyList(),
    val popularRoutines: List<Routine> = emptyList(),
    val selectedDifficulty: String = "",
    val selectedMuscleGroups: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val timesCompletedUpdated: Boolean = false
)