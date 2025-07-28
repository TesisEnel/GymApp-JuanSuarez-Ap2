package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscle_groups")
data class MuscleGroup(
    @PrimaryKey(autoGenerate = true)
    val muscleGroupId: Int = 0,
    val name: String,
    val description: String,
    val iconResource: String? = null
)
