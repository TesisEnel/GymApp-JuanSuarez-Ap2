package edu.ucne.gymapp.presentation.workouts

import edu.ucne.gymapp.data.local.entities.Workout

sealed interface WorkoutEvent {
    data class UserChange(val userId: Int) : WorkoutEvent
    data class RoutineIdChange(val routineId: Int) : WorkoutEvent
    data class NameChange(val name: String) : WorkoutEvent
    data class StartTimeChange(val startTime: Long) : WorkoutEvent
    data class EndTimeChange(val endTime: Long?) : WorkoutEvent
    data class StatusChange(val status: String) : WorkoutEvent
    data class NotesChange(val notes: String?) : WorkoutEvent
    data class LoadWorkoutById(val id: Int) : WorkoutEvent
    data class LoadWorkoutsByUser(val userId: Int) : WorkoutEvent
    data class LoadWorkoutsByRoutine(val routineId: Int) : WorkoutEvent
    data class LoadWorkoutsByStatus(val status: String) : WorkoutEvent
    data class StartWorkout(val routineId: Int) : WorkoutEvent
    data class PauseWorkout(val workoutId: Int) : WorkoutEvent
    data class ResumeWorkout(val workoutId: Int) : WorkoutEvent
    data class FinishWorkout(val workoutId: Int) : WorkoutEvent
    data class CancelWorkout(val workoutId: Int) : WorkoutEvent
    data class SelectWorkout(val workout: Workout) : WorkoutEvent
    data object CreateWorkout : WorkoutEvent
    data object UpdateWorkout : WorkoutEvent
    data object DeleteWorkout : WorkoutEvent
    data object LoadAllWorkouts : WorkoutEvent
    data object LoadActiveWorkouts : WorkoutEvent
    data object LoadRecentWorkouts : WorkoutEvent
    data object LoadWorkoutHistory : WorkoutEvent
    data object ClearError : WorkoutEvent
    data object ClearMessages : WorkoutEvent
}