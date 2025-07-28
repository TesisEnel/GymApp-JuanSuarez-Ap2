package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.ExerciseSet

@Dao
interface ExerciseSetDao {
    @Insert
    suspend fun insertSet(exerciseSet: ExerciseSet): Long

    @Insert
    suspend fun insertSets(exerciseSets: List<ExerciseSet>): List<Long>

    @Update
    suspend fun updateSet(exerciseSet: ExerciseSet)

    @Delete
    suspend fun deleteSet(exerciseSet: ExerciseSet)

    @Query("SELECT * FROM exercise_sets WHERE exerciseSetId = :id")
    suspend fun getSetById(id: Int): ExerciseSet?

    @Query("SELECT * FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId ORDER BY setNumber")
    suspend fun getSetsByWorkoutExercise(workoutExerciseId: Int): List<ExerciseSet>

    @Query("SELECT * FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId AND isCompleted = 1")
    suspend fun getCompletedSets(workoutExerciseId: Int): List<ExerciseSet>

    @Query("SELECT * FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId AND isCompleted = 0")
    suspend fun getPendingSets(workoutExerciseId: Int): List<ExerciseSet>

    @Query("UPDATE exercise_sets SET isCompleted = 1 WHERE exerciseSetId = :setId")
    suspend fun makeSetAsCompleted(setId: Int)

    @Query("UPDATE exercise_sets SET reps = :reps, weight = :weight WHERE exerciseSetId = :setId")
    suspend fun updateSetProgress(setId: Int, reps: Int, weight: Float?)

    @Query("DELETE FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun deleteSetsByWorkoutExercise(workoutExerciseId: Int)
}