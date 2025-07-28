package edu.ucne.gymapp.presentation.musclegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.repository.MuscleGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MuscleGroupViewModel @Inject constructor(
    private val muscleGroupRepository: MuscleGroupRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MuscleGroupUiState())
    val state = _state.asStateFlow()

    private fun createMuscleGroup() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.name.trim().isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El nombre del grupo muscular es requerido"
                    )
                }
                return@launch
            }

            val muscleGroup = MuscleGroup(
                name = currentState.name.trim(),
                description = currentState.description.trim(),
                iconResource = currentState.iconResource
            )

            muscleGroupRepository.insertMuscleGroup(muscleGroup).collect { result ->
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
                                successMessage = "Grupo muscular creado exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllMuscleGroups()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear el grupo muscular",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createMultipleMuscleGroups() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.multipleMuscleGroups.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay grupos musculares para crear"
                    )
                }
                return@launch
            }

            muscleGroupRepository.insertMuscleGroups(currentState.multipleMuscleGroups).collect { result ->
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
                                successMessage = "Grupos musculares creados exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllMuscleGroups()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al crear los grupos musculares",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateMuscleGroup() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.selectedMuscleGroup == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay grupo muscular seleccionado para actualizar"
                    )
                }
                return@launch
            }

            if (currentState.name.trim().isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "El nombre del grupo muscular es requerido"
                    )
                }
                return@launch
            }

            val updatedMuscleGroup = currentState.selectedMuscleGroup.copy(
                name = currentState.name.trim(),
                description = currentState.description.trim(),
                iconResource = currentState.iconResource
            )

            muscleGroupRepository.updateMuscleGroup(updatedMuscleGroup).collect { result ->
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
                                successMessage = "Grupo muscular actualizado exitosamente",
                                errorMessage = null,
                                selectedMuscleGroup = updatedMuscleGroup
                            )
                        }
                        // Recargar lista después de actualizar
                        loadAllMuscleGroups()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al actualizar el grupo muscular",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteMuscleGroup() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.selectedMuscleGroup == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No hay grupo muscular seleccionado para eliminar"
                    )
                }
                return@launch
            }

            muscleGroupRepository.deleteMuscleGroup(currentState.selectedMuscleGroup).collect { result ->
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
                                successMessage = "Grupo muscular eliminado exitosamente",
                                errorMessage = null,
                                selectedMuscleGroup = null
                            )
                        }
                        loadAllMuscleGroups()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al eliminar el grupo muscular",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadAllMuscleGroups() {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroups().collect { result ->
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
                                muscleGroups = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar los grupos musculares"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadMuscleGroupsOrdered() {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroupsOrdered().collect { result ->
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
                                orderedMuscleGroups = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar los grupos musculares ordenados"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadMuscleGroupById(id: Int) {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroupById(id).collect { result ->
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
                                selectedMuscleGroup = result.data,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar el grupo muscular"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadMuscleGroupsByIds(ids: List<Int>) {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroupsByIds(ids).collect { result ->
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
                                selectedMuscleGroups = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar los grupos musculares por IDs"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadMuscleGroupWithExercises(id: Int) {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroupWithExercises(id).collect { result ->
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
                                muscleGroupWithExercises = result.data,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar el grupo muscular con ejercicios"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadAllMuscleGroupsWithExercises() {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroupsWithExercises().collect { result ->
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
                                muscleGroupsWithExercises = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al cargar los grupos musculares con ejercicios"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: MuscleGroupEvent) {
        when (event) {
            is MuscleGroupEvent.NameChange -> {
                _state.update { it.copy(name = event.name) }
            }
            is MuscleGroupEvent.DescriptionChange -> {
                _state.update { it.copy(description = event.description) }
            }
            is MuscleGroupEvent.IconResourceChange -> {
                _state.update { it.copy(iconResource = event.iconResource) }
            }
            is MuscleGroupEvent.LoadMuscleGroupById -> {
                loadMuscleGroupById(event.id)
            }
            is MuscleGroupEvent.LoadMuscleGroupsByIds -> {
                loadMuscleGroupsByIds(event.ids)
            }
            is MuscleGroupEvent.LoadMuscleGroupWithExercises -> {
                loadMuscleGroupWithExercises(event.id)
            }
            is MuscleGroupEvent.SelectMuscleGroup -> {
                _state.update {
                    it.copy(
                        selectedMuscleGroup = event.muscleGroup,
                        name = event.muscleGroup.name,
                        description = event.muscleGroup.description,
                        iconResource = event.muscleGroup.iconResource
                    )
                }
            }
            is MuscleGroupEvent.SelectMuscleGroups -> {
                _state.update { it.copy(selectedMuscleGroups = event.muscleGroups) }
            }
            is MuscleGroupEvent.CreateMuscleGroup -> {
                createMuscleGroup()
            }
            is MuscleGroupEvent.CreateMultipleMuscleGroups -> {
                createMultipleMuscleGroups()
            }
            is MuscleGroupEvent.UpdateMuscleGroup -> {
                updateMuscleGroup()
            }
            is MuscleGroupEvent.DeleteMuscleGroup -> {
                deleteMuscleGroup()
            }
            is MuscleGroupEvent.LoadAllMuscleGroups -> {
                loadAllMuscleGroups()
            }
            is MuscleGroupEvent.LoadMuscleGroupsOrdered -> {
                loadMuscleGroupsOrdered()
            }
            is MuscleGroupEvent.LoadAllMuscleGroupsWithExercises -> {
                loadAllMuscleGroupsWithExercises()
            }
            is MuscleGroupEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is MuscleGroupEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null,
                        isCreated = false,
                        isUpdated = false,
                        isDeleted = false
                    )
                }
            }
        }
    }
}