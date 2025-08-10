package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.WorkoutExerciseDao
import edu.ucne.gymapp.data.local.entities.WorkoutExercise
import edu.ucne.gymapp.data.local.relation.WorkoutExerciseComplete
import edu.ucne.gymapp.data.local.relation.WorkoutExerciseWithSets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WorkoutExerciseRepository @Inject constructor(
    private val workoutExerciseDao: WorkoutExerciseDao
) {
    fun insertWorkoutExercise(workoutExercise: WorkoutExercise): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val workoutExerciseId = workoutExerciseDao.insertWorkoutExercise(workoutExercise)
            emit(Resource.Success(workoutExerciseId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Oops, no pudimos añadir este ejercicio a tu rutina. ¡Intenta de nuevo!"))
        }
    }.flowOn(Dispatchers.IO)

    fun insertWorkoutExercises(workoutExercises: List<WorkoutExercise>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val workoutExerciseIds = workoutExerciseDao.insertWorkoutExercises(workoutExercises)
            emit(Resource.Success(workoutExerciseIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Intentamos cargar tus ejercicios, pero se nos cayeron las pesas..."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateWorkoutExercise(workoutExercise: WorkoutExercise): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutExerciseDao.updateWorkoutExercise(workoutExercise)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al ajustar este ejercicio. ¡Es como si se nos hubiera roto la máquina!"))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteWorkoutExercise(workoutExercise: WorkoutExercise): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutExerciseDao.deleteWorkoutExercise(workoutExercise)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos eliminar este ejercicio. Tal vez necesitaba una repetición más."))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutExerciseById(id: Int): Flow<Resource<WorkoutExercise?>> = flow {
        try {
            emit(Resource.Loading())
            val workoutExercise = workoutExerciseDao.getWorkoutExerciseById(id)
            emit(Resource.Success(workoutExercise))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Buscamos el ejercicio, pero parece que se escondió en los vestidores."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByWorkout(workoutId: Int): Flow<Resource<List<WorkoutExercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = workoutExerciseDao.getExercisesByWorkout(workoutId)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos los ejercicios para este entrenamiento. ¡Probablemente se tomaron un descanso!"))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByStatus(workoutId: Int, status: String): Flow<Resource<List<WorkoutExercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = workoutExerciseDao.getExercisesByStatus(workoutId, status)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Estos ejercicios no están respondiendo... ¿Será que entraron en modo zen?"))
        }
    }.flowOn(Dispatchers.IO)

    fun startExercise(workoutExerciseId: Int, status: String, startTime: Long): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutExerciseDao.startExercise(workoutExerciseId, status, startTime)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Intentamos iniciar el ejercicio, pero se nos quedó sin energía..."))
        }
    }.flowOn(Dispatchers.IO)

    fun finishExercise(workoutExerciseId: Int, status: String, endTime: Long): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutExerciseDao.finishExercise(workoutExerciseId, status, endTime)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Finalizar este ejercicio fue más difícil que un burpee..."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateCompletedSets(workoutExerciseId: Int, completedSets: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutExerciseDao.updateCompletedSets(workoutExerciseId, completedSets)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos actualizar tus sets completados. ¡Pero tú sí que completaste el intento!"))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteExercisesByWorkout(workoutId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutExerciseDao.deleteExercisesByWorkout(workoutId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Tratar de borrar estos ejercicios fue como empujar un trineo cargado..."))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutExerciseWithSets(workoutExerciseId: Int): Flow<Resource<WorkoutExerciseWithSets?>> = flow {
        try {
            emit(Resource.Loading())
            val workoutExerciseWithSets = workoutExerciseDao.getWorkoutExerciseWithSets(workoutExerciseId)
            emit(Resource.Success(workoutExerciseWithSets))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos este ejercicio con sus sets... ¿quizás se fue a tomar agua?"))
        }
    }.flowOn(Dispatchers.IO)

    fun getCompleteWorkoutExercise(workoutExerciseId: Int): Flow<Resource<WorkoutExerciseComplete?>> = flow {
        try {
            emit(Resource.Loading())
            val completeWorkoutExercise = workoutExerciseDao.getCompleteWorkoutExercise(workoutExerciseId)
            emit(Resource.Success(completeWorkoutExercise))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Ejercicio completo... pero desaparecido. ¿Será parte del entrenamiento ninja?"))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutExercisesWithSets(workoutId: Int): Flow<Resource<List<WorkoutExerciseWithSets>>> = flow {
        try {
            emit(Resource.Loading())
            val workoutExercisesWithSets = workoutExerciseDao.getWorkoutExercisesWithSets(workoutId)
            emit(Resource.Success(workoutExercisesWithSets))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos cargar los ejercicios con sets. ¡Nos falló la proteína de datos!"))
        }
    }.flowOn(Dispatchers.IO)
}