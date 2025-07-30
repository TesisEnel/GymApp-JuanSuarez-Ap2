package edu.ucne.gymapp.presentation.musclegroup

import edu.ucne.gymapp.data.local.entities.MuscleGroup

sealed interface MuscleGroupEvent {
    data class SelectMuscleGroup(val muscleGroup: MuscleGroup) : MuscleGroupEvent
    data object LoadAllMuscleGroups : MuscleGroupEvent
    data object InitializePredefinedGroups : MuscleGroupEvent
    data class LoadExercisesByMuscleGroup(val muscleGroupId: Int) : MuscleGroupEvent
    data object ClearError : MuscleGroupEvent
    data object ClearMessages : MuscleGroupEvent
}