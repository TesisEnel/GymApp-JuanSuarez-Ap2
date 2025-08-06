package edu.ucne.gymapp.presentation.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExerciseUiState())
    val state = _state.asStateFlow()

    init {
        loadAllExercises()
    }

    private fun loadAllExercises() {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercises().collect { result ->
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
                            val exercises = result.data ?: emptyList()
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    exercises = exercises,
                                    filteredExercises = exercises,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "Error desconocido al cargar ejercicios"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    fun insertPredefinedExercises(exercises: List<Exercise>) {
        viewModelScope.launch {
            try {
                exerciseRepository.insertExercisesSynchronously(exercises).collect { result ->
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
                                    successMessage = "Ejercicios predefinidos insertados exitosamente"
                                )
                            }
                            loadAllExercises()
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "Error insertando ejercicios predefinidos: ${result.message}"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado insertando ejercicios: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadExerciseById(id: Int) {
        viewModelScope.launch {
            try {
                exerciseRepository.getExerciseById(id).collect { result ->
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
                            val exercise = result.data
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    selectedExercise = exercise,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "Error desconocido al cargar ejercicio"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadExercisesByMuscleGroup(muscleGroupId: Int) {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByMuscleGroup(muscleGroupId).collect { result ->
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadExercisesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByDifficulty(difficulty).collect { result ->
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadExercisesByPopularity() {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByPopularity().collect { result ->
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadExercisesByMuscleGroupsAndDifficulties(
        muscleGroupIds: List<Int>,
        difficulties: List<String>
    ) {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByMuscleGroupsAndDifficulties(
                    muscleGroupIds, difficulties
                ).collect { result ->
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun incrementPopularity(exerciseId: Int) {
        viewModelScope.launch {
            try {
                exerciseRepository.incrementExercisePopularity(exerciseId).collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            loadAllExercises()
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    errorMessage = result.message ?: "Error al actualizar popularidad"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun filterByDifficulties(difficulties: List<String>) {
        val currentExercises = _state.value.exercises

        val filtered = if (difficulties.isEmpty()) {
            currentExercises
        } else {
            currentExercises.filter { it.difficulty in difficulties }
        }

        _state.update {
            it.copy(
                filteredExercises = filtered,
                selectedDifficulties = difficulties
            )
        }
    }

    private fun filterByMuscleGroups(muscleGroupIds: List<Int>) {
        val currentExercises = _state.value.exercises

        val filtered = if (muscleGroupIds.isEmpty()) {
            currentExercises
        } else {
            currentExercises.filter { it.muscleGroupId in muscleGroupIds }
        }

        _state.update {
            it.copy(
                filteredExercises = filtered,
                selectedMuscleGroups = muscleGroupIds
            )
        }
    }

    private fun handleExercisesResult(result: Resource<List<Exercise>>) {
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
                val exercises = result.data ?: emptyList()
                _state.update {
                    it.copy(
                        isLoading = false,
                        exercises = exercises,
                        filteredExercises = exercises,
                        errorMessage = null
                    )
                }
            }
            is Resource.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Error desconocido al cargar ejercicios"
                    )
                }
            }
        }
    }


    fun verifyDatabaseState() {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercises().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                        }
                        else -> {
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onEvent(event: ExerciseEvent) {
        when (event) {
            is ExerciseEvent.LoadExerciseById -> {
                loadExerciseById(event.id)
            }
            is ExerciseEvent.LoadExercisesByMuscleGroup -> {
                loadExercisesByMuscleGroup(event.muscleGroupId)
            }
            is ExerciseEvent.LoadExercisesByDifficulty -> {
                loadExercisesByDifficulty(event.difficulty)
            }
            is ExerciseEvent.LoadExercisesByMuscleGroupsAndDifficulties -> {
                loadExercisesByMuscleGroupsAndDifficulties(
                    event.muscleGroupIds,
                    event.difficulties
                )
            }
            is ExerciseEvent.IncrementPopularity -> {
                incrementPopularity(event.exerciseId)
            }
            is ExerciseEvent.FilterByDifficulties -> {
                filterByDifficulties(event.difficulties)
            }
            is ExerciseEvent.FilterByMuscleGroups -> {
                filterByMuscleGroups(event.muscleGroupIds)
            }
            is ExerciseEvent.LoadExercisesByPopularity -> {
                loadExercisesByPopularity()
            }
            is ExerciseEvent.InsertPredefinedExercises -> {
                insertPredefinedExercises(event.exercises)
            }
            is ExerciseEvent.LoadAllExercises -> {
                loadAllExercises()
            }
            is ExerciseEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is ExerciseEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }
            else -> {
            }
        }
    }
}