package edu.ucne.gymapp.presentation.musclegroup

import edu.ucne.gymapp.data.local.entities.MuscleGroup

sealed interface MuscleGroupEvent {
    data class NameChange(val name: String) : MuscleGroupEvent
    data class DescriptionChange(val description: String) : MuscleGroupEvent
    data class IconResourceChange(val iconResource: String?) : MuscleGroupEvent
    data class LoadMuscleGroupById(val id: Int) : MuscleGroupEvent
    data class LoadMuscleGroupsByIds(val ids: List<Int>) : MuscleGroupEvent
    data class LoadMuscleGroupWithExercises(val id: Int) : MuscleGroupEvent
    data class SelectMuscleGroup(val muscleGroup: MuscleGroup) : MuscleGroupEvent
    data class SelectMuscleGroups(val muscleGroups: List<MuscleGroup>) : MuscleGroupEvent
    data object CreateMuscleGroup : MuscleGroupEvent
    data object CreateMultipleMuscleGroups : MuscleGroupEvent
    data object UpdateMuscleGroup : MuscleGroupEvent
    data object DeleteMuscleGroup : MuscleGroupEvent
    data object LoadAllMuscleGroups : MuscleGroupEvent
    data object LoadMuscleGroupsOrdered : MuscleGroupEvent
    data object LoadAllMuscleGroupsWithExercises : MuscleGroupEvent
    data object ClearError : MuscleGroupEvent
    data object ClearMessages : MuscleGroupEvent
}