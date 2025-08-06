package edu.ucne.gymapp.presentation.musclegroup

import edu.ucne.gymapp.data.local.database.GymDatabase
import edu.ucne.gymapp.data.local.entities.MuscleGroup

object PredefinedMuscleGroups {

    private const val CHEST_ICON = "chest"
    private const val BACK_ICON = "back"
    private const val SHOULDERS_ICON = "shoulder"
    private const val BICEPS_ICON = "biceps"
    private const val TRICEPS_ICON = "triceps"
    private const val FOREARMS_ICON = "forearms"
    private const val ABS_ICON = "abs"
    private const val QUADRICEPS_ICON = "quadriceps"
    private const val HAMSTRINGS_ICON = "hamstrings"
    private const val BUTTOCKS_ICON = "buttocks"
    private const val CALVES_ICON = "calves"
    private const val ABDUCTORS_ICON = "abductors1"
    private const val ABDUCTORS_ICON_2 = "abductors2"
    private const val NECK_ICON = "neck"
    private const val TRAPEZE_ICON = "trapeze"
    private const val LOWER_BACK_ICON = "lower_back"

    val muscleGroups = listOf(
        MuscleGroup(
            muscleGroupId = 1,
            name = "Pecho",
            description = "Músculos pectorales - Mayor, menor y serrato anterior",
            iconResource = CHEST_ICON
        ),
        MuscleGroup(
            muscleGroupId = 2,
            name = "Espalda",
            description = "Dorsales, romboides, trapecio, redondo mayor y menor",
            iconResource = BACK_ICON
        ),
        MuscleGroup(
            muscleGroupId = 3,
            name = "Hombros",
            description = "Deltoides anterior, medio y posterior",
            iconResource = SHOULDERS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 4,
            name = "Bíceps",
            description = "Bíceps braquial, braquial anterior y coracobraquial",
            iconResource = BICEPS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 5,
            name = "Tríceps",
            description = "Tríceps braquial - cabeza larga, lateral y medial",
            iconResource = TRICEPS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 6,
            name = "Antebrazos",
            description = "Flexores, extensores y músculos de la muñeca",
            iconResource = FOREARMS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 7,
            name = "Abdominales",
            description = "Recto abdominal, oblicuos y transverso del abdomen",
            iconResource = ABS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 8,
            name = "Cuádriceps",
            description = "Recto femoral, vasto lateral, medial e intermedio",
            iconResource = QUADRICEPS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 9,
            name = "Isquiotibiales",
            description = "Bíceps femoral, semitendinoso y semimembranoso",
            iconResource = HAMSTRINGS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 10,
            name = "Glúteos",
            description = "Glúteo mayor, medio y menor",
            iconResource = BUTTOCKS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 11,
            name = "Pantorrillas",
            description = "Gastrocnemio, sóleo y tibial anterior",
            iconResource = CALVES_ICON
        ),
        MuscleGroup(
            muscleGroupId = 12,
            name = "Abductores",
            description = "Músculos de la cadera - abductores internos",
            iconResource = ABDUCTORS_ICON
        ),
        MuscleGroup(
            muscleGroupId = 13,
            name = "Abductores",
            description = "Músculos de la cadera - aductores externos",
            iconResource = ABDUCTORS_ICON_2
        ),

        MuscleGroup(
            muscleGroupId = 14,
            name = "Cuello",
            description = "Esternocleidomastoideo y músculos cervicales",
            iconResource = NECK_ICON
        )
    )

    private val backSubGroups = listOf(
        MuscleGroup(
            muscleGroupId = 15,
            name = "Dorsales",
            description = "Dorsal ancho - porción superior e inferior",
            iconResource = BACK_ICON
        ),
        MuscleGroup(
            muscleGroupId = 16,
            name = "Trapecio",
            description = "Trapecio superior, medio e inferior",
            iconResource = TRAPEZE_ICON
        ),
        MuscleGroup(
            muscleGroupId = 17,
            name = "Espalda Alta",
            description = "Romboides, trapecio medio y posterior deltoides",
            iconResource = BACK_ICON
        ),
        MuscleGroup(
            muscleGroupId = 18,
            name = "Espalda Baja",
            description = "Erectores espinales y cuadrado lumbar",
            iconResource = LOWER_BACK_ICON
        )

    )

    fun getAllMuscleGroups(): List<MuscleGroup> {
        return muscleGroups + backSubGroups
    }

    fun getMainMuscleGroups(): List<MuscleGroup> {
        return muscleGroups.take(7)
    }

    fun getSpecificMuscleGroups(): List<MuscleGroup> {
        return muscleGroups.drop(7)
    }

    fun getBackSubGroups(): List<MuscleGroup> {
        return backSubGroups
    }

}

suspend fun GymDatabase.initializePredefinedMuscleGroups() {
    val existingGroups = muscleGroupDao().getMuscleGroups()

    if (existingGroups.isEmpty()) {
        val allGroups = PredefinedMuscleGroups.getAllMuscleGroups()
        muscleGroupDao().insertMuscleGroups(allGroups)
    }
}