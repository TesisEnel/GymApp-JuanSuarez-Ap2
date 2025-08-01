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

    fun insertMuscleGroups(muscleGroups: List<MuscleGroup>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val muscleGroupIds = muscleGroupDao.insertMuscleGroups(muscleGroups)
            emit(Resource.Success(muscleGroupIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al intentar cargar varios grupos musculares. Quizás están todos en su día de descanso."))
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