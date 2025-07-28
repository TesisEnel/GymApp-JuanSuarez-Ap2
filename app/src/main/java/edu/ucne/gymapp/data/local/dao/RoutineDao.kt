package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.relation.RoutineComplete
import edu.ucne.gymapp.data.local.relation.RoutineWithExercises

@Dao
interface RoutineDao {
    @Insert
    suspend fun insertRoutine(routine: Routine): Long

    @Insert
    suspend fun insertRoutines(routines: List<Routine>): List<Long>

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("SELECT * FROM routines WHERE routineId = :id")
    suspend fun getRoutineById(id: Int): Routine?

    @Query("SELECT * FROM routines")
    suspend fun getRoutines(): List<Routine>

    @Query("SELECT * FROM routines WHERE difficulty = :difficulty")
    suspend fun getRoutinesByDifficulty(difficulty: String): List<Routine>

    @Query("SELECT * FROM routines WHERE difficulty IN (:difficulties)")
    suspend fun getRoutinesByDifficulties(difficulties: List<String>): List<Routine>

    @Query("SELECT * FROM routines ORDER BY name ASC")
    suspend fun getRoutinesOrdered(): List<Routine>

    @Query("SELECT * FROM routines WHERE estimatedDuration BETWEEN :minDuration AND :maxDuration")
    suspend fun getRoutinesByDuration(minDuration: Int, maxDuration: Int): List<Routine>

    @Query("UPDATE routines SET isActive = 0")
    suspend fun deactivateAllRoutines()

    @Query("UPDATE routines SET isActive = 1 WHERE routineId = :routineId")
    suspend fun activateRoutine(routineId: Int)

    @Query("UPDATE routines SET timesCompleted = timesCompleted + 1, lastModified = :timestamp WHERE routineId = :routineId")
    suspend fun incrementTimesCompleted(routineId: Int, timestamp: Long = System.currentTimeMillis())

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    suspend fun getRoutineWithExercises(routineId: Int): RoutineWithExercises?

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    suspend fun getCompleteRoutine(routineId: Int): RoutineComplete?

    @Transaction
    @Query("SELECT * FROM routines")
    suspend fun getRoutinesWithExercises(): List<RoutineWithExercises>
}
