package edu.ucne.gymapp.presentation.workoutexercises

import edu.ucne.gymapp.data.local.entities.WorkoutExercise

sealed interface WorkoutExerciseEvent {
    data class WorkoutIdChange(val workoutId: Int) : WorkoutExerciseEvent
    data class ExerciseIdChange(val exerciseId: Int) : WorkoutExerciseEvent
    data class OrderChange(val order: Int) : WorkoutExerciseEvent
    data class PlannedSetsChange(val plannedSets: Int) : WorkoutExerciseEvent
    data class CompletedSetsChange(val completedSets: Int) : WorkoutExerciseEvent
    data class StatusChange(val status: String) : WorkoutExerciseEvent
    data class StartTimeChange(val startTime: Long?) : WorkoutExerciseEvent
    data class EndTimeChange(val endTime: Long?) : WorkoutExerciseEvent
    data class NotesChange(val notes: String?) : WorkoutExerciseEvent
    data class LoadWorkoutExerciseById(val id: Int) : WorkoutExerciseEvent
    data class LoadWorkoutExercisesByWorkout(val workoutId: Int) : WorkoutExerciseEvent
    data class LoadWorkoutExercisesByStatus(val status: String) : WorkoutExerciseEvent
    data class StartExercise(val workoutExerciseId: Int) : WorkoutExerciseEvent
    data class CompleteExercise(val workoutExerciseId: Int) : WorkoutExerciseEvent
    data class SkipExercise(val workoutExerciseId: Int) : WorkoutExerciseEvent
    data class CompleteSet(val workoutExerciseId: Int) : WorkoutExerciseEvent
    data class SelectWorkoutExercise(val workoutExercise: WorkoutExercise) : WorkoutExerciseEvent
    data object MoveToNextExercise : WorkoutExerciseEvent
    data object MoveToPreviousExercise : WorkoutExerciseEvent
    data object CreateWorkoutExercise : WorkoutExerciseEvent
    data object UpdateWorkoutExercise : WorkoutExerciseEvent
    data object DeleteWorkoutExercise : WorkoutExerciseEvent
    data object LoadActiveWorkoutExercises : WorkoutExerciseEvent
    data object LoadCompletedWorkoutExercises : WorkoutExerciseEvent
    data object LoadPendingWorkoutExercises : WorkoutExerciseEvent
    data object ClearError : WorkoutExerciseEvent
    data object ClearMessages : WorkoutExerciseEvent
}