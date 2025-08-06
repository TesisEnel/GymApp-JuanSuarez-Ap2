package edu.ucne.gymapp.presentation.workouts

import edu.ucne.gymapp.data.local.entities.Routine
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

    data class NavigateToScreen(val screen: WorkoutScreen) : WorkoutEvent
    data object ShowRoutineSelector : WorkoutEvent
    data object ShowQuickStart : WorkoutEvent
    data object BackToDashboard : WorkoutEvent

    data class SelectRoutine(val routine: Routine) : WorkoutEvent
    data class LoadAvailableRoutines(val userId: Int) : WorkoutEvent
    data class StartWorkoutWithRoutine(val routine: Routine) : WorkoutEvent

    data object StartQuickWorkout : WorkoutEvent
    data object NextExercise : WorkoutEvent
    data object PreviousExercise : WorkoutEvent
    data class CompleteSet(val setNumber: Int) : WorkoutEvent
    data object StartRest : WorkoutEvent
    data object SkipRest : WorkoutEvent
    data object CompleteExercise : WorkoutEvent

    data object UpdateWorkoutTimer : WorkoutEvent
    data object UpdateRestTimer : WorkoutEvent
    data object ResetTimers : WorkoutEvent

    data object ShowMotivation : WorkoutEvent
    data object ShowCelebration : WorkoutEvent
    data object DismissDialogs : WorkoutEvent
}