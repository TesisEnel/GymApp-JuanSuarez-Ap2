package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.RoutineExerciseDao
import edu.ucne.gymapp.data.local.entities.RoutineExercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoutineExerciseRepository @Inject constructor(
    private val routineExerciseDao: RoutineExerciseDao
) {

    fun insertRoutineExercise(routineExercise: RoutineExercise): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val routineExerciseId = routineExerciseDao.insertRoutineExercise(routineExercise)
            emit(Resource.Success(routineExerciseId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo agregar el ejercicio a la rutina. Intenta nuevamente."))
        }
    }.flowOn(Dispatchers.IO)

    fun insertRoutineExercises(routineExercises: List<RoutineExercise>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val routineExerciseIds = routineExerciseDao.insertRoutineExercises(routineExercises)
            emit(Resource.Success(routineExerciseIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al agregar varios ejercicios a la rutina. Verifica la conexión o los datos."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateRoutineExercise(routineExercise: RoutineExercise): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineExerciseDao.updateRoutineExercise(routineExercise)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo actualizar el ejercicio. Puede que ya no exista en la rutina."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteRoutineExercise(routineExercise: RoutineExercise): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineExerciseDao.deleteRoutineExercise(routineExercise)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo eliminar el ejercicio. Puede que no esté en la rutina."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutineExerciseById(id: Int): Flow<Resource<RoutineExercise?>> = flow {
        try {
            emit(Resource.Loading())
            val routineExercise = routineExerciseDao.getRoutineExerciseById(id)
            emit(Resource.Success(routineExercise))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al recuperar el ejercicio con ID $id."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByRoutine(routineId: Int): Flow<Resource<List<RoutineExercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = routineExerciseDao.getExercisesByRoutine(routineId)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudieron obtener los ejercicios para la rutina con ID $routineId."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutinesWithExercise(exerciseId: Int): Flow<Resource<List<RoutineExercise>>> = flow {
        try {
            emit(Resource.Loading())
            val routines = routineExerciseDao.getRoutinesWithExercise(exerciseId)
            emit(Resource.Success(routines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al buscar rutinas que contengan el ejercicio con ID $exerciseId."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteExercisesByRoutine(routineId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineExerciseDao.deleteExercisesByRoutine(routineId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudieron eliminar los ejercicios de la rutina con ID $routineId."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateExerciseOrder(routineExerciseId: Int, newOrder: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineExerciseDao.updateExerciseOrder(routineExerciseId, newOrder)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo actualizar el orden del ejercicio con ID $routineExerciseId a $newOrder."))
        }
    }.flowOn(Dispatchers.IO)

    fun getMaxOrderInRoutine(routineId: Int): Flow<Resource<Int?>> = flow {
        try {
            emit(Resource.Loading())
            val maxOrder = routineExerciseDao.getMaxOrderInRoutine(routineId)
            emit(Resource.Success(maxOrder))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener el mayor orden de los ejercicios en la rutina con ID $routineId."))
        }
    }.flowOn(Dispatchers.IO)
    fun getRoutineExercises(routineId: Int): Flow<Resource<List<RoutineExercise>>> = flow {
        try {
            emit(Resource.Loading())
            val routineExercises = routineExerciseDao.getExercisesByRoutine(routineId)
            emit(Resource.Success(routineExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener ejercicios de la rutina con ID $routineId."))
        }
    }.flowOn(Dispatchers.IO)
}