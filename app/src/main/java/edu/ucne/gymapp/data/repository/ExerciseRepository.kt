package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.ExerciseDao
import edu.ucne.gymapp.presentation.exercises.PredefinedExercises
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.relation.ExerciseWithMuscleGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    fun insertExercise(exercise: Exercise): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val exerciseId = exerciseDao.insertExercise(exercise)
            emit(Resource.Success(exerciseId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Este ejercicio no quiso entrar al sistema. Tal vez necesita calentar primero."))
        }
    }.flowOn(Dispatchers.IO)
    fun getExercisesByIds(exerciseIds: List<Int>): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByIds(exerciseIds)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener ejercicios por IDs"))
        }
    }.flowOn(Dispatchers.IO)

    fun insertExercises(exercises: List<Exercise>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val exerciseIds = exerciseDao.insertExercises(exercises)
            emit(Resource.Success(exerciseIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Falló la carga masiva de ejercicios. Al parecer el servidor se quedó sin energía."))
        }
    }.flowOn(Dispatchers.IO)

    fun insertExercisesSynchronously(exercises: List<Exercise>): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())

            exercises.forEachIndexed { index, exercise ->
                try {
                    val exerciseId = exerciseDao.insertExercise(exercise)
                } catch (e: Exception) {
                    throw e
                }
            }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error insertando ejercicios uno por uno"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateExercise(exercise: Exercise): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseDao.updateExercise(exercise)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos actualizar este ejercicio. Parece que se quedó atascado en una mala repetición."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteExercise(exercise: Exercise): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseDao.deleteExercise(exercise)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Intentamos eliminar este ejercicio, pero se aferró a su lugar como un curl bien hecho."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExerciseById(id: Int): Flow<Resource<Exercise?>> = flow {
        try {
            emit(Resource.Loading())
            val exercise = exerciseDao.getExerciseById(id)
            emit(Resource.Success(exercise))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos ese ejercicio. Tal vez se escondió detrás de una barra de 100kg."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercises(): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())


            val dbExercises = exerciseDao.getExercises().toMutableList()

            val predefinedExercises = PredefinedExercises.getAll()

            val existingIds = dbExercises.map { it.exerciseId }.toSet()
            val missingPredefined = predefinedExercises.filter { it.exerciseId !in existingIds }

            if (missingPredefined.isNotEmpty()) {
                missingPredefined.forEach { exercise ->
                    try {
                        val insertedId = exerciseDao.insertExercise(exercise)
                        dbExercises.add(exercise.copy(exerciseId = insertedId.toInt()))
                    } catch (e: Exception) {
                    }
                }
            }

            val allExercises = dbExercises.sortedBy { it.exerciseId }


            if (allExercises.isEmpty()) {
            } else {
                allExercises.forEachIndexed { index, exercise ->
                }
            }

            emit(Resource.Success(allExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "La lista de ejercicios no se pudo cargar. Posiblemente está haciendo cardio... lejos."))
        }
    }.flowOn(Dispatchers.IO)
    fun getExercisesByMuscleGroup(muscleGroupId: Int): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByMuscleGroup(muscleGroupId)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No logramos traer los ejercicios de ese grupo muscular. Quizá están en su día de descanso."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByMuscleGroups(muscleGroupIds: List<Int>): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByMuscleGroups(muscleGroupIds)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No fue posible obtener los ejercicios por múltiples grupos. Se nos cruzaron los cables... o los tendones."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByDifficulty(difficulty: String): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByDifficulty(difficulty)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos filtrar por dificultad '$difficulty'. Parece que los ejercicios nivel boss se escondieron."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByDifficulties(difficulties: List<String>): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByDifficulties(difficulties)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Algo falló al filtrar por dificultades. Quizá mezclaste novato con leyenda."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByMuscleGroupsAndDifficulties(
        muscleGroupIds: List<Int>,
        difficulties: List<String>
    ): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByMuscleGroupsAndDifficulties(muscleGroupIds, difficulties)
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error combinando músculos y dificultad. Parece una rutina imposible hasta para Hulk."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesByPopularity(): Flow<Resource<List<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val exercises = exerciseDao.getExercisesByPopularity()
            emit(Resource.Success(exercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos cargar los más populares. Tal vez están ocupados firmando autógrafos."))
        }
    }.flowOn(Dispatchers.IO)

    fun incrementExercisePopularity(exerciseId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            exerciseDao.incrementExercisePopularity(exerciseId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo subir la popularidad. Este ejercicio todavía no es tendencia."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExerciseWithMuscleGroup(exerciseId: Int): Flow<Resource<ExerciseWithMuscleGroup?>> = flow {
        try {
            emit(Resource.Loading())
            val exerciseWithMuscleGroup = exerciseDao.getExerciseWithMuscleGroup(exerciseId)
            emit(Resource.Success(exerciseWithMuscleGroup))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos el músculo que trabaja este ejercicio. Quizá es tan secreto como la fórmula de la proteína perfecta."))
        }
    }.flowOn(Dispatchers.IO)

    fun getExercisesWithMuscleGroup(muscleGroupId: Int): Flow<Resource<List<ExerciseWithMuscleGroup>>> = flow {
        try {
            emit(Resource.Loading())
            val exercisesWithMuscleGroup = exerciseDao.getExercisesWithMuscleGroup(muscleGroupId)
            emit(Resource.Success(exercisesWithMuscleGroup))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Falló la combinación ejercicio + músculo. Puede que haya ocurrido una desconexión muscular inesperada."))
        }
    }.flowOn(Dispatchers.IO)
}