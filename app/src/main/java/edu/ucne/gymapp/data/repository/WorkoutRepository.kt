package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.WorkoutDao
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.relation.WorkoutComplete
import edu.ucne.gymapp.data.local.relation.WorkoutWithExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) {
    fun insertWorkout(workout: Workout): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val workoutId = workoutDao.insertWorkout(workout)
            emit(Resource.Success(workoutId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Ups, algo impidió guardar tu entrenamiento. Asegúrate de no haber olvidado nada."))
        }
    }.flowOn(Dispatchers.IO)

    fun insertWorkouts(workouts: List<Workout>): Flow<Resource<List<Long>>> = flow {
        try {
            emit(Resource.Loading())
            val workoutIds = workoutDao.insertWorkouts(workouts)
            emit(Resource.Success(workoutIds))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos cargar la rutina completa. ¿Puedes intentarlo otra vez?"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateWorkout(workout: Workout): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutDao.updateWorkout(workout)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Editar este entrenamiento no funcionó. Quizás necesitaba un poco más de motivación."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteWorkout(workout: Workout): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutDao.deleteWorkout(workout)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos eliminar ese entrenamiento. Parece que aún no quiere irse."))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutById(id: Int): Flow<Resource<Workout?>> = flow {
        try {
            emit(Resource.Loading())
            val workout = workoutDao.getWorkoutById(id)
            emit(Resource.Success(workout))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos ese entrenamiento. Tal vez se fue a correr por su cuenta."))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutsByUser(userId: Int): Flow<Resource<List<Workout>>> = flow {
        try {
            emit(Resource.Loading())
            val workouts = workoutDao.getWorkoutsByUser(userId)
            emit(Resource.Success(workouts))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Tus entrenamientos están jugando a las escondidas. Intenta más tarde."))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutsByStatus(userId: Int, status: String): Flow<Resource<List<Workout>>> = flow {
        try {
            emit(Resource.Loading())
            val workouts = workoutDao.getWorkoutsByStatus(userId, status)
            emit(Resource.Success(workouts))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo filtrar por estado. Parece que todos están confundidos."))
        }
    }.flowOn(Dispatchers.IO)

    fun getActiveWorkout(userId: Int): Flow<Resource<Workout?>> = flow {
        try {
            emit(Resource.Loading())
            val activeWorkout = workoutDao.getActiveWorkout(userId)
            emit(Resource.Success(activeWorkout))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No encontramos ningún entrenamiento activo. ¿Seguro que empezaste uno?"))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutsByRoutine(routineId: Int): Flow<Resource<List<Workout>>> = flow {
        try {
            emit(Resource.Loading())
            val workouts = workoutDao.getWorkoutsByRoutine(routineId)
            emit(Resource.Success(workouts))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Esta rutina no quiere contarnos sus secretos... aún."))
        }
    }.flowOn(Dispatchers.IO)

    fun finishWorkout(workoutId: Int, status: String, endTime: Long, duration: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutDao.finishWorkout(workoutId, status, endTime, duration)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos finalizar tu entrenamiento. Quizás necesita una vuelta más."))
        }
    }.flowOn(Dispatchers.IO)

    fun pauseWorkout(workoutId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutDao.pauseWorkout(workoutId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Tu entrenamiento no quiso pausar. Tal vez está demasiado motivado."))
        }
    }.flowOn(Dispatchers.IO)

    fun resumeWorkout(workoutId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            workoutDao.resumeWorkout(workoutId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Este entrenamiento no despertó. ¿Le damos otro intento?"))
        }
    }.flowOn(Dispatchers.IO)

    fun getCompletedWorkoutsCount(userId: Int): Flow<Resource<Int>> = flow {
        try {
            emit(Resource.Loading())
            val count = workoutDao.getCompletedWorkoutsCount(userId)
            emit(Resource.Success(count))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos contar cuántos entrenamientos completaste. ¡Pero sabemos que son muchos!"))
        }
    }.flowOn(Dispatchers.IO)

    fun getTotalWorkoutTime(userId: Int): Flow<Resource<Int?>> = flow {
        try {
            emit(Resource.Loading())
            val totalTime = workoutDao.getTotalWorkoutTime(userId)
            emit(Resource.Success(totalTime))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Se nos escapó el cronómetro. No pudimos calcular tu tiempo total."))
        }
    }.flowOn(Dispatchers.IO)

    fun getWorkoutWithExercises(workoutId: Int): Flow<Resource<WorkoutWithExercises?>> = flow {
        try {
            emit(Resource.Loading())
            val workoutWithExercises = workoutDao.getWorkoutWithExercises(workoutId)
            emit(Resource.Success(workoutWithExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos traer los ejercicios. Puede que estén haciendo flexiones fuera de base."))
        }
    }.flowOn(Dispatchers.IO)

    fun getCompleteWorkout(workoutId: Int): Flow<Resource<WorkoutComplete?>> = flow {
        try {
            emit(Resource.Loading())
            val completeWorkout = workoutDao.getCompleteWorkout(workoutId)
            emit(Resource.Success(completeWorkout))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Este entrenamiento no vino completo. ¿Intentamos armarlo de nuevo?"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserWorkoutsWithExercises(userId: Int): Flow<Resource<List<WorkoutWithExercises>>> = flow {
        try {
            emit(Resource.Loading())
            val workoutsWithExercises = workoutDao.getUserWorkoutsWithExercises(userId)
            emit(Resource.Success(workoutsWithExercises))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Tus entrenamientos y ejercicios se escondieron. Vamos a buscarlos otra vez."))
        }
    }.flowOn(Dispatchers.IO)
}