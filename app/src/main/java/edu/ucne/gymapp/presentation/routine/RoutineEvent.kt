package edu.ucne.gymapp.presentation.routine

import edu.ucne.gymapp.data.local.entities.Routine

sealed interface RoutineEvent {
    data class NameChange(val name: String) : RoutineEvent
    data class DescriptionChange(val description: String) : RoutineEvent
    data class EstimatedDurationChange(val estimatedDuration: Int) : RoutineEvent
    data class DifficultyChange(val difficulty: String) : RoutineEvent
    data class TargetMuscleGroupsChange(val targetMuscleGroups: String) : RoutineEvent
    data class IsActiveChange(val isActive: Boolean) : RoutineEvent
    data class LoadRoutineById(val id: Int) : RoutineEvent
    data class LoadRoutinesByDifficulty(val difficulty: String) : RoutineEvent
    data class LoadRoutinesByMuscleGroups(val muscleGroups: String) : RoutineEvent
    data class SelectRoutine(val routine: Routine) : RoutineEvent
    data class ToggleRoutineActive(val routineId: Int) : RoutineEvent
    data class IncrementTimesCompleted(val routineId: Int) : RoutineEvent
    data object CreateRoutine : RoutineEvent
    data object UpdateRoutine : RoutineEvent
    data object DeleteRoutine : RoutineEvent
    data object LoadAllRoutines : RoutineEvent
    data object LoadActiveRoutines : RoutineEvent
    data object LoadRoutinesOrderedByPopularity : RoutineEvent
    data object LoadRecentRoutines : RoutineEvent
    data object ClearError : RoutineEvent
    data object ClearMessages : RoutineEvent
}