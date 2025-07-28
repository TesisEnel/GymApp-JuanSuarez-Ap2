package edu.ucne.gymapp.presentation.exercisesets

sealed interface ExerciseSetEvent {
    data class WorkoutExerciseChange(val workoutExerciseId: Int) : ExerciseSetEvent
    data class SetNumberChange(val setNumber: Int) : ExerciseSetEvent
    data class RepsChange(val reps: Int) : ExerciseSetEvent
    data class WeightChange(val weight: Float?) : ExerciseSetEvent
    data class RestTimeChange(val restTime: Int?) : ExerciseSetEvent
    data class DifficultyChange(val difficulty: Int) : ExerciseSetEvent
    data class LoadSetById(val id: Int) : ExerciseSetEvent
    data class LoadSetsByWorkoutExercise(val workoutExerciseId: Int) : ExerciseSetEvent
    data class MarkAsCompleted(val setId: Int) : ExerciseSetEvent
    data class UpdateProgress(val setId: Int, val reps: Int, val weight: Float?) : ExerciseSetEvent
    data class DeleteSetsByWorkoutExercise(val workoutExerciseId: Int) : ExerciseSetEvent
    data object CreateSet : ExerciseSetEvent
    data object CreateMultipleSets : ExerciseSetEvent
    data object UpdateSet : ExerciseSetEvent
    data object DeleteSet : ExerciseSetEvent
    data object LoadCompletedSets : ExerciseSetEvent
    data object LoadPendingSets : ExerciseSetEvent
    data object ClearError : ExerciseSetEvent
    data object ClearMessages : ExerciseSetEvent
}