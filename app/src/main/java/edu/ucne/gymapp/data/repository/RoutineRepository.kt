package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.RoutineDao
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.relation.RoutineComplete
import edu.ucne.gymapp.data.local.relation.RoutineWithExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao
) {
    fun insertRoutine(routine: Routine): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val routineId = routineDao.insertRoutine(routine)
            emit(Resource.Success(routineId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Esta rutina no quiso entrar. Tal vez necesitaba estiramiento antes del código."))
        }
    }.flowOn(Dispatchers.IO)

    fun insertRoutines(routines: List<Routine>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val routineIds = routineDao.insertRoutines(routines)
            emit(Resource.Success(routineIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Intentamos cargar todas las rutinas, pero se quedaron atrapadas en un burpee infinito."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateRoutine(routine: Routine): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineDao.updateRoutine(routine)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Esta rutina se resistió al cambio. A lo mejor está muy apegada a su antigua versión."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteRoutine(routine: Routine): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineDao.deleteRoutine(routine)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos eliminar esta rutina. Quizá se escondió en la zona de cardio."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutineById(id: Int): Flow<Resource<Routine?>> = flow {
        try {
            emit(Resource.Loading())
            val routine = routineDao.getRoutineById(id)
            emit(Resource.Success(routine))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Esa rutina se nos escabulló. Quizás está en modo fantasma."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutines(): Flow<Resource<List<Routine>>> = flow {
        try {
            emit(Resource.Loading())
            val routines = routineDao.getRoutines()
            emit(Resource.Success(routines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Las rutinas no se dejaron ver. Puede que estén en su día de descanso activo."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutinesByDifficulty(difficulty: String): Flow<Resource<List<Routine>>> = flow {
        try {
            emit(Resource.Loading())
            val routines = routineDao.getRoutinesByDifficulty(difficulty)
            emit(Resource.Success(routines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Parece que las rutinas '$difficulty' se fueron a estirar."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutinesByDifficulties(difficulties: List<String>): Flow<Resource<List<Routine>>> = flow {
        try {
            emit(Resource.Loading())
            val routines = routineDao.getRoutinesByDifficulties(difficulties)
            emit(Resource.Success(routines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Las rutinas con esas dificultades no están disponibles. Quizá están en recuperación muscular."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutinesOrdered(): Flow<Resource<List<Routine>>> = flow {
        try {
            emit(Resource.Loading())
            val routines = routineDao.getRoutinesOrdered()
            emit(Resource.Success(routines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos ordenar las rutinas. Se rebelaron contra el sistema."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutinesByDuration(minDuration: Int, maxDuration: Int): Flow<Resource<List<Routine>>> = flow {
        try {
            emit(Resource.Loading())
            val routines = routineDao.getRoutinesByDuration(minDuration, maxDuration)
            emit(Resource.Success(routines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Las rutinas con esa duración se perdieron en el tiempo... de descanso."))
        }
    }.flowOn(Dispatchers.IO)

    fun deactivateAllRoutines(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineDao.deactivateAllRoutines()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudieron desactivar las rutinas. Algunas se resistieron con fuerza bruta."))
        }
    }.flowOn(Dispatchers.IO)

    fun activateRoutine(routineId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineDao.activateRoutine(routineId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Intentamos activar la rutina, pero se quedó dormida en el sofá."))
        }
    }.flowOn(Dispatchers.IO)

    fun incrementTimesCompleted(routineId: Int, timestamp: Long = System.currentTimeMillis()): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            routineDao.incrementTimesCompleted(routineId, timestamp)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos contar esta rutina como completada. El sudor bloqueó el sensor."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutineWithExercises(routineId: Int): Flow<Resource<RoutineWithExercises?>> = flow {
        try {
            emit(Resource.Loading())
            val routineWithExercises = routineDao.getRoutineWithExercises(routineId)
            emit(Resource.Success(routineWithExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Esta rutina con ejercicios no se presentó al llamado. Debe estar en la caminadora."))
        }
    }.flowOn(Dispatchers.IO)

    fun getCompleteRoutine(routineId: Int): Flow<Resource<RoutineComplete?>> = flow {
        try {
            emit(Resource.Loading())
            val completeRoutine = routineDao.getCompleteRoutine(routineId)
            emit(Resource.Success(completeRoutine))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No logramos traer la rutina completa. Parece que dejó la mitad en la banca."))
        }
    }.flowOn(Dispatchers.IO)

    fun getRoutinesWithExercises(): Flow<Resource<List<RoutineWithExercises>>> = flow {
        try {
            emit(Resource.Loading())
            val routinesWithExercises = routineDao.getRoutinesWithExercises()
            emit(Resource.Success(routinesWithExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Las rutinas con ejercicios no llegaron. Tal vez están haciendo sentadillas sincronizadas."))
        }
    }.flowOn(Dispatchers.IO)
}