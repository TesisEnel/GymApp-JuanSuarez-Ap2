package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.ExerciseSetDao
import edu.ucne.gymapp.data.local.entities.ExerciseSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExerciseSetRepository @Inject constructor(
    private val exerciseSetDao: ExerciseSetDao
) {
    fun insertSet(exerciseSet: ExerciseSet): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val setId = exerciseSetDao.insertSet(exerciseSet)
            emit(Resource.Success(setId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Ocurrió un error inesperado al registrar esta serie de ejercicios. Inténtalo de nuevo o estira un poco antes"))
        }
    }.flowOn(Dispatchers.IO)

    fun insertSets(exerciseSets: List<ExerciseSet>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val setIds = exerciseSetDao.insertSets(exerciseSets)
            emit(Resource.Success(setIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Algo salió mal al registrar el lote de series. El gimnasio digital está teniendo un mal día."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateSet(exerciseSet: ExerciseSet): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseSetDao.updateSet(exerciseSet)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos actualizar esta serie. Parece que necesita más proteína... o revisar el código."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteSet(exerciseSet: ExerciseSet): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseSetDao.deleteSet(exerciseSet)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "La serie se resistió a ser eliminada. Intenta nuevamente con más fuerza."))
        }
    }.flowOn(Dispatchers.IO)

    fun getSetById(id: Int): Flow<Resource<ExerciseSet?>> = flow {
        try {
            emit(Resource.Loading())
            val exerciseSet = exerciseSetDao.getSetById(id)
            emit(Resource.Success(exerciseSet))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Parece que esta serie está escondida entre los discos. No pudimos encontrarla."))
        }
    }.flowOn(Dispatchers.IO)

    fun getSetsByWorkoutExercise(workoutExerciseId: Int): Flow<Resource<List<ExerciseSet>>> = flow {
        try {
            emit(Resource.Loading())
            val sets = exerciseSetDao.getSetsByWorkoutExercise(workoutExerciseId)
            emit(Resource.Success(sets))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos recuperar las series de este ejercicio. Tal vez están haciendo cardio... lejos del sistema."))
        }
    }.flowOn(Dispatchers.IO)

    fun getCompletedSets(workoutExerciseId: Int): Flow<Resource<List<ExerciseSet>>> = flow {
        try {
            emit(Resource.Loading())
            val completedSets = exerciseSetDao.getCompletedSets(workoutExerciseId)
            emit(Resource.Success(completedSets))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al traer las series completadas. Están celebrando con un batido y no contestan"))
        }
    }.flowOn(Dispatchers.IO)

    fun getPendingSets(workoutExerciseId: Int): Flow<Resource<List<ExerciseSet>>> = flow {
        try {
            emit(Resource.Loading())
            val pendingSets = exerciseSetDao.getPendingSets(workoutExerciseId)
            emit(Resource.Success(pendingSets))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Ups... Las series pendientes se fueron a descansar antes de tiempo."))
        }
    }.flowOn(Dispatchers.IO)

    fun markSetAsCompleted(setId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseSetDao.makeSetAsCompleted(setId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo marcar esta serie como completada. Quizás aún le falta una repetición."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateSetProgress(setId: Int, reps: Int, weight: Float?): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseSetDao.updateSetProgress(setId, reps, weight)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?:  "No pudimos actualizar el progreso. Intenta de nuevo sin soltar la barra."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteSetsByWorkoutExercise(workoutExerciseId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseSetDao.deleteSetsByWorkoutExercise(workoutExerciseId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos eliminar las series. Tal vez siguen calentando."))
        }
    }.flowOn(Dispatchers.IO)
}