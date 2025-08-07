package edu.ucne.gymapp.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.RoutineExercise
import edu.ucne.gymapp.data.repository.RoutineRepository
import edu.ucne.gymapp.data.repository.MuscleGroupRepository
import edu.ucne.gymapp.data.repository.ExerciseRepository
import edu.ucne.gymapp.data.repository.RoutineExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val muscleGroupRepository: MuscleGroupRepository,
    private val exerciseRepository: ExerciseRepository,
    private val routineExerciseRepository: RoutineExerciseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RoutineUiState())
    val state = _state.asStateFlow()

    init {
        loadAllRoutines()
    }

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
                targetMuscleGroups = currentState.selectedMuscleGroups.joinToString(", ") { it.name }.ifEmpty { currentState.targetMuscleGroups.trim() },
                isActive = currentState.isActive
            )

            routineRepository.insertRoutine(routine).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(isLoading = true, errorMessage = null, successMessage = null)
                        }
                    }
                    is Resource.Success -> {
                        val routineId = result.data

                        if (currentState.selectedExercises.isNotEmpty() && routineId != null) {
                            createRoutineExercises(routineId.toInt(), currentState.selectedExercises)
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isCreated = true,
                                    successMessage = "Rutina creada exitosamente",
                                    errorMessage = null
                                )
                            }
                            clearFormData()
                            loadAllRoutines()
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al crear rutina",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadExercisesForRoutine(routineId: Int) {
        viewModelScope.launch {
            try {
                routineExerciseRepository.getRoutineExercises(routineId).collect { routineExerciseResult ->
                    when (routineExerciseResult) {
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true, errorMessage = null) }
                        }
                        is Resource.Success -> {
                            val routineExercises = routineExerciseResult.data ?: emptyList()

                            if (routineExercises.isNotEmpty()) {
                                val exerciseIds = routineExercises.map { it.exerciseId }
                                exerciseRepository.getExercisesByIds(exerciseIds).collect { exerciseResult ->
                                    when (exerciseResult) {
                                        is Resource.Success -> {
                                            val exercises = exerciseResult.data ?: emptyList()
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    routineExercises = routineExercises,
                                                    selectedExercises = exercises,
                                                    errorMessage = null
                                                )
                                            }
                                        }
                                        is Resource.Error -> {
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    routineExercises = routineExercises,
                                                    errorMessage = exerciseResult.message ?: "Error al cargar ejercicios"
                                                )
                                            }
                                        }
                                        is Resource.Loading -> { }
                                    }
                                }
                            } else {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        routineExercises = emptyList(),
                                        selectedExercises = emptyList(),
                                        errorMessage = null
                                    )
                                }
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = routineExerciseResult.message ?: "Error al cargar ejercicios de la rutina"
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

    private fun createRoutineExercises(routineId: Int, exercises: List<Exercise>) {
        viewModelScope.launch {
            try {
                val routineExercises = exercises.mapIndexed { index, exercise ->
                    RoutineExercise(
                        routineId = routineId,
                        exerciseId = exercise.exerciseId,
                        order = index + 1,
                        sets = 3,
                        reps = "10",
                        weight = null,
                        restTime = 90,
                        notes = null
                    )
                }

                routineExerciseRepository.insertRoutineExercises(routineExercises).collect { result ->
                    when (result) {
                        is Resource.Loading -> { }
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isCreated = true,
                                    successMessage = "Rutina creada con ${exercises.size} ejercicios",
                                    errorMessage = null
                                )
                            }
                            clearFormData()
                            loadAllRoutines()
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "Rutina creada pero error al agregar ejercicios: ${result.message}",
                                    successMessage = null
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado al crear ejercicios: ${e.message}"
                    )
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
                isActive = currentState.isActive,
                lastModified = System.currentTimeMillis()
            )

            routineRepository.updateRoutine(updatedRoutine).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(isLoading = true, errorMessage = null, successMessage = null)
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
                        clearFormData()
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al actualizar rutina",
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
            val selectedRoutine = _state.value.selectedRoutine

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
                            it.copy(isLoading = true, errorMessage = null, successMessage = null)
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
                        clearFormData()
                        loadAllRoutines()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al eliminar rutina",
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
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
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
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val activeRoutines = result.data?.filter { it.isActive } ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                routines = activeRoutines,
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

    private fun loadRoutinesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            routineRepository.getRoutinesByDifficulty(difficulty).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                filteredRoutines = result.data ?: emptyList(),
                                routines = result.data ?: emptyList(),
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
                            it.copy(isLoading = true, errorMessage = null, successMessage = null)
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
                            it.copy(isLoading = true, errorMessage = null, successMessage = null)
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

    private fun loadMuscleGroups() {
        viewModelScope.launch {
            muscleGroupRepository.getMuscleGroups().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val muscleGroups = result.data ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                muscleGroups = muscleGroups,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error al cargar grupos musculares"
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
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
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
                                errorMessage = result.message ?: "Error al cargar ejercicios"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadExercisesByMuscleGroups(muscleGroupIds: List<Int>) {
        viewModelScope.launch {
            exerciseRepository.getExercisesByMuscleGroups(muscleGroupIds).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
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
                                errorMessage = result.message ?: "Error al cargar ejercicios"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun searchExercises(query: String) {
        val currentExercises = _state.value.exercises
        val filtered = if (query.isBlank()) {
            currentExercises
        } else {
            currentExercises.filter { exercise ->
                exercise.name.contains(query, ignoreCase = true) ||
                        exercise.description.contains(query, ignoreCase = true)
            }
        }

        _state.update {
            it.copy(
                exerciseSearchQuery = query,
                filteredExercises = filtered
            )
        }
    }

    private fun selectExercise(exercise: Exercise) {
        val currentSelected = _state.value.selectedExercises.toMutableList()

        if (currentSelected.any { it.exerciseId == exercise.exerciseId }) {
            currentSelected.removeAll { it.exerciseId == exercise.exerciseId }
        } else {
            currentSelected.add(exercise)
        }

        _state.update {
            it.copy(selectedExercises = currentSelected)
        }
    }

    private fun removeExercise(exercise: Exercise) {
        val currentSelected = _state.value.selectedExercises.toMutableList()
        currentSelected.removeAll { it.exerciseId == exercise.exerciseId }

        _state.update {
            it.copy(selectedExercises = currentSelected)
        }
    }

    private fun changeStep(step: RoutineCreationStep) {
        _state.update { it.copy(currentStep = step) }
    }

    private fun nextStep() {
        val currentStep = _state.value.currentStep
        val nextStep = when (currentStep) {
            RoutineCreationStep.BASIC_INFO -> RoutineCreationStep.MUSCLE_GROUP
            RoutineCreationStep.MUSCLE_GROUP -> RoutineCreationStep.EXERCISE
            RoutineCreationStep.EXERCISE -> RoutineCreationStep.EXERCISE_CONFIG
            RoutineCreationStep.EXERCISE_CONFIG -> RoutineCreationStep.REVIEW
            RoutineCreationStep.REVIEW -> RoutineCreationStep.REVIEW
        }

        _state.update { it.copy(currentStep = nextStep) }
    }

    private fun previousStep() {
        val currentStep = _state.value.currentStep
        val previousStep = when (currentStep) {
            RoutineCreationStep.BASIC_INFO -> RoutineCreationStep.BASIC_INFO
            RoutineCreationStep.MUSCLE_GROUP -> RoutineCreationStep.BASIC_INFO
            RoutineCreationStep.EXERCISE -> RoutineCreationStep.MUSCLE_GROUP
            RoutineCreationStep.EXERCISE_CONFIG -> RoutineCreationStep.EXERCISE
            RoutineCreationStep.REVIEW -> RoutineCreationStep.EXERCISE_CONFIG
        }

        _state.update { it.copy(currentStep = previousStep) }
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
        }
        return true
    }

    private fun clearFormData() {
        _state.update {
            it.copy(
                name = "",
                description = "",
                estimatedDuration = 30,
                difficulty = "Principiante",
                targetMuscleGroups = "",
                isActive = false,
                selectedRoutine = null,
                selectedMuscleGroups = emptyList(),
                selectedExercises = emptyList(),
                exercises = emptyList(),
                filteredExercises = emptyList(),
                exerciseSearchQuery = "",
                currentStep = RoutineCreationStep.BASIC_INFO,
                showExerciseSelection = false,
                showRoutineDetails = false,
                routineToView = null,
                isCreated = false,
                isUpdated = false,
                isDeleted = false,
                timesCompletedUpdated = false
            )
        }
    }

    fun getRoutineExerciseDetails(exerciseId: Int): RoutineExercise? {
        return _state.value.routineExercises.find { it.exerciseId == exerciseId }
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
            is RoutineEvent.LoadRoutinesByDifficulty -> {
                loadRoutinesByDifficulty(event.difficulty)
            }
            is RoutineEvent.LoadRoutinesByMuscleGroups -> {
                val filtered = _state.value.routines.filter { routine ->
                    routine.targetMuscleGroups.contains(event.muscleGroups, ignoreCase = true)
                }
                _state.update { it.copy(filteredRoutines = filtered, routines = filtered) }
            }
            is RoutineEvent.SelectRoutine -> {
                if (event.routine.routineId == 0) {
                    clearFormData()
                } else {
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
                    loadExercisesForRoutine(event.routine.routineId)
                }
            }
            is RoutineEvent.ViewRoutine -> {
                _state.update {
                    it.copy(
                        showRoutineDetails = true,
                        routineToView = event.routine
                    )
                }
                loadExercisesForRoutine(event.routine.routineId)
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
            is RoutineEvent.LoadMuscleGroups -> {
                loadMuscleGroups()
            }
            is RoutineEvent.SelectMuscleGroup -> {
                val currentSelected = _state.value.selectedMuscleGroups.toMutableList()
                if (currentSelected.any { it.muscleGroupId == event.muscleGroup.muscleGroupId }) {
                    currentSelected.removeAll { it.muscleGroupId == event.muscleGroup.muscleGroupId }
                } else {
                    currentSelected.add(event.muscleGroup)
                }
                _state.update { it.copy(selectedMuscleGroups = currentSelected) }

                if (currentSelected.isNotEmpty()) {
                    val muscleGroupIds = currentSelected.map { it.muscleGroupId }
                    loadExercisesByMuscleGroups(muscleGroupIds)
                } else {
                    _state.update {
                        it.copy(
                            exercises = emptyList(),
                            filteredExercises = emptyList()
                        )
                    }
                }
            }
            is RoutineEvent.LoadExercisesByMuscleGroup -> {
                loadExercisesByMuscleGroup(event.muscleGroupId)
            }
            is RoutineEvent.SearchExercises -> {
                searchExercises(event.query)
            }
            is RoutineEvent.SelectExercise -> {
                selectExercise(event.exercise)
            }
            is RoutineEvent.RemoveExercise -> {
                removeExercise(event.exercise)
            }
            is RoutineEvent.ChangeStep -> {
                changeStep(event.step)
            }
            is RoutineEvent.NextStep -> {
                nextStep()
            }
            is RoutineEvent.PreviousStep -> {
                previousStep()
            }
            is RoutineEvent.ShowExerciseSelection -> {
                _state.update {
                    it.copy(
                        showExerciseSelection = true,
                        currentStep = RoutineCreationStep.MUSCLE_GROUP
                    )
                }
                loadMuscleGroups()
            }
            is RoutineEvent.HideExerciseSelection -> {
                _state.update {
                    it.copy(
                        showExerciseSelection = false,
                        currentStep = RoutineCreationStep.BASIC_INFO,
                        selectedMuscleGroups = emptyList(),
                        selectedExercises = emptyList(),
                        exercises = emptyList(),
                        filteredExercises = emptyList(),
                        exerciseSearchQuery = "",
                        showRoutineDetails = false,
                        routineToView = null
                    )
                }
            }
            is RoutineEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is RoutineEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null,
                        showRoutineDetails = false,
                        routineToView = null
                    )
                }
            }
        }
    }
}