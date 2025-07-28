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
    val currentState = _state.value
    val currentExercises = _state.value.exercises

    private fun createExercise() {
        viewModelScope.launch {
            if (!isValidExerciseData(currentState)) {
                return@launch
            }

            val exercise = Exercise(
                exerciseId = 0,
                name = currentState.name.trim(),
                description = currentState.description.trim(),
                instructions = currentState.instructions.trim(),
                muscleGroupId = currentState.muscleGroupId,
                difficulty = currentState.difficulty,
                videoUrl = currentState.videoUrl?.trim(),
                thumbnailUrl = currentState.thumbnailUrl?.trim(),
                videoDuration = currentState.videoDuration,
                isVideoAvailable = currentState.isVideoAvailable,
                equipment = currentState.equipment?.trim(),
                tips = currentState.tips?.trim(),
                commonMistakes = currentState.commonMistakes?.trim(),
                popularity = 0
            )

            exerciseRepository.insertExercise(exercise).collect { result ->
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
                                successMessage = "Ejercicio creado exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllExercises()
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

    private fun updateExercise() {
        viewModelScope.launch {
            if (!isValidExerciseData(currentState)) {
                return@launch
            }

            val selectedExercise = currentState.selectedExercise ?: run {
                _state.update {
                    it.copy(
                        errorMessage = "No hay ejercicio seleccionado para actualizar"
                    )
                }
                return@launch
            }

            val updatedExercise = selectedExercise.copy(
                name = currentState.name.trim(),
                description = currentState.description.trim(),
                instructions = currentState.instructions.trim(),
                muscleGroupId = currentState.muscleGroupId,
                difficulty = currentState.difficulty,
                videoUrl = currentState.videoUrl?.trim(),
                thumbnailUrl = currentState.thumbnailUrl?.trim(),
                videoDuration = currentState.videoDuration,
                isVideoAvailable = currentState.isVideoAvailable,
                equipment = currentState.equipment?.trim(),
                tips = currentState.tips?.trim(),
                commonMistakes = currentState.commonMistakes?.trim()
            )

            exerciseRepository.updateExercise(updatedExercise).collect { result ->
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
                        loadAllExercises()
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

    private fun deleteExercise() {
        viewModelScope.launch {
            val selectedExercise = _state.value.selectedExercise ?: run {
                _state.update {
                    it.copy(
                        errorMessage = "No hay ejercicio seleccionado para eliminar"
                    )
                }
                return@launch
            }

            exerciseRepository.deleteExercise(selectedExercise).collect { result ->
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
                                selectedExercise = null,
                                successMessage = "Ejercicio eliminado exitosamente",
                                errorMessage = null
                            )
                        }
                        loadAllExercises()
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

    private fun loadAllExercises() {
        viewModelScope.launch {
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
                        _state.update {
                            it.copy(
                                isLoading = false,
                                exercises = result.data ?: emptyList(),
                                filteredExercises = result.data ?: emptyList(),
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
        }
    }

    private fun loadExerciseById(id: Int) {
        viewModelScope.launch {
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
                                name = exercise?.name ?: "",
                                description = exercise?.description ?: "",
                                instructions = exercise?.instructions ?: "",
                                muscleGroupId = exercise?.muscleGroupId ?: 0,
                                difficulty = exercise?.difficulty ?: "Principiante",
                                videoUrl = exercise?.videoUrl,
                                thumbnailUrl = exercise?.thumbnailUrl,
                                videoDuration = exercise?.videoDuration,
                                isVideoAvailable = exercise?.isVideoAvailable ?: false,
                                equipment = exercise?.equipment,
                                tips = exercise?.tips,
                                commonMistakes = exercise?.commonMistakes,
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
        }
    }

    private fun loadExercisesByMuscleGroup(muscleGroupId: Int) {
        viewModelScope.launch {
            exerciseRepository.getExercisesByMuscleGroup(muscleGroupId).collect { result ->
                handleExercisesResult(result)
            }
        }
    }

    private fun loadExercisesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            exerciseRepository.getExercisesByDifficulty(difficulty).collect { result ->
                handleExercisesResult(result)
            }
        }
    }

    private fun loadExercisesByPopularity() {
        viewModelScope.launch {
            exerciseRepository.getExercisesByPopularity().collect { result ->
                handleExercisesResult(result)
            }
        }
    }

    private fun loadExercisesByMuscleGroupsAndDifficulties(
        muscleGroupIds: List<Int>,
        difficulties: List<String>
    ) {
        viewModelScope.launch {
            exerciseRepository.getExercisesByMuscleGroupsAndDifficulties(
                muscleGroupIds, difficulties
            ).collect { result ->
                handleExercisesResult(result)
            }
        }
    }

    private fun incrementPopularity(exerciseId: Int) {
        viewModelScope.launch {
            exerciseRepository.incrementExercisePopularity(exerciseId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Opcional: mostrar loading si es necesario
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
        }
    }

    private fun filterByDifficulties(difficulties: List<String>) {
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
                _state.update {
                    it.copy(
                        isLoading = false,
                        exercises = result.data ?: emptyList(),
                        filteredExercises = result.data ?: emptyList(),
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

    private fun isValidExerciseData(state: ExerciseUiState): Boolean {
        if (state.name.isBlank()) {
            _state.update {
                it.copy(
                    errorMessage = "El nombre del ejercicio es obligatorio"
                )
            }
            return false
        }

        if (state.description.isBlank()) {
            _state.update {
                it.copy(
                    errorMessage = "La descripción del ejercicio es obligatoria"
                )
            }
            return false
        }

        if (state.instructions.isBlank()) {
            _state.update {
                it.copy(
                    errorMessage = "Las instrucciones del ejercicio son obligatorias"
                )
            }
            return false
        }

        if (state.muscleGroupId <= 0) {
            _state.update {
                it.copy(
                    errorMessage = "Debe seleccionar un grupo muscular válido"
                )
            }
            return false
        }

        if (state.difficulty.isBlank()) {
            _state.update {
                it.copy(
                    errorMessage = "Debe especificar la dificultad del ejercicio"
                )
            }
            return false
        }

        return true
    }

    private fun clearFormData() {
        _state.update {
            it.copy(
                name = "",
                description = "",
                instructions = "",
                muscleGroupId = 0,
                difficulty = "Principiante",
                videoUrl = null,
                thumbnailUrl = null,
                videoDuration = null,
                isVideoAvailable = false,
                equipment = null,
                tips = null,
                commonMistakes = null,
                selectedExercise = null,
                isCreated = false,
                isUpdated = false,
                isDeleted = false
            )
        }
    }

    fun onEvent(event: ExerciseEvent) {
        when (event) {
            is ExerciseEvent.NameChange -> {
                _state.update { it.copy(name = event.name) }
            }
            is ExerciseEvent.DescriptionChange -> {
                _state.update { it.copy(description = event.description) }
            }
            is ExerciseEvent.InstructionsChange -> {
                _state.update { it.copy(instructions = event.instructions) }
            }
            is ExerciseEvent.MuscleGroupChange -> {
                _state.update { it.copy(muscleGroupId = event.muscleGroupId) }
            }
            is ExerciseEvent.DifficultyChange -> {
                _state.update { it.copy(difficulty = event.difficulty) }
            }
            is ExerciseEvent.VideoUrlChange -> {
                _state.update { it.copy(videoUrl = event.videoUrl) }
            }
            is ExerciseEvent.ThumbnailUrlChange -> {
                _state.update { it.copy(thumbnailUrl = event.thumbnailUrl) }
            }
            is ExerciseEvent.VideoDurationChange -> {
                _state.update { it.copy(videoDuration = event.videoDuration) }
            }
            is ExerciseEvent.IsVideoAvailableChange -> {
                _state.update { it.copy(isVideoAvailable = event.isVideoAvailable) }
            }
            is ExerciseEvent.EquipmentChange -> {
                _state.update { it.copy(equipment = event.equipment) }
            }
            is ExerciseEvent.TipsChange -> {
                _state.update { it.copy(tips = event.tips) }
            }
            is ExerciseEvent.CommonMistakesChange -> {
                _state.update { it.copy(commonMistakes = event.commonMistakes) }
            }
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
            is ExerciseEvent.CreateExercise -> {
                createExercise()
            }
            is ExerciseEvent.UpdateExercise -> {
                updateExercise()
            }
            is ExerciseEvent.DeleteExercise -> {
                deleteExercise()
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
        }
    }
}
