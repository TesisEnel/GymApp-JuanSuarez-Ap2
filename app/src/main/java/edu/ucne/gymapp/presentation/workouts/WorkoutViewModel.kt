package edu.ucne.gymapp.presentation.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.repository.WorkoutRepository
import edu.ucne.gymapp.data.repository.RoutineRepository
import edu.ucne.gymapp.data.repository.RoutineExerciseRepository
import edu.ucne.gymapp.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val routineRepository: RoutineRepository,
    private val routineExerciseRepository: RoutineExerciseRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutUiState())
    val state = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            loadRecentWorkouts()
            loadAvailableRoutines()
        }
    }

    private fun navigateToScreen(screen: WorkoutScreen) {
        _state.update { it.copy(currentScreen = screen) }
    }

    private fun showRoutineSelector() {
        _state.update {
            it.copy(
                currentScreen = WorkoutScreen.ROUTINE_SELECTOR,
                showRoutineSelector = true
            )
        }
        loadAvailableRoutines()
    }

    private fun backToDashboard() {
        _state.update {
            it.copy(
                currentScreen = WorkoutScreen.DASHBOARD,
                showRoutineSelector = false,
                showQuickStart = false
            )
        }
    }

    private fun loadAvailableRoutines() {
        viewModelScope.launch {
            routineRepository.getRoutines().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                availableRoutines = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun selectRoutine(routine: Routine) {
        _state.update {
            it.copy(selectedRoutine = routine)
        }
        loadRoutineExercises(routine.routineId)
    }

    private fun loadRoutineExercises(routineId: Int) {
        viewModelScope.launch {
            routineExerciseRepository.getRoutineExercises(routineId).collect { routineExerciseResult ->
                when (routineExerciseResult) {
                    is Resource.Success -> {
                        val routineExercises = routineExerciseResult.data ?: emptyList()
                        _state.update { it.copy(routineExercises = routineExercises) }

                        if (routineExercises.isNotEmpty()) {
                            val exerciseIds = routineExercises.map { it.exerciseId }
                            exerciseRepository.getExercisesByIds(exerciseIds).collect { exerciseResult ->
                                when (exerciseResult) {
                                    is Resource.Success -> {
                                        val exercises = exerciseResult.data ?: emptyList()
                                        _state.update {
                                            it.copy(
                                                exercises = exercises,
                                                errorMessage = null
                                            )
                                        }
                                    }
                                    is Resource.Error -> {
                                        _state.update {
                                            it.copy(errorMessage = "Error al cargar ejercicios")
                                        }
                                    }
                                    is Resource.Loading -> {
                                        _state.update {
                                            it.copy(
                                                isLoading = true,
                                                errorMessage = null,
                                                successMessage = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(errorMessage = "Error al cargar ejercicios de la rutina")
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startWorkoutWithRoutine(routine: Routine) {
        viewModelScope.launch {
            val workout = Workout(
                userId = _state.value.userId,
                routineId = routine.routineId,
                name = routine.name,
                startTime = System.currentTimeMillis(),
                endTime = null,
                totalDuration = 0,
                status = "IN_PROGRESS",
                notes = null
            )

            workoutRepository.insertWorkout(workout).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                currentWorkout = workout,
                                selectedRoutine = routine,
                                isWorkoutActive = true,
                                currentScreen = WorkoutScreen.ACTIVE_WORKOUT,
                                startTime = workout.startTime,
                                workoutTimer = 0,
                                currentExerciseIndex = 0,
                                currentSet = 1,
                                isPaused = false,
                                successMessage = "¡Entrenamiento iniciado!"
                            )
                        }
                        loadRoutineExercises(routine.routineId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al iniciar entrenamiento"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startQuickWorkout() {
        viewModelScope.launch {
            val workout = Workout(
                userId = _state.value.userId,
                routineId = null,
                name = "Entrenamiento Libre - ${System.currentTimeMillis()}",
                startTime = System.currentTimeMillis(),
                endTime = null,
                totalDuration = 0,
                status = "IN_PROGRESS",
                notes = null
            )

            workoutRepository.insertWorkout(workout).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                currentWorkout = workout,
                                selectedRoutine = null,
                                isWorkoutActive = true,
                                currentScreen = WorkoutScreen.ACTIVE_WORKOUT,
                                startTime = workout.startTime,
                                workoutTimer = 0,
                                isPaused = false,
                                successMessage = "¡Entrenamiento libre iniciado!"
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(errorMessage = "Error al iniciar entrenamiento libre")
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun pauseWorkout() {
        _state.update {
            it.copy(
                isPaused = !it.isPaused,
                successMessage = if (!it.isPaused) "Entrenamiento pausado" else "Entrenamiento reanudado"
            )
        }
    }

    private fun finishWorkout() {
        viewModelScope.launch {
            val currentState = _state.value
            val currentTime = System.currentTimeMillis()
            val duration = ((currentTime - currentState.startTime) / 1000).toInt()

            currentState.currentWorkout?.let { workout ->
                val finishedWorkout = workout.copy(
                    endTime = currentTime,
                    totalDuration = duration,
                    status = "COMPLETED"
                )

                workoutRepository.updateWorkout(finishedWorkout).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isWorkoutActive = false,
                                    isPaused = false,
                                    currentWorkout = null,
                                    currentScreen = WorkoutScreen.DASHBOARD,
                                    showCompletionCelebration = true,
                                    successMessage = "¡Entrenamiento completado! Duración: ${formatDuration(duration)}",
                                    todayWorkouts = it.todayWorkouts + 1,
                                    todayDuration = it.todayDuration + duration,
                                    weeklyStreak = it.weeklyStreak + 1
                                )
                            }
                            loadRecentWorkouts()
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(errorMessage = "Error al finalizar entrenamiento")
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
        }
    }

    private fun cancelWorkout() {
        viewModelScope.launch {
            val currentState = _state.value
            val currentTime = System.currentTimeMillis()
            val duration = ((currentTime - currentState.startTime) / 1000).toInt()

            currentState.currentWorkout?.let { workout ->
                val cancelledWorkout = workout.copy(
                    endTime = currentTime,
                    totalDuration = duration,
                    status = "CANCELLED"
                )

                workoutRepository.updateWorkout(cancelledWorkout).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isWorkoutActive = false,
                                    isPaused = false,
                                    currentWorkout = null,
                                    currentScreen = WorkoutScreen.DASHBOARD,
                                    successMessage = "Entrenamiento cancelado"
                                )
                            }
                            loadRecentWorkouts()
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(errorMessage = "Error al cancelar entrenamiento")
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
        }
    }

    private fun completeSet() {
        val currentState = _state.value
        val routineExercise = currentState.routineExercises.find {
            it.exerciseId == currentState.exercises.getOrNull(currentState.currentExerciseIndex)?.exerciseId
        }
        val totalSets = routineExercise?.sets ?: 3

        if (currentState.currentSet < totalSets) {
            _state.update {
                it.copy(
                    currentSet = it.currentSet + 1,
                    isResting = true,
                    restTimer = routineExercise?.restTime?.toLong() ?: 90L,
                    successMessage = "¡Serie completada! Descansa ${routineExercise?.restTime ?: 90} segundos"
                )
            }
        } else {
            nextExercise()
        }
    }

    private fun nextExercise() {
        val currentState = _state.value
        if (currentState.currentExerciseIndex < currentState.exercises.size - 1) {
            _state.update {
                it.copy(
                    currentExerciseIndex = it.currentExerciseIndex + 1,
                    currentSet = 1,
                    isResting = false,
                    restTimer = 0,
                    successMessage = "¡Ejercicio completado! Siguiente ejercicio"
                )
            }
        } else {
            finishWorkout()
        }
    }

    private fun startRest() {
        val currentState = _state.value
        val routineExercise = currentState.routineExercises.find {
            it.exerciseId == currentState.exercises.getOrNull(currentState.currentExerciseIndex)?.exerciseId
        }

        _state.update {
            it.copy(
                isResting = true,
                restTimer = routineExercise?.restTime?.toLong() ?: 90L
            )
        }
    }

    private fun skipRest() {
        _state.update {
            it.copy(
                isResting = false,
                restTimer = 0
            )
        }
    }

    private fun updateWorkoutTimer() {
        _state.update {
            it.copy(workoutTimer = it.workoutTimer + 1)
        }
    }

    private fun updateRestTimer() {
        val currentState = _state.value
        if (currentState.restTimer > 0) {
            _state.update {
                it.copy(restTimer = it.restTimer - 1)
            }
        } else {
            _state.update {
                it.copy(
                    isResting = false,
                    restTimer = 0
                )
            }
        }
    }

    private fun resetTimers() {
        _state.update {
            it.copy(
                workoutTimer = 0,
                restTimer = 0,
                isResting = false
            )
        }
    }

    private fun loadRecentWorkouts() {
        viewModelScope.launch {
            workoutRepository.getWorkoutsByUser(_state.value.userId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val workouts = result.data ?: emptyList()
                        val recentWorkouts = workouts.take(5)
                        _state.update {
                            it.copy(
                                workouts = workouts,
                                recentWorkouts = recentWorkouts,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(errorMessage = "Error al cargar entrenamientos recientes")
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun showMotivation() {
        val motivationMessages = listOf(
            "¡Estás haciendo un trabajo increíble!",
            "Cada repetición te acerca a tu objetivo",
            "Tu fuerza mental es tu mayor arma",
            "¡No pares, estás imparable!",
            "El dolor de hoy es la fuerza de mañana"
        )

        _state.update {
            it.copy(
                showMotivationDialog = true,
                motivationMessage = motivationMessages.random()
            )
        }
    }

    private fun showCelebration() {
        _state.update {
            it.copy(showCompletionCelebration = true)
        }
    }

    private fun dismissDialogs() {
        _state.update {
            it.copy(
                showMotivationDialog = false,
                showCompletionCelebration = false,
                showRestDialog = false
            )
        }
    }

    private fun clearMessages() {
        _state.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.NavigateToScreen -> {
                navigateToScreen(event.screen)
            }
            is WorkoutEvent.ShowRoutineSelector -> {
                showRoutineSelector()
            }
            is WorkoutEvent.ShowQuickStart -> {
                _state.update { it.copy(showQuickStart = true) }
            }
            is WorkoutEvent.BackToDashboard -> {
                backToDashboard()
            }
            is WorkoutEvent.SelectRoutine -> {
                selectRoutine(event.routine)
            }
            is WorkoutEvent.LoadAvailableRoutines -> {
                loadAvailableRoutines()
            }
            is WorkoutEvent.StartWorkoutWithRoutine -> {
                startWorkoutWithRoutine(event.routine)
            }
            is WorkoutEvent.StartQuickWorkout -> {
                startQuickWorkout()
            }
            is WorkoutEvent.PauseWorkout -> {
                pauseWorkout()
            }
            is WorkoutEvent.FinishWorkout -> {
                finishWorkout()
            }
            is WorkoutEvent.CancelWorkout -> {
                cancelWorkout()
            }
            is WorkoutEvent.CompleteSet -> {
                completeSet()
            }
            is WorkoutEvent.NextExercise -> {
                nextExercise()
            }
            is WorkoutEvent.StartRest -> {
                startRest()
            }
            is WorkoutEvent.SkipRest -> {
                skipRest()
            }
            is WorkoutEvent.UpdateWorkoutTimer -> {
                updateWorkoutTimer()
            }
            is WorkoutEvent.UpdateRestTimer -> {
                updateRestTimer()
            }
            is WorkoutEvent.ResetTimers -> {
                resetTimers()
            }
            is WorkoutEvent.LoadRecentWorkouts -> {
                loadRecentWorkouts()
            }
            is WorkoutEvent.ShowMotivation -> {
                showMotivation()
            }
            is WorkoutEvent.ShowCelebration -> {
                showCelebration()
            }
            is WorkoutEvent.DismissDialogs -> {
                dismissDialogs()
            }
            is WorkoutEvent.ClearMessages -> {
                clearMessages()
            }
            is WorkoutEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is WorkoutEvent.UserChange -> {
                _state.update { it.copy(userId = event.userId) }
            }
            is WorkoutEvent.NameChange -> {
                _state.update { it.copy(name = event.name) }
            }
            is WorkoutEvent.NotesChange -> {
                _state.update { it.copy(notes = event.notes) }
            }

            else -> {

            }
        }
    }

    private fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format("%d:%02d", minutes, secs)
        }
    }
}