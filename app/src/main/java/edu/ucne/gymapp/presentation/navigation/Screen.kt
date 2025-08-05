package edu.ucne.gymapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen{
    @Serializable
    data object Login: Screen()

    @Serializable
    data object Register: Screen()

    @Serializable
    data object Main: Screen()

    @Serializable
    data class Exercise(val muscleGroupId: Int? = null) : Screen()

    @Serializable
    data object ExerciseSet: Screen()

    @Serializable
    data object MuscleGroup: Screen()

    @Serializable
    data object Routine: Screen()

    @Serializable
    data object RoutineExercise: Screen()

    @Serializable
    data object UserPreferences: Screen()

    @Serializable
    data object Workout: Screen()

    @Serializable
    data object WorkoutExercise: Screen()
}