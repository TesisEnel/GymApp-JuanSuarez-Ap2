package edu.ucne.gymapp.presentation.exercisesets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.ExerciseSet
import edu.ucne.gymapp.data.repository.ExerciseSetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSetViewModel @Inject constructor(
    private val exerciseSetRepository: ExerciseSetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExerciseSetUiState())
    val state = _state.asStateFlow()

    private fun createSet() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.workoutExerciseId == 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "ID de ejercicio requerido para crear la serie"
                    )
                }
                return@launch
            }

            if (currentState.reps <= 0) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El número de repeticiones debe ser mayor a 0"
                    )
                }
                return@launch
            }

            val exerciseSet = ExerciseSet(
                workoutExerciseId = currentState.workoutExerciseId,
                setNumber = currentState.setNumber,
                reps = currentState.reps,
                weight = currentState.weight,
                restTime = currentState.restTime,
                difficulty = currentState.difficulty,
                isCompleted = false
            )

            exerciseSetRepository.insertSet(exerciseSet).collect { result ->
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
                                successMessage = "Serie creada exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear la serie",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createMultipleSets() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.multipleSets.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay series para crear en lote"
                    )
                }
                return@launch
            }

            exerciseSetRepository.insertSets(currentState.multipleSets).collect { result ->
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
                                successMessage = "Series creadas exitosamente (${result.data?.size ?: 0} series)",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear las series",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateSet() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.selectedSet == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay serie seleccionada para actualizar"
                    )
                }
                return@launch
            }

            val updatedSet = currentState.selectedSet.copy(
                workoutExerciseId = currentState.workoutExerciseId,
                setNumber = currentState.setNumber,
                reps = currentState.reps,
                weight = currentState.weight,
                restTime = currentState.restTime,
                difficulty = currentState.difficulty
            )

            exerciseSetRepository.updateSet(updatedSet).collect { result ->
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
                                successMessage = "Serie actualizada exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar la serie",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteSet() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.selectedSet == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay serie seleccionada para eliminar"
                    )
                }
                return@launch
            }

            exerciseSetRepository.deleteSet(currentState.selectedSet).collect { result ->
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
                                successMessage = "Serie eliminada exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al eliminar la serie",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadSetById(id: Int) {
        viewModelScope.launch {
            exerciseSetRepository.getSetById(id).collect { result ->
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
                        val exerciseSet = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                selectedSet = exerciseSet,
                                workoutExerciseId = exerciseSet?.workoutExerciseId ?: 0,
                                setNumber = exerciseSet?.setNumber ?: 1,
                                reps = exerciseSet?.reps ?: 0,
                                weight = exerciseSet?.weight,
                                restTime = exerciseSet?.restTime,
                                difficulty = exerciseSet?.difficulty ?: 5,
                                isCompleted = exerciseSet?.isCompleted ?: false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar la serie",
                                selectedSet = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadSetsByWorkoutExercise(workoutExerciseId: Int) {
        viewModelScope.launch {
            exerciseSetRepository.getSetsByWorkoutExercise(workoutExerciseId).collect { result ->
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
                                sets = result.data ?: emptyList(),
                                totalSets = result.data?.size ?: 0,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar las series",
                                sets = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadCompletedSets() {
        viewModelScope.launch {
            val currentState = _state.value
            exerciseSetRepository.getCompletedSets(currentState.workoutExerciseId).collect { result ->
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
                                completedSets = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar las series completadas",
                                completedSets = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadPendingSets() {
        viewModelScope.launch {
            val currentState = _state.value
            exerciseSetRepository.getPendingSets(currentState.workoutExerciseId).collect { result ->
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
                                pendingSets = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar las series pendientes",
                                pendingSets = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun markAsCompleted(setId: Int) {
        viewModelScope.launch {
            exerciseSetRepository.markSetAsCompleted(setId).collect { result ->
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
                                isMarkedCompleted = true,
                                successMessage = "Serie marcada como completada",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al marcar la serie como completada",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateProgress(setId: Int, reps: Int, weight: Float?) {
        viewModelScope.launch {
            exerciseSetRepository.updateSetProgress(setId, reps, weight).collect { result ->
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
                                progressUpdated = true,
                                successMessage = "Progreso actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar el progreso",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteSetsByWorkoutExercise(workoutExerciseId: Int) {
        viewModelScope.launch {
            exerciseSetRepository.deleteSetsByWorkoutExercise(workoutExerciseId).collect { result ->
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
                                successMessage = "Series eliminadas exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al eliminar las series",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ExerciseSetEvent) {
        when (event) {
            is ExerciseSetEvent.WorkoutExerciseChange -> {
                _state.update { it.copy(workoutExerciseId = event.workoutExerciseId) }
            }
            is ExerciseSetEvent.SetNumberChange -> {
                _state.update { it.copy(setNumber = event.setNumber) }
            }
            is ExerciseSetEvent.RepsChange -> {
                _state.update { it.copy(reps = event.reps) }
            }
            is ExerciseSetEvent.WeightChange -> {
                _state.update { it.copy(weight = event.weight) }
            }
            is ExerciseSetEvent.RestTimeChange -> {
                _state.update { it.copy(restTime = event.restTime) }
            }
            is ExerciseSetEvent.DifficultyChange -> {
                _state.update { it.copy(difficulty = event.difficulty) }
            }
            is ExerciseSetEvent.LoadSetById -> {
                loadSetById(event.id)
            }
            is ExerciseSetEvent.LoadSetsByWorkoutExercise -> {
                loadSetsByWorkoutExercise(event.workoutExerciseId)
            }
            is ExerciseSetEvent.MarkAsCompleted -> {
                markAsCompleted(event.setId)
            }
            is ExerciseSetEvent.UpdateProgress -> {
                updateProgress(event.setId, event.reps, event.weight)
            }
            is ExerciseSetEvent.DeleteSetsByWorkoutExercise -> {
                deleteSetsByWorkoutExercise(event.workoutExerciseId)
            }
            is ExerciseSetEvent.CreateSet -> {
                createSet()
            }
            is ExerciseSetEvent.CreateMultipleSets -> {
                createMultipleSets()
            }
            is ExerciseSetEvent.UpdateSet -> {
                updateSet()
            }
            is ExerciseSetEvent.DeleteSet -> {
                deleteSet()
            }
            is ExerciseSetEvent.LoadCompletedSets -> {
                loadCompletedSets()
            }
            is ExerciseSetEvent.LoadPendingSets -> {
                loadPendingSets()
            }
            is ExerciseSetEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is ExerciseSetEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }
        }
    }
}
