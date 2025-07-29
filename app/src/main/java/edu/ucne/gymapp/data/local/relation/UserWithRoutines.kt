package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.User

data class UserWithRoutines(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val routines: List<Routine>
)