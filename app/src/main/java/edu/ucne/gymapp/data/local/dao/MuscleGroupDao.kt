package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.relation.MuscleGroupWithExercises

@Dao
interface MuscleGroupDao {

    @Insert
    suspend fun insertMuscleGroups(muscleGroups: List<MuscleGroup>): List<Long>

    @Query("SELECT * FROM muscle_groups")
    suspend fun getMuscleGroups(): List<MuscleGroup>

    @Transaction
    @Query("SELECT * FROM muscle_groups WHERE muscleGroupId = :id")
    suspend fun getMuscleGroupWithExercises(id: Int): MuscleGroupWithExercises?

    @Transaction
    @Query("SELECT * FROM muscle_groups")
    suspend fun getMuscleGroupsWithExercises(): List<MuscleGroupWithExercises>
}