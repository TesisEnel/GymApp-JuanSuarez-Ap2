package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.RoutineExercise

data class RoutineWithExercises(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val routineExercises: List<RoutineExercise>
)