package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.relation.ExerciseWithMuscleGroup

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE exerciseId = :id")
    suspend fun getExerciseById(id: Int): Exercise?

    @Query("SELECT * FROM exercises")
    suspend fun getExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE muscleGroupId = :muscleGroupId")
    suspend fun getExercisesByMuscleGroup(muscleGroupId: Int): List<Exercise>

    @Query("SELECT * FROM exercises WHERE muscleGroupId IN (:muscleGroupIds)")
    suspend fun getExercisesByMuscleGroups(muscleGroupIds: List<Int>): List<Exercise>

    @Query("SELECT * FROM exercises WHERE difficulty = :difficulty")
    suspend fun getExercisesByDifficulty(difficulty: String): List<Exercise>
    @Query("SELECT * FROM exercises WHERE exerciseId IN (:exerciseIds)")
    suspend fun getExercisesByIds(exerciseIds: List<Int>): List<Exercise>

    @Query("SELECT * FROM exercises WHERE difficulty IN (:difficulties)")
    suspend fun getExercisesByDifficulties(difficulties: List<String>): List<Exercise>

    @Query("SELECT * FROM exercises WHERE muscleGroupId IN (:muscleGroupIds) AND difficulty IN (:difficulties)")
    suspend fun getExercisesByMuscleGroupsAndDifficulties(muscleGroupIds: List<Int>, difficulties: List<String>): List<Exercise>

    @Query("SELECT * FROM exercises ORDER BY popularity DESC")
    suspend fun getExercisesByPopularity(): List<Exercise>

    @Query("UPDATE exercises SET popularity = popularity + 1 WHERE exerciseId = :exerciseId")
    suspend fun incrementExercisePopularity(exerciseId: Int)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Query("SELECT COUNT(*) FROM exercises WHERE name = :name AND muscleGroupId = :muscleGroupId")
    suspend fun exerciseExists(name: String, muscleGroupId: Int): Int

    @Transaction
    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    suspend fun getExerciseWithMuscleGroup(exerciseId: Int): ExerciseWithMuscleGroup?

    @Transaction
    @Query("SELECT * FROM exercises WHERE muscleGroupId = :muscleGroupId")
    suspend fun getExercisesWithMuscleGroup(muscleGroupId: Int): List<ExerciseWithMuscleGroup>
}