package edu.ucne.gymapp.presentation.routine

import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.entities.Routine


sealed class RoutineEvent {
    data class NameChange(val name: String) : RoutineEvent()
    data class DescriptionChange(val description: String) : RoutineEvent()
    data class EstimatedDurationChange(val estimatedDuration: Int) : RoutineEvent()
    data class DifficultyChange(val difficulty: String) : RoutineEvent()
    data class TargetMuscleGroupsChange(val targetMuscleGroups: String) : RoutineEvent()
    data class IsActiveChange(val isActive: Boolean) : RoutineEvent()

    data class LoadRoutinesByDifficulty(val difficulty: String) : RoutineEvent()
    data class LoadRoutinesByMuscleGroups(val muscleGroups: String) : RoutineEvent()
    data class SelectRoutine(val routine: Routine) : RoutineEvent()
    data class ToggleRoutineActive(val routineId: Int) : RoutineEvent()
    data class IncrementTimesCompleted(val routineId: Int) : RoutineEvent()
    data class ViewRoutine(val routine: Routine) : RoutineEvent()

    object CreateRoutine : RoutineEvent()
    object UpdateRoutine : RoutineEvent()
    object DeleteRoutine : RoutineEvent()
    object LoadAllRoutines : RoutineEvent()
    object LoadActiveRoutines : RoutineEvent()
    object ClearError : RoutineEvent()
    object ClearMessages : RoutineEvent()

    object LoadMuscleGroups : RoutineEvent()
    data class SelectMuscleGroup(val muscleGroup: MuscleGroup) : RoutineEvent()
    data class LoadExercisesByMuscleGroup(val muscleGroupId: Int) : RoutineEvent()
    data class SearchExercises(val query: String) : RoutineEvent()
    data class SelectExercise(val exercise: Exercise) : RoutineEvent()
    data class RemoveExercise(val exercise: Exercise) : RoutineEvent()

    data class ChangeStep(val step: RoutineCreationStep) : RoutineEvent()
    object NextStep : RoutineEvent()
    object PreviousStep : RoutineEvent()
    object ShowExerciseSelection : RoutineEvent()
    object HideExerciseSelection : RoutineEvent()
}