package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.ExerciseSet
import edu.ucne.gymapp.data.local.entities.WorkoutExercise

data class WorkoutExerciseWithSets(
    @Embedded val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exercise: Exercise,
    @Relation(
        parentColumn = "workoutExerciseId",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<ExerciseSet>
)