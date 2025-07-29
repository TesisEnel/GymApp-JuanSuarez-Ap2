package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.entities.WorkoutExercise

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val workoutExercises: List<WorkoutExercise>
)