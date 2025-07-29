package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.MuscleGroupDao
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.relation.MuscleGroupWithExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MuscleGroupRepository @Inject constructor(
    private val muscleGroupDao: MuscleGroupDao
) {
    fun insertMuscleGroup(muscleGroup: MuscleGroup): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroupId = muscleGroupDao.insertMuscleGroup(muscleGroup)
            emit(Resource.Success(muscleGroupId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No logramos registrar este grupo muscular... parece que se nos contracturó el sistema."))
        }
    }.flowOn(Dispatchers.IO)

    fun insertMuscleGroups(muscleGroups: List<MuscleGroup>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroupIds = muscleGroupDao.insertMuscleGroups(muscleGroups)
            emit(Resource.Success(muscleGroupIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al intentar cargar varios grupos musculares. Quizás están todos en su día de descanso."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateMuscleGroup(muscleGroup: MuscleGroup): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            muscleGroupDao.updateMuscleGroup(muscleGroup)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo actualizar este grupo muscular. Tal vez necesita un buen estiramiento... de código."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteMuscleGroup(muscleGroup: MuscleGroup): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            muscleGroupDao.deleteMuscleGroup(muscleGroup)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Este grupo muscular se resiste a desaparecer. ¡Necesitamos más fuerza bruta!"))
        }
    }.flowOn(Dispatchers.IO)

    fun getMuscleGroupById(id: Int): Flow<Resource<MuscleGroup?>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroup = muscleGroupDao.getMuscleGroupById(id)
            emit(Resource.Success(muscleGroup))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos ese grupo muscular. Quizás está escondido tras una serie de abdominales."))
        }
    }.flowOn(Dispatchers.IO)

    fun getMuscleGroups(): Flow<Resource<List<MuscleGroup>>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroups = muscleGroupDao.getMuscleGroups()
            emit(Resource.Success(muscleGroups))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos recuperar los grupos musculares. Se fueron todos a correr al parque."))
        }
    }.flowOn(Dispatchers.IO)

    fun getMuscleGroupsOrdered(): Flow<Resource<List<MuscleGroup>>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroups = muscleGroupDao.getMuscleGroupsOrdered()
            emit(Resource.Success(muscleGroups))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No logramos traer los grupos musculares ordenados. Están haciendo fila... pero no responden."))
        }
    }.flowOn(Dispatchers.IO)

    fun getMuscleGroupsByIds(ids: List<Int>): Flow<Resource<List<MuscleGroup>>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroups = muscleGroupDao.getMuscleGroupsByIds(ids)
            emit(Resource.Success(muscleGroups))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No fue posible encontrar los grupos musculares por esos IDs. A lo mejor están entrenando en modo incógnito."))
        }
    }.flowOn(Dispatchers.IO)

    fun getMuscleGroupWithExercises(id: Int): Flow<Resource<MuscleGroupWithExercises?>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroupWithExercises = muscleGroupDao.getMuscleGroupWithExercises(id)
            emit(Resource.Success(muscleGroupWithExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No logramos conectar este grupo muscular con sus ejercicios. ¡Parece que no fueron al entrenamiento!"))
        }
    }.flowOn(Dispatchers.IO)

    fun getMuscleGroupsWithExercises(): Flow<Resource<List<MuscleGroupWithExercises>>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroupsWithExercises = muscleGroupDao.getMuscleGroupsWithExercises()
            emit(Resource.Success(muscleGroupsWithExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos traer los grupos musculares con sus ejercicios. Tal vez están todos en modo recuperación."))
        }
    }.flowOn(Dispatchers.IO)
}