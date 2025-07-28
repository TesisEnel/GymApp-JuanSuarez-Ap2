package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.WorkoutExercise
import edu.ucne.gymapp.data.local.relation.WorkoutExerciseComplete
import edu.ucne.gymapp.data.local.relation.WorkoutExerciseWithSets

@Dao
interface WorkoutExerciseDao {
    @Insert
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExercise): Long

    @Insert
    suspend fun insertWorkoutExercises(workoutExercises: List<WorkoutExercise>): List<Long>

    @Update
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExercise)

    @Delete
    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExercise)

    @Query("SELECT * FROM workout_exercises WHERE workoutExerciseId = :id")
    suspend fun getWorkoutExerciseById(id: Int): WorkoutExercise?

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order`")
    suspend fun getExercisesByWorkout(workoutId: Int): List<WorkoutExercise>

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId AND status = :status")
    suspend fun getExercisesByStatus(workoutId: Int, status: String): List<WorkoutExercise>

    @Query("UPDATE workout_exercises SET status = :status, startTime = :startTime WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun startExercise(workoutExerciseId: Int, status: String, startTime: Long)

    @Query("UPDATE workout_exercises SET status = :status, endTime = :endTime WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun finishExercise(workoutExerciseId: Int, status: String, endTime: Long)

    @Query("UPDATE workout_exercises SET completedSets = :completedSets WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun updateCompletedSets(workoutExerciseId: Int, completedSets: Int)

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    suspend fun deleteExercisesByWorkout(workoutId: Int)

    @Transaction
    @Query("SELECT * FROM workout_exercises WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun getWorkoutExerciseWithSets(workoutExerciseId: Int): WorkoutExerciseWithSets?

    @Transaction
    @Query("SELECT * FROM workout_exercises WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun getCompleteWorkoutExercise(workoutExerciseId: Int): WorkoutExerciseComplete?

    @Transaction
    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order`")
    suspend fun getWorkoutExercisesWithSets(workoutId: Int): List<WorkoutExerciseWithSets>
}