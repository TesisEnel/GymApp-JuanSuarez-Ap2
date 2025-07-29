package edu.ucne.gymapp.presentation.workoutexercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.WorkoutExercise
import edu.ucne.gymapp.data.repository.WorkoutExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutExerciseViewModel @Inject constructor(
    private val workoutExerciseRepository: WorkoutExerciseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutExerciseUiState())
    val state = _state.asStateFlow()

    private fun createWorkoutExercise() {
        viewModelScope.launch {
            val currentState = _state.value

            val workoutExercise = WorkoutExercise(
                workoutId = currentState.workoutId,
                exerciseId = currentState.exerciseId,
                order = currentState.order,
                plannedSets = currentState.plannedSets,
                completedSets = currentState.completedSets,
                status = currentState.status,
                startTime = currentState.startTime,
                endTime = currentState.endTime,
                notes = currentState.notes
            )

            workoutExerciseRepository.insertWorkoutExercise(workoutExercise).collect { result ->
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
                                successMessage = "Ejercicio agregado exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear ejercicio",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateWorkoutExercise() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedExercise = currentState.selectedWorkoutExercise ?: return@launch

            val updatedExercise = selectedExercise.copy(
                workoutId = currentState.workoutId,
                exerciseId = currentState.exerciseId,
                order = currentState.order,
                plannedSets = currentState.plannedSets,
                completedSets = currentState.completedSets,
                status = currentState.status,
                startTime = currentState.startTime,
                endTime = currentState.endTime,
                notes = currentState.notes
            )

            workoutExerciseRepository.updateWorkoutExercise(updatedExercise).collect { result ->
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
                                successMessage = "Ejercicio actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar ejercicio",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteWorkoutExercise() {
        viewModelScope.launch {
            val selectedExercise = _state.value.selectedWorkoutExercise ?: return@launch

            workoutExerciseRepository.deleteWorkoutExercise(selectedExercise).collect { result ->
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
                                successMessage = "Ejercicio eliminado exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al eliminar ejercicio",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutExerciseById(id: Int) {
        viewModelScope.launch {
            workoutExerciseRepository.getWorkoutExerciseById(id).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val exercise = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                selectedWorkoutExercise = exercise,
                                workoutId = exercise?.workoutId ?: 0,
                                exerciseId = exercise?.exerciseId ?: 0,
                                order = exercise?.order ?: 1,
                                plannedSets = exercise?.plannedSets ?: 3,
                                completedSets = exercise?.completedSets ?: 0,
                                status = exercise?.status ?: "PENDING",
                                startTime = exercise?.startTime,
                                endTime = exercise?.endTime,
                                notes = exercise?.notes,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar ejercicio"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutExercisesByWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutExerciseRepository.getExercisesByWorkout(workoutId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val exercises = result.data ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                exercisesInWorkout = exercises,
                                workoutExercises = exercises,
                                totalExercises = exercises.size,
                                errorMessage = null
                            )
                        }
                        updateNavigationState()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar ejercicios del entrenamiento"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWorkoutExercisesByStatus(workoutId: Int, status: String) {
        viewModelScope.launch {
            workoutExerciseRepository.getExercisesByStatus(workoutId, status).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val exercises = result.data ?: emptyList()
                        _state.update { currentState ->
                            when (status) {
                                "PENDING" -> currentState.copy(
                                    isLoading = false,
                                    pendingWorkoutExercises = exercises,
                                    errorMessage = null
                                )
                                "IN_PROGRESS" -> currentState.copy(
                                    isLoading = false,
                                    activeWorkoutExercises = exercises,
                                    errorMessage = null
                                )
                                "COMPLETED" -> currentState.copy(
                                    isLoading = false,
                                    completedWorkoutExercises = exercises,
                                    errorMessage = null
                                )
                                else -> currentState.copy(
                                    isLoading = false,
                                    workoutExercises = exercises,
                                    errorMessage = null
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar ejercicios por estado"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startExercise(workoutExerciseId: Int) {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            workoutExerciseRepository.startExercise(workoutExerciseId, "IN_PROGRESS", startTime).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                exerciseStarted = true,
                                isExerciseActive = true,
                                startTime = startTime,
                                status = "IN_PROGRESS",
                                successMessage = "Ejercicio iniciado",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al iniciar ejercicio"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun completeExercise(workoutExerciseId: Int) {
        viewModelScope.launch {
            val endTime = System.currentTimeMillis()
            workoutExerciseRepository.finishExercise(workoutExerciseId, "COMPLETED", endTime).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                exerciseCompleted = true,
                                isExerciseActive = false,
                                endTime = endTime,
                                status = "COMPLETED",
                                successMessage = "Ejercicio completado",
                                errorMessage = null
                            )
                        }
                        updateWorkoutProgress()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al completar ejercicio"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun skipExercise(workoutExerciseId: Int) {
        viewModelScope.launch {
            val endTime = System.currentTimeMillis()
            workoutExerciseRepository.finishExercise(workoutExerciseId, "SKIPPED", endTime).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                exerciseSkipped = true,
                                isExerciseActive = false,
                                endTime = endTime,
                                status = "SKIPPED",
                                successMessage = "Ejercicio saltado",
                                errorMessage = null
                            )
                        }
                        updateWorkoutProgress()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al saltar ejercicio"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun completeSet(workoutExerciseId: Int) {
        viewModelScope.launch {
            val currentState = _state.value
            val newCompletedSets = currentState.completedSets + 1

            workoutExerciseRepository.updateCompletedSets(workoutExerciseId, newCompletedSets).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                setCompleted = true,
                                completedSets = newCompletedSets,
                                currentSet = minOf(newCompletedSets + 1, it.plannedSets),
                                successMessage = "Set completado",
                                errorMessage = null
                            )
                        }
                        updateExerciseProgress()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al completar set"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun moveToNextExercise() {
        val currentState = _state.value
        if (!currentState.isLastExercise) {
            val newIndex = currentState.currentExerciseIndex + 1
            _state.update {
                it.copy(
                    currentExerciseIndex = newIndex,
                    movedToNext = true,
                    isFirstExercise = newIndex == 0,
                    isLastExercise = newIndex == currentState.totalExercises - 1
                )
            }
            updateSelectedExercise(newIndex)
        }
    }

    private fun moveToPreviousExercise() {
        val currentState = _state.value
        if (!currentState.isFirstExercise) {
            val newIndex = currentState.currentExerciseIndex - 1
            _state.update {
                it.copy(
                    currentExerciseIndex = newIndex,
                    movedToPrevious = true,
                    isFirstExercise = newIndex == 0,
                    isLastExercise = newIndex == currentState.totalExercises - 1
                )
            }
            updateSelectedExercise(newIndex)
        }
    }

    private fun updateSelectedExercise(index: Int) {
        val exercises = _state.value.exercisesInWorkout
        if (index in exercises.indices) {
            val selectedExercise = exercises[index]
            _state.update {
                it.copy(
                    selectedWorkoutExercise = selectedExercise,
                    workoutId = selectedExercise.workoutId,
                    exerciseId = selectedExercise.exerciseId,
                    order = selectedExercise.order,
                    plannedSets = selectedExercise.plannedSets,
                    completedSets = selectedExercise.completedSets,
                    status = selectedExercise.status,
                    startTime = selectedExercise.startTime,
                    endTime = selectedExercise.endTime,
                    notes = selectedExercise.notes,
                    currentSet = minOf(selectedExercise.completedSets + 1, selectedExercise.plannedSets),
                    isExerciseActive = selectedExercise.status == "IN_PROGRESS"
                )
            }
            updateExerciseProgress()
        }
    }

    private fun updateNavigationState() {
        val currentState = _state.value
        _state.update {
            it.copy(
                isFirstExercise = currentState.currentExerciseIndex == 0,
                isLastExercise = currentState.currentExerciseIndex == currentState.totalExercises - 1
            )
        }
    }

    private fun updateExerciseProgress() {
        val currentState = _state.value
        val progress = if (currentState.plannedSets > 0) {
            currentState.completedSets.toFloat() / currentState.plannedSets.toFloat()
        } else 0f

        _state.update {
            it.copy(exerciseProgress = progress)
        }
    }

    private fun updateWorkoutProgress() {
        val currentState = _state.value
        val completedExercises = currentState.exercisesInWorkout.count {
            it.status == "COMPLETED" || it.status == "SKIPPED"
        }
        val progress = if (currentState.totalExercises > 0) {
            completedExercises.toFloat() / currentState.totalExercises.toFloat()
        } else 0f

        _state.update {
            it.copy(workoutProgress = progress)
        }
    }

    fun onEvent(event: WorkoutExerciseEvent) {
        when (event) {
            is WorkoutExerciseEvent.WorkoutIdChange -> {
                _state.update { it.copy(workoutId = event.workoutId) }
            }
            is WorkoutExerciseEvent.ExerciseIdChange -> {
                _state.update { it.copy(exerciseId = event.exerciseId) }
            }
            is WorkoutExerciseEvent.OrderChange -> {
                _state.update { it.copy(order = event.order) }
            }
            is WorkoutExerciseEvent.PlannedSetsChange -> {
                _state.update { it.copy(plannedSets = event.plannedSets) }
                updateExerciseProgress()
            }
            is WorkoutExerciseEvent.CompletedSetsChange -> {
                _state.update { it.copy(completedSets = event.completedSets) }
                updateExerciseProgress()
            }
            is WorkoutExerciseEvent.StatusChange -> {
                _state.update { it.copy(status = event.status) }
            }
            is WorkoutExerciseEvent.StartTimeChange -> {
                _state.update { it.copy(startTime = event.startTime) }
            }
            is WorkoutExerciseEvent.EndTimeChange -> {
                _state.update { it.copy(endTime = event.endTime) }
            }
            is WorkoutExerciseEvent.NotesChange -> {
                _state.update { it.copy(notes = event.notes) }
            }
            is WorkoutExerciseEvent.LoadWorkoutExerciseById -> {
                loadWorkoutExerciseById(event.id)
            }
            is WorkoutExerciseEvent.LoadWorkoutExercisesByWorkout -> {
                loadWorkoutExercisesByWorkout(event.workoutId)
            }
            is WorkoutExerciseEvent.LoadWorkoutExercisesByStatus -> {
                val currentWorkoutId = _state.value.workoutId
                loadWorkoutExercisesByStatus(currentWorkoutId, event.status)
            }
            is WorkoutExerciseEvent.StartExercise -> {
                startExercise(event.workoutExerciseId)
            }
            is WorkoutExerciseEvent.CompleteExercise -> {
                completeExercise(event.workoutExerciseId)
            }
            is WorkoutExerciseEvent.SkipExercise -> {
                skipExercise(event.workoutExerciseId)
            }
            is WorkoutExerciseEvent.CompleteSet -> {
                completeSet(event.workoutExerciseId)
            }
            is WorkoutExerciseEvent.SelectWorkoutExercise -> {
                val exercises = _state.value.exercisesInWorkout
                val index = exercises.indexOf(event.workoutExercise)
                if (index != -1) {
                    _state.update { it.copy(currentExerciseIndex = index) }
                    updateSelectedExercise(index)
                }
            }
            is WorkoutExerciseEvent.MoveToNextExercise -> {
                moveToNextExercise()
            }
            is WorkoutExerciseEvent.MoveToPreviousExercise -> {
                moveToPreviousExercise()
            }
            is WorkoutExerciseEvent.CreateWorkoutExercise -> {
                createWorkoutExercise()
            }
            is WorkoutExerciseEvent.UpdateWorkoutExercise -> {
                updateWorkoutExercise()
            }
            is WorkoutExerciseEvent.DeleteWorkoutExercise -> {
                deleteWorkoutExercise()
            }
            is WorkoutExerciseEvent.LoadActiveWorkoutExercises -> {
                val currentWorkoutId = _state.value.workoutId
                loadWorkoutExercisesByStatus(currentWorkoutId, "IN_PROGRESS")
            }
            is WorkoutExerciseEvent.LoadCompletedWorkoutExercises -> {
                val currentWorkoutId = _state.value.workoutId
                loadWorkoutExercisesByStatus(currentWorkoutId, "COMPLETED")
            }
            is WorkoutExerciseEvent.LoadPendingWorkoutExercises -> {
                val currentWorkoutId = _state.value.workoutId
                loadWorkoutExercisesByStatus(currentWorkoutId, "PENDING")
            }
            is WorkoutExerciseEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is WorkoutExerciseEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null,
                        isCreated = false,
                        isUpdated = false,
                        isDeleted = false,
                        exerciseStarted = false,
                        exerciseCompleted = false,
                        exerciseSkipped = false,
                        setCompleted = false,
                        movedToNext = false,
                        movedToPrevious = false
                    )
                }
            }
        }
    }
}