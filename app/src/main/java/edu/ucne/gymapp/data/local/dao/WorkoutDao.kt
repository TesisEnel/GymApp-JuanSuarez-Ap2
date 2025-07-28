package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.relation.WorkoutComplete
import edu.ucne.gymapp.data.local.relation.WorkoutWithExercises

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Insert
    suspend fun insertWorkouts(workouts: List<Workout>): List<Long>

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getWorkoutById(id: Int): Workout?

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getWorkoutsByUser(userId: Int): List<Workout>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND status = :status")
    suspend fun getWorkoutsByStatus(userId: Int, status: String): List<Workout>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND status = 'IN_PROGRESS' LIMIT 1")
    suspend fun getActiveWorkout(userId: Int): Workout?

    @Query("SELECT * FROM workouts WHERE routineId = :routineId")
    suspend fun getWorkoutsByRoutine(routineId: Int): List<Workout>

    @Query("UPDATE workouts SET status = :status, endTime = :endTime, totalDuration = :duration WHERE workoutId = :workoutId")
    suspend fun finishWorkout(workoutId: Int, status: String, endTime: Long, duration: Int)

    @Query("UPDATE workouts SET status = 'PAUSED' WHERE workoutId = :workoutId")
    suspend fun pauseWorkout(workoutId: Int)

    @Query("UPDATE workouts SET status = 'IN_PROGRESS' WHERE workoutId = :workoutId")
    suspend fun resumeWorkout(workoutId: Int)

    @Query("SELECT COUNT(*) FROM workouts WHERE userId = :userId AND status = 'COMPLETED'")
    suspend fun getCompletedWorkoutsCount(userId: Int): Int

    @Query("SELECT SUM(totalDuration) FROM workouts WHERE userId = :userId AND status = 'COMPLETED'")
    suspend fun getTotalWorkoutTime(userId: Int): Int?

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises?

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    suspend fun getCompleteWorkout(workoutId: Int): WorkoutComplete?

    @Transaction
    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getUserWorkoutsWithExercises(userId: Int): List<WorkoutWithExercises>
}