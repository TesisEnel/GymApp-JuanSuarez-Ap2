package edu.ucne.gymapp.presentation.routinexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.RoutineExercise
import edu.ucne.gymapp.data.repository.RoutineExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineExerciseViewModel @Inject constructor(
    private val routineExerciseRepository: RoutineExerciseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RoutineExerciseUiState())
    val state = _state.asStateFlow()

    private fun createRoutineExercise() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.routineId == 0 || currentState.exerciseId == 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Debe seleccionar una rutina y un ejercicio válidos"
                    )
                }
                return@launch
            }

            if (currentState.sets <= 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El número de series debe ser mayor a 0"
                    )
                }
                return@launch
            }

            if (currentState.reps.isBlank()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Las repeticiones no pueden estar vacías"
                    )
                }
                return@launch
            }

            val routineExercise = RoutineExercise(
                routineId = currentState.routineId,
                exerciseId = currentState.exerciseId,
                order = currentState.order,
                sets = currentState.sets,
                reps = currentState.reps.trim(),
                weight = currentState.weight,
                restTime = currentState.restTime,
                notes = currentState.notes?.trim()
            )

            routineExerciseRepository.insertRoutineExercise(routineExercise).collect { result ->
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
                                successMessage = "Ejercicio agregado a la rutina exitosamente",
                                errorMessage = null
                            )
                        }
                        loadExercisesByRoutine(currentState.routineId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al agregar ejercicio",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateRoutineExercise() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedExercise = currentState.selectedRoutineExercise

            if (selectedExercise == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay ejercicio seleccionado para actualizar"
                    )
                }
                return@launch
            }

            if (currentState.sets <= 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El número de series debe ser mayor a 0"
                    )
                }
                return@launch
            }

            if (currentState.reps.isBlank()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Las repeticiones no pueden estar vacías"
                    )
                }
                return@launch
            }

            val updatedRoutineExercise = selectedExercise.copy(
                order = currentState.order,
                sets = currentState.sets,
                reps = currentState.reps.trim(),
                weight = currentState.weight,
                restTime = currentState.restTime,
                notes = currentState.notes?.trim()
            )

            routineExerciseRepository.updateRoutineExercise(updatedRoutineExercise).collect { result ->
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
                        loadExercisesByRoutine(selectedExercise.routineId)
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

    private fun deleteRoutineExercise() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedExercise = currentState.selectedRoutineExercise

            if (selectedExercise == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay ejercicio seleccionado para eliminar"
                    )
                }
                return@launch
            }

            routineExerciseRepository.deleteRoutineExercise(selectedExercise).collect { result ->
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
                                selectedRoutineExercise = null,
                                successMessage = "Ejercicio eliminado de la rutina exitosamente",
                                errorMessage = null
                            )
                        }
                        loadExercisesByRoutine(selectedExercise.routineId)
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

    private fun loadRoutineExerciseById(id: Int) {
        viewModelScope.launch {
            routineExerciseRepository.getRoutineExerciseById(id).collect { result ->
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
                        val routineExercise = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                selectedRoutineExercise = routineExercise,
                                routineId = routineExercise?.routineId ?: 0,
                                exerciseId = routineExercise?.exerciseId ?: 0,
                                order = routineExercise?.order ?: 1,
                                sets = routineExercise?.sets ?: 3,
                                reps = routineExercise?.reps ?: "10",
                                weight = routineExercise?.weight,
                                restTime = routineExercise?.restTime ?: 90,
                                notes = routineExercise?.notes,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar el ejercicio"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadExercisesByRoutine(routineId: Int) {
        viewModelScope.launch {
            routineExerciseRepository.getExercisesByRoutine(routineId).collect { result ->
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
                                exercisesInRoutine = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar ejercicios de la rutina"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadRoutinesWithExercise(exerciseId: Int) {
        viewModelScope.launch {
            routineExerciseRepository.getRoutinesWithExercise(exerciseId).collect { result ->
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
                                routinesWithExercise = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas con el ejercicio"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addExerciseToRoutine(routineId: Int, exerciseId: Int) {
        viewModelScope.launch {
            routineExerciseRepository.getMaxOrderInRoutine(routineId).collect { maxOrderResult ->
                when (maxOrderResult) {
                    is Resource.Success -> {
                        val nextOrder = (maxOrderResult.data ?: 0) + 1

                        val routineExercise = RoutineExercise(
                            routineId = routineId,
                            exerciseId = exerciseId,
                            order = nextOrder,
                            sets = 3,
                            reps = "10",
                            weight = null,
                            restTime = 90,
                            notes = null
                        )

                        routineExerciseRepository.insertRoutineExercise(routineExercise).collect { result ->
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
                                            exerciseAdded = true,
                                            successMessage = "Ejercicio agregado a la rutina",
                                            errorMessage = null
                                        )
                                    }
                                    loadExercisesByRoutine(routineId)
                                }
                                is Resource.Error -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            errorMessage = result.message ?: "Error al agregar ejercicio a la rutina",
                                            successMessage = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = maxOrderResult.message ?: "Error al obtener orden de ejercicios"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun removeExerciseFromRoutine(routineExerciseId: Int) {
        viewModelScope.launch {
            val exerciseToRemove = _state.value.exercisesInRoutine.find { it.routineExerciseId == routineExerciseId }

            if (exerciseToRemove == null) {
                _state.update {
                    it.copy(
                        errorMessage = "No se encontró el ejercicio a eliminar"
                    )
                }
                return@launch
            }

            routineExerciseRepository.deleteRoutineExercise(exerciseToRemove).collect { result ->
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
                                exerciseRemoved = true,
                                successMessage = "Ejercicio eliminado de la rutina",
                                errorMessage = null
                            )
                        }
                        loadExercisesByRoutine(exerciseToRemove.routineId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al eliminar ejercicio de la rutina",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun reorderExercises(routineExercises: List<RoutineExercise>) {
        viewModelScope.launch {
            _state.update { it.copy(isReordering = true) }

            try {
                routineExercises.forEachIndexed { index, exercise ->
                    val updatedExercise = exercise.copy(order = index + 1)
                    routineExerciseRepository.updateRoutineExercise(updatedExercise).collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                _state.update {
                                    it.copy(
                                        isReordering = false,
                                        errorMessage = result.message ?: "Error al reordenar ejercicios"
                                    )
                                }
                                return@collect
                            }
                            else -> { /* Continuar */ }
                        }
                    }
                }

                _state.update {
                    it.copy(
                        isReordering = false,
                        exercisesReordered = true,
                        exercisesInRoutine = routineExercises,
                        successMessage = "Ejercicios reordenados exitosamente"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isReordering = false,
                        errorMessage = "Error al reordenar ejercicios: ${e.message}"
                    )
                }
            }
        }
    }

    fun onEvent(event: RoutineExerciseEvent) {
        when (event) {
            is RoutineExerciseEvent.RoutineChange -> {
                _state.update { it.copy(routineId = event.routineId) }
            }
            is RoutineExerciseEvent.ExerciseIdChange -> {
                _state.update { it.copy(exerciseId = event.exerciseId) }
            }
            is RoutineExerciseEvent.OrderChange -> {
                _state.update { it.copy(order = event.order) }
            }
            is RoutineExerciseEvent.SetsChange -> {
                _state.update { it.copy(sets = event.sets) }
            }
            is RoutineExerciseEvent.RepsChange -> {
                _state.update { it.copy(reps = event.reps) }
            }
            is RoutineExerciseEvent.WeightChange -> {
                _state.update { it.copy(weight = event.weight) }
            }
            is RoutineExerciseEvent.RestTimeChange -> {
                _state.update { it.copy(restTime = event.restTime) }
            }
            is RoutineExerciseEvent.NotesChange -> {
                _state.update { it.copy(notes = event.notes) }
            }
            is RoutineExerciseEvent.LoadRoutineExerciseById -> {
                loadRoutineExerciseById(event.id)
            }
            is RoutineExerciseEvent.LoadRoutineExercisesByRoutine -> {
                loadExercisesByRoutine(event.routineId)
            }
            is RoutineExerciseEvent.LoadRoutineExercisesByExercise -> {
                loadRoutinesWithExercise(event.exerciseId)
            }
            is RoutineExerciseEvent.ReorderExercises -> {
                reorderExercises(event.routineExercises)
            }
            is RoutineExerciseEvent.AddExerciseToRoutine -> {
                addExerciseToRoutine(event.routineId, event.exerciseId)
            }
            is RoutineExerciseEvent.RemoveExerciseFromRoutine -> {
                removeExerciseFromRoutine(event.routineExerciseId)
            }
            is RoutineExerciseEvent.SelectRoutineExercise -> {
                val exercise = event.routineExercise
                _state.update {
                    it.copy(
                        selectedRoutineExercise = exercise,
                        routineId = exercise.routineId,
                        exerciseId = exercise.exerciseId,
                        order = exercise.order,
                        sets = exercise.sets,
                        reps = exercise.reps,
                        weight = exercise.weight,
                        restTime = exercise.restTime,
                        notes = exercise.notes
                    )
                }
            }
            is RoutineExerciseEvent.CreateRoutineExercise -> {
                createRoutineExercise()
            }
            is RoutineExerciseEvent.UpdateRoutineExercise -> {
                updateRoutineExercise()
            }
            is RoutineExerciseEvent.DeleteRoutineExercise -> {
                deleteRoutineExercise()
            }
            is RoutineExerciseEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is RoutineExerciseEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null,
                        isCreated = false,
                        isUpdated = false,
                        isDeleted = false,
                        exerciseAdded = false,
                        exerciseRemoved = false,
                        exercisesReordered = false
                    )
                }
            }
        }
    }
}