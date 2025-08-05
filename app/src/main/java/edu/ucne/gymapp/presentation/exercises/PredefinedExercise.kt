package edu.ucne.gymapp.presentation.exercises
import edu.ucne.gymapp.data.local.entities.Exercise

object PredefinedExercises {

    fun getAll(): List<Exercise> = listOf(
        Exercise(
            exerciseId = 1,
            name = "Press de Banca",
            description = "Ejercicio fundamental para el desarrollo del pecho, hombros y tríceps",
            instructions = "Acuéstate en el banco, agarra la barra con las manos separadas, baja controladamente hasta el pecho y empuja hacia arriba",
            muscleGroupId = 1,
            difficulty = "Intermedio",
            equipment = "Barra, Banco, Discos",
            popularity = 0
        ),
        Exercise(
            exerciseId = 2,
            name = "Flexiones de Pecho",
            description = "Ejercicio básico con peso corporal para fortalecer pecho, hombros y tríceps",
            instructions = "Colócate en posición de plancha, baja el cuerpo hasta casi tocar el suelo y empuja hacia arriba",
            muscleGroupId = 1,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 3,
            name = "Press Inclinado con Mancuernas",
            description = "Trabaja la parte superior del pecho de manera efectiva",
            instructions = "En banco inclinado a 30-45°, empuja las mancuernas desde el pecho hacia arriba en un arco controlado",
            muscleGroupId = 1,
            difficulty = "Intermedio",
            equipment = "Mancuernas, Banco inclinado",
            popularity = 0
        ),
        Exercise(
            exerciseId = 4,
            name = "Aperturas con Mancuernas",
            description = "Aísla el pectoral mayor abriendo los brazos desde una posición extendida.",
            instructions = "Acostado en banco plano, abre los brazos lateralmente y regresa controlado.",
            muscleGroupId = 1,
            difficulty = "Intermedio",
            equipment = "Mancuernas, Banco plano",
            popularity = 0
        ),
        Exercise(
            exerciseId = 5,
            name = "Press Declinado",
            description = "Trabaja la parte inferior del pecho.",
            instructions = "En banco declinado, baja la barra al pecho y empuja hacia arriba.",
            muscleGroupId = 1,
            difficulty = "Intermedio",
            equipment = "Barra, Banco declinado",
            popularity = 0
        ),

        Exercise(
            exerciseId = 6,
            name = "Dominadas",
            description = "Ejercicio excelente para desarrollar la espalda y bíceps",
            instructions = "Cuelga de la barra con agarre pronado, tira del cuerpo hacia arriba hasta que la barbilla supere la barra",
            muscleGroupId = 2,
            difficulty = "Avanzado",
            equipment = "Barra de dominadas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 7,
            name = "Remo con Barra",
            description = "Fortalece toda la musculatura de la espalda",
            instructions = "Inclínate hacia adelante, mantén la espalda recta y tira de la barra hacia el abdomen",
            muscleGroupId = 2,
            difficulty = "Intermedio",
            equipment = "Barra, Discos",
            popularity = 0
        ),
        Exercise(
            exerciseId = 8,
            name = "Remo con Mancuernas",
            description = "Trabaja la parte media de la espalda.",
            instructions = "Con una mancuerna en cada mano, inclínate y rema hacia el torso.",
            muscleGroupId = 2,
            difficulty = "Intermedio",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 9,
            name = "Pull Over",
            description = "Ejercicio para expandir la caja torácica y trabajar dorsales.",
            instructions = "Acostado, lleva una mancuerna desde el pecho hacia atrás de la cabeza y regresa.",
            muscleGroupId = 2,
            difficulty = "Intermedio",
            equipment = "Mancuerna, Banco",
            popularity = 0
        ),

        Exercise(
            exerciseId = 10,
            name = "Sentadillas",
            description = "Rey de los ejercicios para piernas, trabaja cuádriceps, glúteos y core",
            instructions = "Baja como si te fueras a sentar, mantén el pecho erguido, baja hasta que los muslos estén paralelos al suelo",
            muscleGroupId = 3,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 11,
            name = "Sentadilla con Barra",
            description = "Versión con peso de las sentadillas tradicionales",
            instructions = "Coloca la barra en los trapecios, baja controladamente y empuja con los talones para subir",
            muscleGroupId = 3,
            difficulty = "Intermedio",
            equipment = "Barra, Discos, Rack",
            popularity = 0
        ),
        Exercise(
            exerciseId = 12,
            name = "Prensa de Piernas",
            description = "Enfoca cuádriceps, glúteos y femorales.",
            instructions = "Empuja la plataforma con las piernas, manteniendo el control.",
            muscleGroupId = 3,
            difficulty = "Intermedio",
            equipment = "Prensa de piernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 13,
            name = "Zancadas",
            description = "Excelente ejercicio unilateral para piernas.",
            instructions = "Da un paso hacia adelante y baja hasta que ambas rodillas estén a 90°.",
            muscleGroupId = 3,
            difficulty = "Principiante",
            equipment = "Opcional: mancuernas",
            popularity = 0
        ),

        Exercise(
            exerciseId = 14,
            name = "Press Militar",
            description = "Desarrolla la fuerza y masa de los hombros",
            instructions = "De pie, empuja la barra desde los hombros hacia arriba, mantén el core activado",
            muscleGroupId = 4,
            difficulty = "Intermedio",
            equipment = "Barra, Discos",
            popularity = 0
        ),
        Exercise(
            exerciseId = 15,
            name = "Elevaciones Laterales",
            description = "Aísla los deltoides medios para amplitud de hombros",
            instructions = "Levanta las mancuernas hacia los lados hasta la altura de los hombros, baja controladamente",
            muscleGroupId = 4,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),

        Exercise(
            exerciseId = 16,
            name = "Curl de Bíceps",
            description = "Ejercicio básico para desarrollar los bíceps",
            instructions = "Mantén los codos pegados al cuerpo, levanta las mancuernas flexionando únicamente los antebrazos",
            muscleGroupId = 5,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),

        Exercise(
            exerciseId = 17,
            name = "Plancha",
            description = "Fortalece todo el core y mejora la estabilidad",
            instructions = "Mantén el cuerpo recto como una tabla, apoya en antebrazos y pies, contrae el abdomen",
            muscleGroupId = 6,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 18,
            name = "Abdominales Crunch",
            description = "Ejercicio básico para fortalecer el recto abdominal",
            instructions = "Acostado, manos detrás de la cabeza, levanta el torso contrayendo el abdomen",
            muscleGroupId = 6,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),

        Exercise(
            exerciseId = 19,
            name = "Burpees",
            description = "Ejercicio de cuerpo completo que combina fuerza y cardio",
            instructions = "Desde de pie, baja a posición de flexión, haz una flexión, salta los pies hacia las manos, salta hacia arriba",
            muscleGroupId = 7,
            difficulty = "Avanzado",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 20,
            name = "Jumping Jacks",
            description = "Ejercicio cardiovascular básico y efectivo",
            instructions = "Salta separando piernas y brazos al mismo tiempo, vuelve a la posición inicial",
            muscleGroupId = 7,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        )
    )
}