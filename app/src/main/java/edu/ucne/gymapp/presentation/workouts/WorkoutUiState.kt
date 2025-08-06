package edu.ucne.gymapp.presentation.workouts

import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.RoutineExercise

data class WorkoutUiState(
    val userId: Int = 1,
    val routineId: Int = 0,
    val name: String = "",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val totalDuration: Int = 0,
    val status: String = "NOT_STARTED",
    val notes: String? = null,
    val workouts: List<Workout> = emptyList(),
    val selectedWorkout: Workout? = null,
    val activeWorkouts: List<Workout> = emptyList(),
    val recentWorkouts: List<Workout> = emptyList(),
    val workoutHistory: List<Workout> = emptyList(),
    val filteredWorkouts: List<Workout> = emptyList(),
    val currentWorkout: Workout? = null,
    val isWorkoutActive: Boolean = false,
    val workoutTimer: Long = 0,
    val isPaused: Boolean = false,
    val selectedStatus: String = "",
    val selectedDateRange: Pair<Long?, Long?> = Pair(null, null),
    val workoutStats: Map<String, Any> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val workoutStarted: Boolean = false,
    val workoutPaused: Boolean = false,
    val workoutResumed: Boolean = false,
    val workoutFinished: Boolean = false,
    val workoutCancelled: Boolean = false,

    val currentScreen: WorkoutScreen = WorkoutScreen.DASHBOARD,
    val showRoutineSelector: Boolean = false,
    val showQuickStart: Boolean = false,

    val selectedRoutine: Routine? = null,
    val availableRoutines: List<Routine> = emptyList(),
    val routineExercises: List<RoutineExercise> = emptyList(),
    val exercises: List<Exercise> = emptyList(),

    val currentExerciseIndex: Int = 0,
    val currentSet: Int = 1,
    val totalSets: Int = 3,
    val restTimer: Long = 0,
    val isResting: Boolean = false,
    val restDuration: Long = 90,

    val todayWorkouts: Int = 0,
    val todayDuration: Long = 0,
    val weeklyStreak: Int = 0,
    val monthlyWorkouts: Int = 0,
    val favoriteRoutine: String = "",
    val totalCalories: Int = 0,

    val showMotivationDialog: Boolean = false,
    val showCompletionCelebration: Boolean = false,
    val showRestDialog: Boolean = false,
    val motivationMessage: String = "",

    val autoAdvanceExercise: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val keepScreenOn: Boolean = true
)

enum class WorkoutScreen {
    DASHBOARD,
    ROUTINE_SELECTOR,
    ACTIVE_WORKOUT,
    REST_SCREEN,
    WORKOUT_COMPLETE,
    WORKOUT_HISTORY
}