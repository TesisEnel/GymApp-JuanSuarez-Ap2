package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.User
import edu.ucne.gymapp.data.local.entities.UserPreferences

data class UserWithPreferences(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val preferences: UserPreferences?
)