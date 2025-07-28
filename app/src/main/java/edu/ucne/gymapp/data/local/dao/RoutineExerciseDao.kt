package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.RoutineExercise

@Dao
interface RoutineExerciseDao {
    @Insert
    suspend fun insertRoutineExercise(routineExercise: RoutineExercise): Long

    @Insert
    suspend fun insertRoutineExercises(routineExercises: List<RoutineExercise>): List<Long>

    @Update
    suspend fun updateRoutineExercise(routineExercise: RoutineExercise)

    @Delete
    suspend fun deleteRoutineExercise(routineExercise: RoutineExercise)

    @Query("SELECT * FROM routine_exercises WHERE routineExerciseId = :id")
    suspend fun getRoutineExerciseById(id: Int): RoutineExercise?

    @Query("SELECT * FROM routine_exercises WHERE routineId = :routineId ORDER BY `order`")
    suspend fun getExercisesByRoutine(routineId: Int): List<RoutineExercise>

    @Query("SELECT * FROM routine_exercises WHERE exerciseId = :exerciseId")
    suspend fun getRoutinesWithExercise(exerciseId: Int): List<RoutineExercise>

    @Query("DELETE FROM routine_exercises WHERE routineId = :routineId")
    suspend fun deleteExercisesByRoutine(routineId: Int)

    @Query("UPDATE routine_exercises SET `order` = :newOrder WHERE routineExerciseId = :routineExerciseId")
    suspend fun updateExerciseOrder(routineExerciseId: Int, newOrder: Int)

    @Query("SELECT MAX(`order`) FROM routine_exercises WHERE routineId = :routineId")
    suspend fun getMaxOrderInRoutine(routineId: Int): Int?
}