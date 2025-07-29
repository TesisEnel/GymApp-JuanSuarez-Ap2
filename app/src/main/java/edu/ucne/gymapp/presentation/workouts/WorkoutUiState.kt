package edu.ucne.gymapp.presentation.workouts

import edu.ucne.gymapp.data.local.entities.Workout

data class WorkoutUiState(
    val userId: Int = 0,
    val routineId: Int = 0,
    val name: String = "",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val totalDuration: Int = 0,
    val status: String = "NOT_STARTED", // NOT_STARTED, IN_PROGRESS, PAUSED, COMPLETED, CANCELLED
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
    val workoutCancelled: Boolean = false
)
