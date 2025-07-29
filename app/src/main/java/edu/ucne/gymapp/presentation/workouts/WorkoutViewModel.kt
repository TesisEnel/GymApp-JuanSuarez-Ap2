package edu.ucne.gymapp.presentation.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WorkoutUiState())
    val state = _state.asStateFlow()

    private fun createWorkout() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.name.isBlank()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El nombre del entrenamiento no puede estar vacío"
                    )
                }
                return@launch
            }

            if (currentState.userId <= 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Usuario inválido"
                    )
                }
                return@launch
            }

            val workout = Workout(
                userId = currentState.userId,
                routineId = if (currentState.routineId > 0) currentState.routineId else null,
                name = currentState.name.trim(),
                startTime = currentState.startTime,
                endTime = currentState.endTime,
                totalDuration = currentState.totalDuration,
                status = currentState.status,
                notes = currentState.notes?.trim()
            )

            workoutRepository.insertWorkout(workout).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isCreated = true,
                                successMessage = "Entrenamiento creado exitosamente",
                                errorMessage = null
                            )
                        }
                        loadWorkoutsByUser(currentState.userId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateWorkout() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedWorkout = currentState.selectedWorkout

            if (selectedWorkout == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay entrenamiento seleccionado para actualizar"
                    )
                }
                return@launch
            }

            if (currentState.name.isBlank()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El nombre del entrenamiento no puede estar vacío"
                    )
                }
                return@launch
            }

            val updatedWorkout = selectedWorkout.copy(
                name = currentState.name.trim(),
                startTime = currentState.startTime,
                endTime = currentState.endTime,
                totalDuration = currentState.totalDuration,
                status = currentState.status,
                notes = currentState.notes?.trim()
            )

            workoutRepository.updateWorkout(updatedWorkout).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isUpdated = true,
                                selectedWorkout = updatedWorkout,
                                successMessage = "Entrenamiento actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                        loadWorkoutsByUser(currentState.userId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteWorkout() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedWorkout = currentState.selectedWorkout

            if (selectedWorkout == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay entrenamiento seleccionado para eliminar"
                    )
                }
                return@launch
            }

            workoutRepository.deleteWorkout(selectedWorkout).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isDeleted = true,
                                selectedWorkout = null,
                                successMessage = "Entrenamiento eliminado exitosamente",
                                errorMessage = null
                            )
                        }
                        loadWorkoutsByUser(currentState.userId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al eliminar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutById(id: Int) {
        viewModelScope.launch {
            workoutRepository.getWorkoutById(id).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                selectedWorkout = result.data,
                                errorMessage = null
                            )
                        }
                        result.data?.let { workout ->
                            populateStateFromWorkout(workout)
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar entrenamiento",
                                selectedWorkout = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutsByUser(userId: Int) {
        viewModelScope.launch {
            workoutRepository.getWorkoutsByUser(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                workouts = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar entrenamientos",
                                workouts = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutsByRoutine(routineId: Int) {
        viewModelScope.launch {
            workoutRepository.getWorkoutsByRoutine(routineId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                filteredWorkouts = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar entrenamientos por rutina",
                                filteredWorkouts = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutsByStatus(userId: Int, status: String) {
        viewModelScope.launch {
            workoutRepository.getWorkoutsByStatus(userId, status).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                filteredWorkouts = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar entrenamientos por estado",
                                filteredWorkouts = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadActiveWorkout(userId: Int) {
        viewModelScope.launch {
            workoutRepository.getActiveWorkout(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        val activeWorkout = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                currentWorkout = activeWorkout,
                                isWorkoutActive = activeWorkout != null,
                                isPaused = activeWorkout?.status == "PAUSED",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar entrenamiento activo",
                                currentWorkout = null,
                                isWorkoutActive = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startWorkout(routineId: Int) {
        viewModelScope.launch {
            val currentState = _state.value

            val workout = Workout(
                userId = currentState.userId,
                routineId = routineId,
                name = "Entrenamiento ${System.currentTimeMillis()}",
                startTime = System.currentTimeMillis(),
                endTime = null,
                totalDuration = 0,
                status = "IN_PROGRESS",
                notes = null
            )

            workoutRepository.insertWorkout(workout).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                workoutStarted = true,
                                isWorkoutActive = true,
                                status = "IN_PROGRESS",
                                startTime = workout.startTime,
                                successMessage = "Entrenamiento iniciado",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al iniciar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun pauseWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutRepository.pauseWorkout(workoutId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                workoutPaused = true,
                                isPaused = true,
                                status = "PAUSED",
                                successMessage = "Entrenamiento pausado",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al pausar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun resumeWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutRepository.resumeWorkout(workoutId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                workoutResumed = true,
                                isPaused = false,
                                status = "IN_PROGRESS",
                                successMessage = "Entrenamiento reanudado",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al reanudar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun finishWorkout(workoutId: Int) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val duration = ((currentTime - _state.value.startTime) / 1000).toInt()

            workoutRepository.finishWorkout(workoutId, "COMPLETED", currentTime, duration).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                workoutFinished = true,
                                isWorkoutActive = false,
                                isPaused = false,
                                status = "COMPLETED",
                                endTime = currentTime,
                                totalDuration = duration,
                                successMessage = "¡Entrenamiento completado!",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al finalizar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun cancelWorkout(workoutId: Int) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val duration = ((currentTime - _state.value.startTime) / 1000).toInt()

            workoutRepository.finishWorkout(workoutId, "CANCELLED", currentTime, duration).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                workoutCancelled = true,
                                isWorkoutActive = false,
                                isPaused = false,
                                status = "CANCELLED",
                                endTime = currentTime,
                                totalDuration = duration,
                                successMessage = "Entrenamiento cancelado",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cancelar entrenamiento",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun populateStateFromWorkout(workout: Workout) {
        _state.update {
            it.copy(
                userId = workout.userId,
                routineId = workout.routineId ?: 0,
                name = workout.name,
                startTime = workout.startTime,
                endTime = workout.endTime,
                totalDuration = workout.totalDuration,
                status = workout.status,
                notes = workout.notes
            )
        }
    }

    fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.UserChange -> {
                _state.update { it.copy(userId = event.userId) }
            }
            is WorkoutEvent.RoutineIdChange -> {
                _state.update { it.copy(routineId = event.routineId) }
            }
            is WorkoutEvent.NameChange -> {
                _state.update { it.copy(name = event.name) }
            }
            is WorkoutEvent.StartTimeChange -> {
                _state.update { it.copy(startTime = event.startTime) }
            }
            is WorkoutEvent.EndTimeChange -> {
                _state.update { it.copy(endTime = event.endTime) }
            }
            is WorkoutEvent.StatusChange -> {
                _state.update { it.copy(status = event.status) }
            }
            is WorkoutEvent.NotesChange -> {
                _state.update { it.copy(notes = event.notes) }
            }
            is WorkoutEvent.LoadWorkoutById -> {
                loadWorkoutById(event.id)
            }
            is WorkoutEvent.LoadWorkoutsByUser -> {
                loadWorkoutsByUser(event.userId)
            }
            is WorkoutEvent.LoadWorkoutsByRoutine -> {
                loadWorkoutsByRoutine(event.routineId)
            }
            is WorkoutEvent.LoadWorkoutsByStatus -> {
                loadWorkoutsByStatus(_state.value.userId, event.status)
            }
            is WorkoutEvent.StartWorkout -> {
                startWorkout(event.routineId)
            }
            is WorkoutEvent.PauseWorkout -> {
                pauseWorkout(event.workoutId)
            }
            is WorkoutEvent.ResumeWorkout -> {
                resumeWorkout(event.workoutId)
            }
            is WorkoutEvent.FinishWorkout -> {
                finishWorkout(event.workoutId)
            }
            is WorkoutEvent.CancelWorkout -> {
                cancelWorkout(event.workoutId)
            }
            is WorkoutEvent.SelectWorkout -> {
                _state.update {
                    it.copy(selectedWorkout = event.workout)
                }
                populateStateFromWorkout(event.workout)
            }
            is WorkoutEvent.CreateWorkout -> {
                createWorkout()
            }
            is WorkoutEvent.UpdateWorkout -> {
                updateWorkout()
            }
            is WorkoutEvent.DeleteWorkout -> {
                deleteWorkout()
            }
            is WorkoutEvent.LoadActiveWorkouts -> {
                loadActiveWorkout(_state.value.userId)
            }
            is WorkoutEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is WorkoutEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null,
                        isCreated = false,
                        isUpdated = false,
                        isDeleted = false,
                        workoutStarted = false,
                        workoutPaused = false,
                        workoutResumed = false,
                        workoutFinished = false,
                        workoutCancelled = false
                    )
                }
            }
            is WorkoutEvent.LoadAllWorkouts -> {
                loadWorkoutsByUser(_state.value.userId)
            }
            is WorkoutEvent.LoadRecentWorkouts -> {
                // Requeriría lógica adicional para filtrar entrenamientos recientes
                loadWorkoutsByUser(_state.value.userId)
            }
            is WorkoutEvent.LoadWorkoutHistory -> {
                // Cargar historial completo (misma lógica que LoadAllWorkouts)
                loadWorkoutsByUser(_state.value.userId)
            }
        }
    }
}