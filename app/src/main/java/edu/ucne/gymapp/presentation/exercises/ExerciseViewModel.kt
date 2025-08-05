package edu.ucne.gymapp.presentation.exercises

import android.util.Log
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

    companion object {
        private const val TAG = "ExerciseViewModel"
    }

    private val _state = MutableStateFlow(ExerciseUiState())
    val state = _state.asStateFlow()


    init {
        loadAllExercises()
    }

    private fun initializePredefinedExercises() {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercises().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val exercises = result.data ?: emptyList()
                            val predefinedExercises = exercises.filter { isPredefinedExercise(it.exerciseId) }

                            if (predefinedExercises.isEmpty()) {
                                insertPredefinedExercises(PredefinedExercises.getAll())
                            }
                        }
                        else -> {
                            Log.d(TAG, "initializePredefinedExercises - No se pudieron verificar ejercicios existentes")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "initializePredefinedExercises - Error: ${e.message}")
            }
        }
    }
    private fun isPredefinedExercise(exerciseId: Int): Boolean {
        val isPredefined = exerciseId <= 100 && exerciseId > 0
        Log.d(TAG, "isPredefinedExercise - ID: $exerciseId, isPredefined: $isPredefined")
        return isPredefined
    }

    private fun createExercise() {
        Log.d(TAG, "createExercise - Iniciando creación de ejercicio")
        viewModelScope.launch {
            val currentState = _state.value
            if (!isValidExerciseData(currentState)) {
                Log.w(TAG, "createExercise - Datos de ejercicio no válidos")
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

            Log.d(TAG, "createExercise - Ejercicio a crear: ${exercise.name}")

            exerciseRepository.insertExercise(exercise).collect { result ->
                Log.d(TAG, "createExercise - Resultado del repositorio: ${result.javaClass.simpleName}")
                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "createExercise - Loading...")
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "createExercise - Success! Recargando ejercicios...")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isCreated = true,
                                successMessage = "Ejercicio creado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearFormData()
                        loadAllExercises()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "createExercise - Error: ${result.message}")
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
        Log.d(TAG, "updateExercise - Iniciando actualización de ejercicio")
        viewModelScope.launch {
            val currentState = _state.value
            if (!isValidExerciseData(currentState)) {
                Log.w(TAG, "updateExercise - Datos de ejercicio no válidos")
                return@launch
            }

            val selectedExercise = currentState.selectedExercise ?: run {
                Log.e(TAG, "updateExercise - No hay ejercicio seleccionado")
                _state.update {
                    it.copy(errorMessage = "No hay ejercicio seleccionado para actualizar")
                }
                return@launch
            }

            Log.d(TAG, "updateExercise - Ejercicio seleccionado: ${selectedExercise.name} (ID: ${selectedExercise.exerciseId})")

            if (isPredefinedExercise(selectedExercise.exerciseId)) {
                Log.w(TAG, "updateExercise - Intento de editar ejercicio predefinido")
                _state.update {
                    it.copy(errorMessage = "No se pueden editar ejercicios predefinidos del sistema")
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

            Log.d(TAG, "updateExercise - Ejercicio actualizado: ${updatedExercise.name}")

            exerciseRepository.updateExercise(updatedExercise).collect { result ->
                Log.d(TAG, "updateExercise - Resultado del repositorio: ${result.javaClass.simpleName}")
                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "updateExercise - Loading...")
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "updateExercise - Success! Recargando ejercicios...")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isUpdated = true,
                                successMessage = "Ejercicio actualizado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearFormData()
                        loadAllExercises()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "updateExercise - Error: ${result.message}")
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
        Log.d(TAG, "deleteExercise - Iniciando eliminación de ejercicio")
        viewModelScope.launch {
            val selectedExercise = _state.value.selectedExercise ?: run {
                Log.e(TAG, "deleteExercise - No hay ejercicio seleccionado")
                _state.update {
                    it.copy(
                        errorMessage = "No hay ejercicio seleccionado para eliminar"
                    )
                }
                return@launch
            }

            Log.d(TAG, "deleteExercise - Ejercicio a eliminar: ${selectedExercise.name} (ID: ${selectedExercise.exerciseId})")

            if (isPredefinedExercise(selectedExercise.exerciseId)) {
                Log.w(TAG, "deleteExercise - Intento de eliminar ejercicio predefinido")
                _state.update {
                    it.copy(
                        errorMessage = "No se pueden eliminar ejercicios predefinidos del sistema"
                    )
                }
                return@launch
            }

            exerciseRepository.deleteExercise(selectedExercise).collect { result ->
                Log.d(TAG, "deleteExercise - Resultado del repositorio: ${result.javaClass.simpleName}")
                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "deleteExercise - Loading...")
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "deleteExercise - Success! Recargando ejercicios...")
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
                        Log.e(TAG, "deleteExercise - Error: ${result.message}")
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

    private fun canEditOrDelete(exercise: Exercise): Boolean {
        val canEdit = !isPredefinedExercise(exercise.exerciseId)
        Log.d(TAG, "canEditOrDelete - Ejercicio: ${exercise.name}, canEdit: $canEdit")
        return canEdit
    }

    private fun loadAllExercises() {
        Log.d(TAG, "loadAllExercises - Iniciando carga de todos los ejercicios")
        viewModelScope.launch {
            try {
                exerciseRepository.getExercises().collect { result ->
                    Log.d(TAG, "loadAllExercises - Resultado del repositorio: ${result.javaClass.simpleName}")
                    when (result) {
                        is Resource.Loading -> {
                            Log.d(TAG, "loadAllExercises - Loading...")
                            _state.update {
                                it.copy(
                                    isLoading = true,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Success -> {
                            val exercises = result.data ?: emptyList()
                            Log.d(TAG, "loadAllExercises - Success! Ejercicios cargados: ${exercises.size}")
                            exercises.forEachIndexed { index, exercise ->
                                Log.d(TAG, "loadAllExercises - Ejercicio $index: ${exercise.name} (ID: ${exercise.exerciseId})")
                            }
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
                            Log.e(TAG, "loadAllExercises - Error: ${result.message}")
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
                Log.e(TAG, "loadAllExercises - Excepción: ${e.message}", e)
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
        Log.d(TAG, "insertPredefinedExercises - Iniciando inserción de ${exercises.size} ejercicios predefinidos")
        viewModelScope.launch {
            try {
                exerciseRepository.insertExercisesSynchronously(exercises).collect { result ->
                    Log.d(TAG, "insertPredefinedExercises - Resultado: ${result.javaClass.simpleName}")
                    when (result) {
                        is Resource.Loading -> {
                            Log.d(TAG, "insertPredefinedExercises - Loading...")
                            _state.update {
                                it.copy(
                                    isLoading = true,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Success -> {
                            Log.d(TAG, "insertPredefinedExercises - Success! Recargando ejercicios...")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    successMessage = "Ejercicios predefinidos insertados exitosamente"
                                )
                            }
                            loadAllExercises()
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "insertPredefinedExercises - Error: ${result.message}")
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
                Log.e(TAG, "insertPredefinedExercises - Excepción: ${e.message}", e)
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
        Log.d(TAG, "loadExerciseById - Cargando ejercicio con ID: $id")
        viewModelScope.launch {
            try {
                exerciseRepository.getExerciseById(id).collect { result ->
                    Log.d(TAG, "loadExerciseById - Resultado del repositorio: ${result.javaClass.simpleName}")
                    when (result) {
                        is Resource.Loading -> {
                            Log.d(TAG, "loadExerciseById - Loading...")
                            _state.update {
                                it.copy(
                                    isLoading = true,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Success -> {
                            val exercise = result.data
                            Log.d(TAG, "loadExerciseById - Success! Ejercicio: ${exercise?.name ?: "null"}")
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
                            Log.e(TAG, "loadExerciseById - Error: ${result.message}")
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
                Log.e(TAG, "loadExerciseById - Excepción: ${e.message}", e)
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
        Log.d(TAG, "loadExercisesByMuscleGroup - Cargando ejercicios para grupo muscular: $muscleGroupId")
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByMuscleGroup(muscleGroupId).collect { result ->
                    Log.d(TAG, "loadExercisesByMuscleGroup - Resultado: ${result.javaClass.simpleName}")
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadExercisesByMuscleGroup - Excepción: ${e.message}", e)
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
        Log.d(TAG, "loadExercisesByDifficulty - Cargando ejercicios para dificultad: $difficulty")
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByDifficulty(difficulty).collect { result ->
                    Log.d(TAG, "loadExercisesByDifficulty - Resultado: ${result.javaClass.simpleName}")
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadExercisesByDifficulty - Excepción: ${e.message}", e)
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
        Log.d(TAG, "loadExercisesByPopularity - Cargando ejercicios por popularidad")
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByPopularity().collect { result ->
                    Log.d(TAG, "loadExercisesByPopularity - Resultado: ${result.javaClass.simpleName}")
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadExercisesByPopularity - Excepción: ${e.message}", e)
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
        Log.d(TAG, "loadExercisesByMuscleGroupsAndDifficulties - Grupos: $muscleGroupIds, Dificultades: $difficulties")
        viewModelScope.launch {
            try {
                exerciseRepository.getExercisesByMuscleGroupsAndDifficulties(
                    muscleGroupIds, difficulties
                ).collect { result ->
                    Log.d(TAG, "loadExercisesByMuscleGroupsAndDifficulties - Resultado: ${result.javaClass.simpleName}")
                    handleExercisesResult(result)
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadExercisesByMuscleGroupsAndDifficulties - Excepción: ${e.message}", e)
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
        Log.d(TAG, "incrementPopularity - Incrementando popularidad para ejercicio ID: $exerciseId")
        viewModelScope.launch {
            try {
                exerciseRepository.incrementExercisePopularity(exerciseId).collect { result ->
                    Log.d(TAG, "incrementPopularity - Resultado: ${result.javaClass.simpleName}")
                    when (result) {
                        is Resource.Loading -> {
                            Log.d(TAG, "incrementPopularity - Loading...")
                        }
                        is Resource.Success -> {
                            Log.d(TAG, "incrementPopularity - Success! Recargando ejercicios...")
                            loadAllExercises()
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "incrementPopularity - Error: ${result.message}")
                            _state.update {
                                it.copy(
                                    errorMessage = result.message ?: "Error al actualizar popularidad"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "incrementPopularity - Excepción: ${e.message}", e)
                _state.update {
                    it.copy(
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    private fun filterByDifficulties(difficulties: List<String>) {
        Log.d(TAG, "filterByDifficulties - Filtrando por dificultades: $difficulties")
        val currentExercises = _state.value.exercises
        Log.d(TAG, "filterByDifficulties - Ejercicios actuales: ${currentExercises.size}")

        val filtered = if (difficulties.isEmpty()) {
            Log.d(TAG, "filterByDifficulties - Sin filtros, mostrando todos")
            currentExercises
        } else {
            val result = currentExercises.filter { it.difficulty in difficulties }
            Log.d(TAG, "filterByDifficulties - Ejercicios filtrados: ${result.size}")
            result
        }

        _state.update {
            it.copy(
                filteredExercises = filtered,
                selectedDifficulties = difficulties
            )
        }
    }

    private fun filterByMuscleGroups(muscleGroupIds: List<Int>) {
        Log.d(TAG, "filterByMuscleGroups - Filtrando por grupos musculares: $muscleGroupIds")
        val currentExercises = _state.value.exercises
        Log.d(TAG, "filterByMuscleGroups - Ejercicios actuales: ${currentExercises.size}")

        val filtered = if (muscleGroupIds.isEmpty()) {
            Log.d(TAG, "filterByMuscleGroups - Sin filtros, mostrando todos")
            currentExercises
        } else {
            val result = currentExercises.filter { it.muscleGroupId in muscleGroupIds }
            Log.d(TAG, "filterByMuscleGrounds - Ejercicios filtrados: ${result.size}")
            result
        }

        _state.update {
            it.copy(
                filteredExercises = filtered,
                selectedMuscleGroups = muscleGroupIds
            )
        }
    }

    private fun handleExercisesResult(result: Resource<List<Exercise>>) {
        Log.d(TAG, "handleExercisesResult - Procesando resultado: ${result.javaClass.simpleName}")
        when (result) {
            is Resource.Loading -> {
                Log.d(TAG, "handleExercisesResult - Loading...")
                _state.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
            is Resource.Success -> {
                val exercises = result.data ?: emptyList()
                Log.d(TAG, "handleExercisesResult - Success! Ejercicios: ${exercises.size}")
                exercises.forEachIndexed { index, exercise ->
                    Log.d(TAG, "handleExercisesResult - Ejercicio $index: ${exercise.name}")
                }
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
                Log.e(TAG, "handleExercisesResult - Error: ${result.message}")
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
        Log.d(TAG, "isValidExerciseData - Validando datos del ejercicio")
        Log.d(TAG, "isValidExerciseData - Nombre: '${state.name}', Descripción: '${state.description}', Instrucciones: '${state.instructions}'")
        Log.d(TAG, "isValidExerciseData - MuscleGroupId: ${state.muscleGroupId}, Dificultad: '${state.difficulty}'")

        if (state.name.isBlank()) {
            Log.w(TAG, "isValidExerciseData - Nombre vacío")
            _state.update {
                it.copy(errorMessage = "El nombre del ejercicio es obligatorio")
            }
            return false
        }

        if (state.description.isBlank()) {
            Log.w(TAG, "isValidExerciseData - Descripción vacía")
            _state.update {
                it.copy(errorMessage = "La descripción del ejercicio es obligatoria")
            }
            return false
        }

        if (state.instructions.isBlank()) {
            Log.w(TAG, "isValidExerciseData - Instrucciones vacías")
            _state.update {
                it.copy(errorMessage = "Las instrucciones del ejercicio son obligatorias")
            }
            return false
        }

        Log.d(TAG, "isValidExerciseData - Datos válidos")
        return true
    }
    private fun clearFormData() {
        Log.d(TAG, "clearFormData - Limpiando datos del formulario")
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

    fun canEditOrDeleteExercise(exercise: Exercise): Boolean {
        return canEditOrDelete(exercise)
    }

    fun verifyDatabaseState() {
        viewModelScope.launch {
            try {
                exerciseRepository.getExercises().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val exercises = result.data ?: emptyList()
                            Log.d(TAG, "verifyDatabaseState - Total ejercicios en BD: ${exercises.size}")
                            exercises.forEach { exercise ->
                                Log.d(TAG, "verifyDatabaseState - ${exercise.exerciseId}: ${exercise.name}")
                            }
                        }
                        else -> {
                            Log.d(TAG, "verifyDatabaseState - No se pudieron obtener ejercicios")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "verifyDatabaseState - Error: ${e.message}")
            }
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
            is ExerciseEvent.InsertPredefinedExercises -> {
                insertPredefinedExercises(event.exercises)
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
            is ExerciseEvent.CheckCanEditOrDelete -> {
                val canEdit = event.exercise?.let { canEditOrDelete(it) } ?: false
                _state.update {
                    it.copy(
                        canEditOrDeleteSelected = canEdit
                    )
                }
            }
        }
    }
}