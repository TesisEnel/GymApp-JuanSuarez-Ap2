package edu.ucne.gymapp.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.repository.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RoutineUiState())
    val state = _state.asStateFlow()

    private fun createRoutine() {
        viewModelScope.launch {
            val currentState = _state.value

            if (!isValidRoutine(currentState)) {
                return@launch
            }

            val routine = Routine(
                name = currentState.name.trim(),
                description = currentState.description.trim(),
                estimatedDuration = currentState.estimatedDuration,
                difficulty = currentState.difficulty,
                targetMuscleGroups = currentState.targetMuscleGroups.trim(),
                isActive = currentState.isActive
            )

            routineRepository.insertRoutine(routine).collect { result ->
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
                                successMessage = "Rutina creada exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear rutina",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateRoutine() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedRoutine = currentState.selectedRoutine

            if (selectedRoutine == null) {
                _state.update {
                    it.copy(errorMessage = "No hay rutina seleccionada para actualizar")
                }
                return@launch
            }

            if (!isValidRoutine(currentState)) {
                return@launch
            }

            val updatedRoutine = selectedRoutine.copy(
                name = currentState.name.trim(),
                description = currentState.description.trim(),
                estimatedDuration = currentState.estimatedDuration,
                difficulty = currentState.difficulty,
                targetMuscleGroups = currentState.targetMuscleGroups.trim(),
                isActive = currentState.isActive
            )

            routineRepository.updateRoutine(updatedRoutine).collect { result ->
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
                                successMessage = "Rutina actualizada exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar rutina",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteRoutine() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedRoutine = currentState.selectedRoutine

            if (selectedRoutine == null) {
                _state.update {
                    it.copy(errorMessage = "No hay rutina seleccionada para eliminar")
                }
                return@launch
            }

            routineRepository.deleteRoutine(selectedRoutine).collect { result ->
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
                                selectedRoutine = null,
                                successMessage = "Rutina eliminada exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al eliminar rutina",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadAllRoutines() {
        viewModelScope.launch {
            routineRepository.getRoutines().collect { result ->
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
                                routines = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadActiveRoutines() {
        viewModelScope.launch {
            routineRepository.getRoutines().collect { result ->
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
                        val activeRoutines = result.data?.filter { it.isActive } ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                activeRoutines = activeRoutines,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas activas"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadRoutinesOrderedByPopularity() {
        viewModelScope.launch {
            routineRepository.getRoutinesOrdered().collect { result ->
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
                                popularRoutines = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas por popularidad"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadRecentRoutines() {
        viewModelScope.launch {
            routineRepository.getRoutines().collect { result ->
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
                        val recentRoutines = result.data?.take(10) ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                recentRoutines = recentRoutines,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas recientes"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadRoutineById(id: Int) {
        viewModelScope.launch {
            routineRepository.getRoutineById(id).collect { result ->
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
                        val routine = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                selectedRoutine = routine,
                                name = routine?.name ?: "",
                                description = routine?.description ?: "",
                                estimatedDuration = routine?.estimatedDuration ?: 30,
                                difficulty = routine?.difficulty ?: "Principiante",
                                targetMuscleGroups = routine?.targetMuscleGroups ?: "",
                                isActive = routine?.isActive ?: false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutina"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadRoutinesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            routineRepository.getRoutinesByDifficulty(difficulty).collect { result ->
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
                                filteredRoutines = result.data ?: emptyList(),
                                selectedDifficulty = difficulty,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar rutinas por dificultad"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun toggleRoutineActive(routineId: Int) {
        viewModelScope.launch {
            routineRepository.activateRoutine(routineId).collect { result ->
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
                                successMessage = "Estado de rutina actualizado",
                                errorMessage = null
                            )
                        }
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cambiar estado de rutina",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun incrementTimesCompleted(routineId: Int) {
        viewModelScope.launch {
            routineRepository.incrementTimesCompleted(routineId).collect { result ->
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
                                timesCompletedUpdated = true,
                                successMessage = "¡Rutina completada!",
                                errorMessage = null
                            )
                        }
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al registrar rutina completada",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun isValidRoutine(state: RoutineUiState): Boolean {
        when {
            state.name.trim().isEmpty() -> {
                _state.update {
                    it.copy(errorMessage = "El nombre de la rutina es obligatorio")
                }
                return false
            }
            state.name.trim().length < 3 -> {
                _state.update {
                    it.copy(errorMessage = "El nombre debe tener al menos 3 caracteres")
                }
                return false
            }
            state.estimatedDuration <= 0 -> {
                _state.update {
                    it.copy(errorMessage = "La duración debe ser mayor a 0 minutos")
                }
                return false
            }
            state.estimatedDuration > 300 -> {
                _state.update {
                    it.copy(errorMessage = "La duración no puede ser mayor a 300 minutos")
                }
                return false
            }
            state.targetMuscleGroups.trim().isEmpty() -> {
                _state.update {
                    it.copy(errorMessage = "Los grupos musculares objetivo son obligatorios")
                }
                return false
            }
        }
        return true
    }

    fun onEvent(event: RoutineEvent) {
        when (event) {
            is RoutineEvent.NameChange -> {
                _state.update { it.copy(name = event.name) }
            }
            is RoutineEvent.DescriptionChange -> {
                _state.update { it.copy(description = event.description) }
            }
            is RoutineEvent.EstimatedDurationChange -> {
                _state.update { it.copy(estimatedDuration = event.estimatedDuration) }
            }
            is RoutineEvent.DifficultyChange -> {
                _state.update { it.copy(difficulty = event.difficulty) }
            }
            is RoutineEvent.TargetMuscleGroupsChange -> {
                _state.update { it.copy(targetMuscleGroups = event.targetMuscleGroups) }
            }
            is RoutineEvent.IsActiveChange -> {
                _state.update { it.copy(isActive = event.isActive) }
            }
            is RoutineEvent.LoadRoutineById -> {
                loadRoutineById(event.id)
            }
            is RoutineEvent.LoadRoutinesByDifficulty -> {
                loadRoutinesByDifficulty(event.difficulty)
            }
            is RoutineEvent.LoadRoutinesByMuscleGroups -> {
                // Implementar filtrado por grupos musculares
                _state.update { it.copy(selectedMuscleGroups = event.muscleGroups) }
                val filtered = _state.value.routines.filter { routine ->
                    routine.targetMuscleGroups.contains(event.muscleGroups, ignoreCase = true)
                }
                _state.update { it.copy(filteredRoutines = filtered) }
            }
            is RoutineEvent.SelectRoutine -> {
                _state.update {
                    it.copy(
                        selectedRoutine = event.routine,
                        name = event.routine.name,
                        description = event.routine.description,
                        estimatedDuration = event.routine.estimatedDuration,
                        difficulty = event.routine.difficulty,
                        targetMuscleGroups = event.routine.targetMuscleGroups,
                        isActive = event.routine.isActive
                    )
                }
            }
            is RoutineEvent.ToggleRoutineActive -> {
                toggleRoutineActive(event.routineId)
            }
            is RoutineEvent.IncrementTimesCompleted -> {
                incrementTimesCompleted(event.routineId)
            }
            is RoutineEvent.CreateRoutine -> {
                createRoutine()
            }
            is RoutineEvent.UpdateRoutine -> {
                updateRoutine()
            }
            is RoutineEvent.DeleteRoutine -> {
                deleteRoutine()
            }
            is RoutineEvent.LoadAllRoutines -> {
                loadAllRoutines()
            }
            is RoutineEvent.LoadActiveRoutines -> {
                loadActiveRoutines()
            }
            is RoutineEvent.LoadRoutinesOrderedByPopularity -> {
                loadRoutinesOrderedByPopularity()
            }
            is RoutineEvent.LoadRecentRoutines -> {
                loadRecentRoutines()
            }
            is RoutineEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is RoutineEvent.ClearMessages -> {
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