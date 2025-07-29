package edu.ucne.gymapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.MuscleGroup

data class MuscleGroupWithExercises(
    @Embedded val muscleGroup: MuscleGroup,
    @Relation(
        parentColumn = "muscleGroupId",
        entityColumn = "muscleGroupId"
    )
    val exercises: List<Exercise>
)