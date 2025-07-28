package edu.ucne.gymapp.presentation.routinexercise

import edu.ucne.gymapp.data.local.entities.RoutineExercise

sealed interface RoutineExerciseEvent {
    data class RoutineChange(val routineId: Int) : RoutineExerciseEvent
    data class ExerciseIdChange(val exerciseId: Int) : RoutineExerciseEvent
    data class OrderChange(val order: Int) : RoutineExerciseEvent
    data class SetsChange(val sets: Int) : RoutineExerciseEvent
    data class RepsChange(val reps: String) : RoutineExerciseEvent
    data class WeightChange(val weight: Float?) : RoutineExerciseEvent
    data class RestTimeChange(val restTime: Int) : RoutineExerciseEvent
    data class NotesChange(val notes: String?) : RoutineExerciseEvent
    data class LoadRoutineExerciseById(val id: Int) : RoutineExerciseEvent
    data class LoadRoutineExercisesByRoutine(val routineId: Int) : RoutineExerciseEvent
    data class LoadRoutineExercisesByExercise(val exerciseId: Int) : RoutineExerciseEvent
    data class ReorderExercises(val routineExercises: List<RoutineExercise>) : RoutineExerciseEvent
    data class AddExerciseToRoutine(val routineId: Int, val exerciseId: Int) : RoutineExerciseEvent
    data class RemoveExerciseFromRoutine(val routineExerciseId: Int) : RoutineExerciseEvent
    data class SelectRoutineExercise(val routineExercise: RoutineExercise) : RoutineExerciseEvent
    data object CreateRoutineExercise : RoutineExerciseEvent
    data object UpdateRoutineExercise : RoutineExerciseEvent
    data object DeleteRoutineExercise : RoutineExerciseEvent
    data object ClearError : RoutineExerciseEvent
    data object ClearMessages : RoutineExerciseEvent
}