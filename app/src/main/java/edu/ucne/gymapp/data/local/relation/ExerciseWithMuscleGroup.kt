package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.MuscleGroup

data class ExerciseWithMuscleGroup(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "muscleGroupId",
        entityColumn = "muscleGroupId"
    )
    val muscleGroup: MuscleGroup
)