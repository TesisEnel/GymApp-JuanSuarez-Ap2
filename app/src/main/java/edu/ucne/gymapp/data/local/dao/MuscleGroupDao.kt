package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.relation.MuscleGroupWithExercises

@Dao
interface MuscleGroupDao {
    @Insert
    suspend fun insertMuscleGroup(muscleGroup: MuscleGroup): Long

    @Insert
    suspend fun insertMuscleGroups(muscleGroups: List<MuscleGroup>): List<Long>

    @Update
    suspend fun updateMuscleGroup(muscleGroup: MuscleGroup)

    @Delete
    suspend fun deleteMuscleGroup(muscleGroup: MuscleGroup)

    @Query("SELECT * FROM muscle_groups WHERE muscleGroupId = :id")
    suspend fun getMuscleGroupById(id: Int): MuscleGroup?

    @Query("SELECT * FROM muscle_groups")
    suspend fun getMuscleGroups(): List<MuscleGroup>

    @Query("SELECT * FROM muscle_groups ORDER BY name ASC")
    suspend fun getMuscleGroupsOrdered(): List<MuscleGroup>

    @Query("SELECT * FROM muscle_groups WHERE muscleGroupId In (:ids)")
    suspend fun getMuscleGroupsByIds(ids: List<Int>): List<MuscleGroup>

    @Transaction
    @Query("SELECT * FROM muscle_groups WHERE muscleGroupId = :id")
    suspend fun getMuscleGroupWithExercises(id: Int): MuscleGroupWithExercises?

    @Transaction
    @Query("SELECT * FROM muscle_groups")
    suspend fun getMuscleGroupsWithExercises(): List<MuscleGroupWithExercises>
}