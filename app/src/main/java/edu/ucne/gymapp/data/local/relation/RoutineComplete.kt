package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.RoutineExercise

data class RoutineComplete(
    @Embedded val routine: Routine,

    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val routineExercises: List<RoutineExercise>,

    @Relation(
        entity = Exercise::class,
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(
            value = RoutineExercise::class,
            parentColumn = "routineId",
            entityColumn = "exerciseId"
        )
    )
    val exercises: List<Exercise>
)