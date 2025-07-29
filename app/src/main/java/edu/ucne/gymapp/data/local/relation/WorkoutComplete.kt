package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.entities.WorkoutExercise

data class WorkoutComplete(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val routine: Routine?,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val workoutExercises: List<WorkoutExercise>
)