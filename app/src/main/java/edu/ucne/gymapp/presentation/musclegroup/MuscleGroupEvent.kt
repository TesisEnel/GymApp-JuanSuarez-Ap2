package edu.ucne.gymapp.presentation.musclegroup


sealed interface MuscleGroupEvent {
    data class SelectMuscleGroup(val muscleGroup: MuscleGroup) : MuscleGroupEvent
    data object LoadAllMuscleGroups : MuscleGroupEvent
    data object InitializePredefinedGroups : MuscleGroupEvent
    data class LoadExercisesByMuscleGroup(val muscleGroupId: Int) : MuscleGroupEvent
    data object ClearError : MuscleGroupEvent
    data object ClearMessages : MuscleGroupEvent
}