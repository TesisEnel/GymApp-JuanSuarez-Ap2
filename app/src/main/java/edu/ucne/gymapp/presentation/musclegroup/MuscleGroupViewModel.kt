package edu.ucne.gymapp.presentation.musclegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.repository.MuscleGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MuscleGroupViewModel @Inject constructor(
    private val muscleGroupRepository: MuscleGroupRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MuscleGroupUiState())
    val state = _state.asStateFlow()

    private fun initializePredefinedMuscleGroups() {
        viewModelScope.launch {
            val predefinedGroups = PredefinedMuscleGroups.getAllMuscleGroups()

            muscleGroupRepository.insertMuscleGroups(predefinedGroups).collect { result ->
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
                                successMessage = "Grupos musculares inicializados"
                            )
                        }
                        loadAllMuscleGroups()
                    }
                    is Resource.Error -> {
                        loadAllMuscleGroups()
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
                        val groups = result.data ?: emptyList()

                        if (groups.isEmpty()) {
                            initializePredefinedMuscleGroups()
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    muscleGroups = groups,
                                    errorMessage = null
                                )
                            }
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

    private fun loadExercisesByMuscleGroup(muscleGroupId: Int) {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroupWithExercises(muscleGroupId).collect { result ->
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
                                errorMessage = result.message ?: "Error al cargar ejercicios del grupo muscular"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: MuscleGroupEvent) {
        when (event) {
            is MuscleGroupEvent.LoadAllMuscleGroups -> {
                loadAllMuscleGroups()
            }
            is MuscleGroupEvent.InitializePredefinedGroups -> {
                initializePredefinedMuscleGroups()
            }
            is MuscleGroupEvent.LoadExercisesByMuscleGroup -> {
                loadExercisesByMuscleGroup(event.muscleGroupId)
            }
            is MuscleGroupEvent.SelectMuscleGroup -> {
                _state.update {
                    it.copy(selectedMuscleGroup = event.muscleGroup)
                }
                loadExercisesByMuscleGroup(event.muscleGroup.muscleGroupId)
            }
            is MuscleGroupEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is MuscleGroupEvent.ClearMessages -> {
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