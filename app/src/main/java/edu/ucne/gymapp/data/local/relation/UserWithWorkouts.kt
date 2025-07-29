package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.User
import edu.ucne.gymapp.data.local.entities.Workout

data class UserWithWorkouts(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val workouts: List<Workout>
)