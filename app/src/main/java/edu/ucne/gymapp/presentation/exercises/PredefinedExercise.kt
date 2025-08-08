package edu.ucne.gymapp.presentation.exercises
import edu.ucne.gymapp.data.local.entities.Exercise

object PredefinedExercises {

    private const val EQUIP_BANDA_ELASTICA = "Banda elástica"
    private const val EQUIP_BARRA_DISCOS = "Barra, Discos"
    private const val EQUIP_OPCIONAL_MANCUERNAS = "Opcional: mancuernas"
    private const val EQUIP_RESISTENCIA_MANUAL = "Resistencia manual"

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
            description = "Aísla el pectoral mayor abriendo los brazos desde una posición extendida",
            instructions = "Acostado en banco plano, abre los brazos lateralmente y regresa controlado",
            muscleGroupId = 1,
            difficulty = "Intermedio",
            equipment = "Mancuernas, Banco plano",
            popularity = 0
        ),
        Exercise(
            exerciseId = 5,
            name = "Press Declinado",
            description = "Trabaja la parte inferior del pecho",
            instructions = "En banco declinado, baja la barra al pecho y empuja hacia arriba",
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
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 8,
            name = "Remo con Mancuernas",
            description = "Trabaja la parte media de la espalda",
            instructions = "Con una mancuerna en cada mano, inclínate y rema hacia el torso",
            muscleGroupId = 2,
            difficulty = "Intermedio",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 9,
            name = "Pull Over",
            description = "Ejercicio para expandir la caja torácica y trabajar dorsales",
            instructions = "Acostado, lleva una mancuerna desde el pecho hacia atrás de la cabeza y regresa",
            muscleGroupId = 2,
            difficulty = "Intermedio",
            equipment = "Mancuerna, Banco",
            popularity = 0
        ),
        Exercise(
            exerciseId = 10,
            name = "Peso Muerto",
            description = "Ejercicio compuesto que trabaja toda la cadena posterior",
            instructions = "Levanta la barra desde el suelo manteniendo la espalda recta, empuja con las caderas",
            muscleGroupId = 2,
            difficulty = "Avanzado",
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 11,
            name = "Press Militar",
            description = "Desarrolla la fuerza y masa de los hombros",
            instructions = "De pie, empuja la barra desde los hombros hacia arriba, mantén el core activado",
            muscleGroupId = 3,
            difficulty = "Intermedio",
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 12,
            name = "Elevaciones Laterales",
            description = "Aísla los deltoides medios para amplitud de hombros",
            instructions = "Levanta las mancuernas hacia los lados hasta la altura de los hombros, baja controladamente",
            muscleGroupId = 3,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 13,
            name = "Press con Mancuernas",
            description = "Desarrollo completo de los deltoides con mayor rango de movimiento",
            instructions = "Sentado o de pie, empuja las mancuernas desde los hombros hacia arriba",
            muscleGroupId = 3,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 14,
            name = "Elevaciones Posteriores",
            description = "Trabaja la parte posterior del deltoides y trapecio medio",
            instructions = "Inclínate hacia adelante, eleva las mancuernas hacia los lados apretando los omóplatos",
            muscleGroupId = 3,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 15,
            name = "Elevaciones Frontales",
            description = "Aísla la porción anterior del deltoides",
            instructions = "Alterna o simultáneamente eleva las mancuernas al frente hasta la altura del hombro",
            muscleGroupId = 3,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),

        Exercise(
            exerciseId = 16,
            name = "Curl de Bíceps",
            description = "Ejercicio básico para desarrollar los bíceps",
            instructions = "Mantén los codos pegados al cuerpo, levanta las mancuernas flexionando únicamente los antebrazos",
            muscleGroupId = 4,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 17,
            name = "Curl con Barra",
            description = "Versión con barra del curl de bíceps para mayor carga",
            instructions = "De pie, curl la barra desde extensión completa hasta contracción máxima",
            muscleGroupId = 4,
            difficulty = "Principiante",
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 18,
            name = "Curl Martillo",
            description = "Trabaja bíceps y antebrazos con agarre neutro",
            instructions = "Curl las mancuernas manteniendo el agarre neutro (palmas enfrentadas)",
            muscleGroupId = 4,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 19,
            name = "Curl Concentrado",
            description = "Ejercicio de aislamiento máximo para el bíceps",
            instructions = "Sentado, apoya el codo en el muslo y curl la mancuerna con máximo control",
            muscleGroupId = 4,
            difficulty = "Principiante",
            equipment = "Mancuerna",
            popularity = 0
        ),
        Exercise(
            exerciseId = 20,
            name = "Curl en Predicador",
            description = "Aislamiento del bíceps eliminando el impulso",
            instructions = "En banco predicador, curl controlado enfocando la contracción",
            muscleGroupId = 4,
            difficulty = "Intermedio",
            equipment = "Banco predicador, barra EZ",
            popularity = 0
        ),

        Exercise(
            exerciseId = 21,
            name = "Press Francés",
            description = "Ejercicio de aislamiento para el desarrollo de los tríceps",
            instructions = "Acostado, baja la barra hacia la frente flexionando solo los codos, extiende hacia arriba",
            muscleGroupId = 5,
            difficulty = "Intermedio",
            equipment = "Barra EZ, Banco",
            popularity = 0
        ),
        Exercise(
            exerciseId = 22,
            name = "Fondos en Paralelas",
            description = "Ejercicio compuesto para tríceps, pecho y deltoides anterior",
            instructions = "Baja el cuerpo flexionando los codos, empuja hacia arriba hasta extensión completa",
            muscleGroupId = 5,
            difficulty = "Intermedio",
            equipment = "Barras paralelas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 23,
            name = "Extensiones con Mancuerna",
            description = "Aislamiento de tríceps con mancuerna tras la cabeza",
            instructions = "Sentado o de pie, baja la mancuerna tras la cabeza y extiende",
            muscleGroupId = 5,
            difficulty = "Principiante",
            equipment = "Mancuerna",
            popularity = 0
        ),
        Exercise(
            exerciseId = 24,
            name = "Patadas de Tríceps",
            description = "Ejercicio de aislamiento con extensión hacia atrás",
            instructions = "Inclínate hacia adelante, extiende el brazo hacia atrás contrayendo el tríceps",
            muscleGroupId = 5,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 25,
            name = "Press de Banca Agarre Cerrado",
            description = "Variación del press que enfatiza los tríceps",
            instructions = "Press de banca con las manos separadas al ancho de los hombros",
            muscleGroupId = 5,
            difficulty = "Intermedio",
            equipment = "Barra, banco",
            popularity = 0
        ),
        Exercise(
            exerciseId = 26,
            name = "Curl de Muñeca",
            description = "Fortalecimiento de flexores de muñeca y antebrazos",
            instructions = "Sentado, antebrazos en los muslos, flexiona las muñecas hacia arriba",
            muscleGroupId = 6,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 27,
            name = "Curl Invertido",
            description = "Trabaja extensores de muñeca y antebrazos",
            instructions = "Curl con agarre pronado, enfocando el trabajo en antebrazos",
            muscleGroupId = 6,
            difficulty = "Principiante",
            equipment = "Barra o mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 28,
            name = "Farmer's Walk",
            description = "Caminata con peso para fuerza de agarre",
            instructions = "Camina una distancia determinada cargando pesos pesados",
            muscleGroupId = 6,
            difficulty = "Intermedio",
            equipment = "Mancuernas pesadas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 29,
            name = "Extensión de Muñeca",
            description = "Fortalece los extensores de la muñeca",
            instructions = "Antebrazos apoyados, extiende las muñecas hacia arriba",
            muscleGroupId = 6,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 30,
            name = "Plancha",
            description = "Fortalece todo el core y mejora la estabilidad",
            instructions = "Mantén el cuerpo recto como una tabla, apoya en antebrazos y pies, contrae el abdomen",
            muscleGroupId = 7,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 31,
            name = "Abdominales Crunch",
            description = "Ejercicio básico para fortalecer el recto abdominal",
            instructions = "Acostado, manos detrás de la cabeza, levanta el torso contrayendo el abdomen",
            muscleGroupId = 7,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 32,
            name = "Elevación de Piernas",
            description = "Trabaja la porción inferior del recto abdominal",
            instructions = "Acostado, eleva las piernas rectas hasta formar 90° con el torso",
            muscleGroupId = 7,
            difficulty = "Intermedio",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 33,
            name = "Abdominales Bicicleta",
            description = "Ejercicio dinámico que trabaja oblicuos y recto abdominal",
            instructions = "Alterna llevando el codo hacia la rodilla opuesta en movimiento de pedaleo",
            muscleGroupId = 7,
            difficulty = "Intermedio",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 34,
            name = "Plancha Lateral",
            description = "Fortalece los oblicuos y mejora la estabilidad lateral",
            instructions = "De lado, apóyate en un antebrazo y mantén el cuerpo recto",
            muscleGroupId = 7,
            difficulty = "Intermedio",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 35,
            name = "Sentadillas",
            description = "Rey de los ejercicios para piernas, trabaja cuádriceps, glúteos y core",
            instructions = "Baja como si te fueras a sentar, mantén el pecho erguido, baja hasta que los muslos estén paralelos al suelo",
            muscleGroupId = 8,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 36,
            name = "Sentadilla con Barra",
            description = "Versión con peso de las sentadillas tradicionales",
            instructions = "Coloca la barra en los trapecios, baja controladamente y empuja con los talones para subir",
            muscleGroupId = 8,
            difficulty = "Intermedio",
            equipment = "$EQUIP_BARRA_DISCOS, Rack",
            popularity = 0
        ),
        Exercise(
            exerciseId = 37,
            name = "Prensa de Piernas",
            description = "Enfoca cuádriceps, glúteos y femorales",
            instructions = "Empuja la plataforma con las piernas, manteniendo el control",
            muscleGroupId = 8,
            difficulty = "Intermedio",
            equipment = "Prensa de piernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 38,
            name = "Zancadas",
            description = "Excelente ejercicio unilateral para piernas",
            instructions = "Da un paso hacia adelante y baja hasta que ambas rodillas estén a 90°",
            muscleGroupId = 8,
            difficulty = "Principiante",
            equipment = EQUIP_OPCIONAL_MANCUERNAS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 39,
            name = "Extensiones de Cuádriceps",
            description = "Aislamiento específico del cuádriceps en máquina",
            instructions = "Sentado en la máquina, extiende las piernas completamente y baja controladamente",
            muscleGroupId = 8,
            difficulty = "Principiante",
            equipment = "Máquina de extensiones",
            popularity = 0
        ),
        Exercise(
            exerciseId = 40,
            name = "Peso Muerto Rumano",
            description = "Enfoca isquiotibiales y glúteos con movimiento de bisagra de cadera",
            instructions = "Mantén las piernas semiflexionadas, baja la barra empujando las caderas hacia atrás",
            muscleGroupId = 9,
            difficulty = "Intermedio",
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 41,
            name = "Curl Femoral Acostado",
            description = "Aislamiento de isquiotibiales en máquina",
            instructions = "Boca abajo, flexiona las piernas llevando los talones hacia los glúteos",
            muscleGroupId = 9,
            difficulty = "Principiante",
            equipment = "Máquina de curl femoral",
            popularity = 0
        ),
        Exercise(
            exerciseId = 42,
            name = "Peso Muerto con Piernas Rígidas",
            description = "Enfoca isquiotibiales con mínima flexión de rodillas",
            instructions = "Piernas casi rectas, baja la barra empujando las caderas hacia atrás",
            muscleGroupId = 9,
            difficulty = "Intermedio",
            equipment = "$EQUIP_BARRA_DISCOS",
            popularity = 0
        ),
        Exercise(
            exerciseId = 43,
            name = "Curl Nórdico",
            description = "Ejercicio avanzado excéntrico para isquiotibiales",
            instructions = "Arrodillado, baja el cuerpo controladamente usando solo los isquiotibiales",
            muscleGroupId = 9,
            difficulty = "Avanzado",
            equipment = "Superficie para anclar pies",
            popularity = 0
        ),
        Exercise(
            exerciseId = 44,
            name = "Good Mornings",
            description = "Fortalece isquiotibiales y espalda baja",
            instructions = "Barra en los hombros, inclínate hacia adelante desde las caderas",
            muscleGroupId = 9,
            difficulty = "Intermedio",
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 45,
            name = "Hip Thrust",
            description = "Ejercicio específico para activación máxima de glúteos",
            instructions = "Espalda apoyada en banco, empuja las caderas hacia arriba",
            muscleGroupId = 10,
            difficulty = "Intermedio",
            equipment = "Banco, barra opcional",
            popularity = 0
        ),
        Exercise(
            exerciseId = 46,
            name = "Patadas de Glúteo",
            description = "Aislamiento del glúteo mayor en cuadrupedia",
            instructions = "En cuatro patas, extiende una pierna hacia atrás contrayendo el glúteo",
            muscleGroupId = 10,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 47,
            name = "Caminata Lateral con Banda",
            description = "Activa glúteo medio y menor",
            instructions = "Con banda en las piernas, camina lateralmente manteniendo tensión",
            muscleGroupId = 10,
            difficulty = "Principiante",
            equipment = EQUIP_BANDA_ELASTICA,
            popularity = 0
        ),
        Exercise(
            exerciseId = 48,
            name = "Puente de Glúteo",
            description = "Ejercicio básico para activación de glúteos",
            instructions = "Acostado, eleva las caderas contrayendo los glúteos",
            muscleGroupId = 10,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 49,
            name = "Sentadilla Sumo",
            description = "Variación que enfatiza glúteos y aductores",
            instructions = "Pies muy separados, sentadilla profunda",
            muscleGroupId = 10,
            difficulty = "Intermedio",
            equipment = "Opcional: mancuerna o kettlebell",
            popularity = 0
        ),
        Exercise(
            exerciseId = 50,
            name = "Elevaciones de Pantorrillas de Pie",
            description = "Ejercicio básico para gastrocnemio",
            instructions = "De pie, elévate sobre las puntas de los pies y baja controladamente",
            muscleGroupId = 11,
            difficulty = "Principiante",
            equipment = EQUIP_OPCIONAL_MANCUERNAS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 51,
            name = "Elevaciones de Pantorrillas Sentado",
            description = "Enfoca el músculo sóleo",
            instructions = "Sentado, eleva los talones contrayendo las pantorrillas",
            muscleGroupId = 11,
            difficulty = "Principiante",
            equipment = "Máquina o mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 52,
            name = "Elevaciones en Prensa",
            description = "Pantorrillas en máquina de prensa",
            instructions = "En la prensa, coloca los pies en la parte baja y eleva con pantorrillas",
            muscleGroupId = 11,
            difficulty = "Intermedio",
            equipment = "Prensa de piernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 53,
            name = "Saltos de Pantorrilla",
            description = "Ejercicio pliométrico para potencia",
            instructions = "Salta usando solo la fuerza de las pantorrillas",
            muscleGroupId = 11,
            difficulty = "Intermedio",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 54,
            name = "Aducción en Máquina",
            description = "Aislamiento de aductores en máquina",
            instructions = "Sentado, junta las piernas venciendo la resistencia",
            muscleGroupId = 12,
            difficulty = "Principiante",
            equipment = "Máquina de aductores",
            popularity = 0
        ),
        Exercise(
            exerciseId = 55,
            name = "Sentadilla con Pelota",
            description = "Fortalece aductores isométricamente",
            instructions = "Sentadilla manteniendo una pelota entre las rodillas",
            muscleGroupId = 12,
            difficulty = "Principiante",
            equipment = "Pelota pequeña",
            popularity = 0
        ),
        Exercise(
            exerciseId = 56,
            name = "Zancadas Laterales",
            description = "Trabaja aductores dinámicamente",
            instructions = "Da un paso lateral amplio, baja y regresa al centro",
            muscleGroupId = 12,
            difficulty = "Principiante",
            equipment = EQUIP_OPCIONAL_MANCUERNAS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 57,
            name = "Cossack Squats",
            description = "Sentadilla lateral profunda",
            instructions = "Sentadilla hacia un lado manteniendo la otra pierna recta",
            muscleGroupId = 12,
            difficulty = "Intermedio",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 58,
            name = "Abducción en Máquina",
            description = "Aislamiento de abductores externos",
            instructions = "Sentado, separa las piernas venciendo la resistencia",
            muscleGroupId = 13,
            difficulty = "Principiante",
            equipment = "Máquina de abductores",
            popularity = 0
        ),
        Exercise(
            exerciseId = 59,
            name = "Elevaciones Laterales de Pierna",
            description = "Abducción acostado de lado",
            instructions = "Acostado de lado, eleva la pierna superior lateralmente",
            muscleGroupId = 13,
            difficulty = "Principiante",
            equipment = "Opcional: tobilleras con peso",
            popularity = 0
        ),
        Exercise(
            exerciseId = 60,
            name = "Clamshells",
            description = "Fortalecimiento de glúteo medio",
            instructions = "Acostado de lado, abre y cierra las rodillas como una almeja",
            muscleGroupId = 13,
            difficulty = "Principiante",
            equipment = "Opcional: $EQUIP_BANDA_ELASTICA",
            popularity = 0
        ),
        Exercise(
            exerciseId = 61,
            name = "Pasos Laterales con Banda",
            description = "Abductores con resistencia elástica",
            instructions = "Con banda en tobillos, da pasos laterales manteniendo tensión",
            muscleGroupId = 13,
            difficulty = "Principiante",
            equipment = EQUIP_BANDA_ELASTICA,
            popularity = 0
        ),
        Exercise(
            exerciseId = 62,
            name = "Flexión de Cuello con Resistencia",
            description = "Fortalecimiento de flexores del cuello",
            instructions = "Con resistencia manual o peso, flexiona el cuello hacia adelante",
            muscleGroupId = 14,
            difficulty = "Principiante",
            equipment = "$EQUIP_RESISTENCIA_MANUAL o disco",
            popularity = 0
        ),
        Exercise(
            exerciseId = 63,
            name = "Extensión de Cuello",
            description = "Fortalece los extensores cervicales",
            instructions = "Con resistencia, extiende el cuello hacia atrás controladamente",
            muscleGroupId = 14,
            difficulty = "Principiante",
            equipment = EQUIP_RESISTENCIA_MANUAL,
            popularity = 0
        ),
        Exercise(
            exerciseId = 64,
            name = "Rotación de Cuello con Resistencia",
            description = "Fortalecimiento rotacional del cuello",
            instructions = "Rota el cuello contra resistencia en ambas direcciones",
            muscleGroupId = 14,
            difficulty = "Principiante",
            equipment = EQUIP_RESISTENCIA_MANUAL,
            popularity = 0
        ),
        Exercise(
            exerciseId = 65,
            name = "Flexión Lateral de Cuello",
            description = "Trabajo de flexores laterales cervicales",
            instructions = "Inclina la cabeza hacia el lado contra resistencia",
            muscleGroupId = 14,
            difficulty = "Principiante",
            equipment = EQUIP_RESISTENCIA_MANUAL,
            popularity = 0
        ),
        Exercise(
            exerciseId = 66,
            name = "Jalones al Pecho",
            description = "Desarrollo de dorsales con agarre amplio",
            instructions = "Tira de la barra hacia el pecho con agarre amplio",
            muscleGroupId = 15,
            difficulty = "Intermedio",
            equipment = "Máquina de jalones",
            popularity = 0
        ),
        Exercise(
            exerciseId = 67,
            name = "Remo en T",
            description = "Enfoca dorsales y parte media de la espalda",
            instructions = "Con barra en T, rema hacia el abdomen",
            muscleGroupId = 15,
            difficulty = "Intermedio",
            equipment = "Barra en T",
            popularity = 0
        ),
        Exercise(
            exerciseId = 68,
            name = "Dominadas con Agarre Amplio",
            description = "Máximo desarrollo de dorsales",
            instructions = "Dominadas con agarre más amplio que los hombros",
            muscleGroupId = 15,
            difficulty = "Intermedio",
            equipment = null,
            popularity = 0
        ),
        Exercise(
            exerciseId = 69,
            name = "Encogimientos con Mancuernas",
            description = "Ejercicio básico para desarrollar el trapecio superior",
            instructions = "De pie, sostén las mancuernas y encoge los hombros hacia las orejas, mantén y baja controladamente",
            muscleGroupId = 16,
            difficulty = "Principiante",
            equipment = "Mancuernas",
            popularity = 0
        ),
        Exercise(
            exerciseId = 70,
            name = "Remo al cuello con barra",
            description = "Trabaja la parte superior de la espalda y el trapecio",
            instructions = "De pie, tira de la barra hacia el cuello con los codos elevados",
            muscleGroupId = 16,
            difficulty = "Intermedio",
            equipment = EQUIP_BARRA_DISCOS,
            popularity = 0
        ),
        Exercise(
            exerciseId = 71,
            name = "Encogimientos con Barra detrás",
            description = "Variación que enfatiza el trapecio medio y posterior",
            instructions = "Barra detrás de la espalda, encoge los hombros hacia arriba y hacia atrás",
            muscleGroupId = 16,
            difficulty = "Intermedio",
            equipment = "Barra",
            popularity = 0
        ),
        Exercise(
            exerciseId = 72,
            name = "Remo al mentón con mancuerna",
            description = "Estimula el trapecio al elevar la mancuerna hacia el mentón",
            instructions = "Con una mancuerna, tira hacia arriba manteniendo los codos más altos que las manos",
            muscleGroupId = 16,
            difficulty = "Principiante",
            equipment = "Mancuerna",
            popularity = 0
        ),
        Exercise(
            exerciseId = 73,
            name = "Face Pull",
            description = "Fortalece la parte superior de la espalda y mejora la postura",
            instructions = "Con cuerda en polea alta, tira hacia el rostro separando las manos",
            muscleGroupId = 17,
            difficulty = "Intermedio",
            equipment = "Polea con cuerda",
            popularity = 0
        ),
        Exercise(
            exerciseId = 74,
            name = "Remo Alto con Banda Elástica",
            description = "Ejercicio con resistencia progresiva para la parte superior de la espalda",
            instructions = "Pisa la banda y rema hacia el pecho manteniendo codos altos",
            muscleGroupId = 17,
            difficulty = "Principiante",
            equipment = EQUIP_BANDA_ELASTICA,
            popularity = 0
        ),
        Exercise(
            exerciseId = 75,
            name = "Pájaros en Pec Deck",
            description = "Aisla el deltoides posterior y parte alta de la espalda",
            instructions = "En máquina pec deck, realiza el movimiento inverso al de aperturas de pecho",
            muscleGroupId = 17,
            difficulty = "Intermedio",
            equipment = "Máquina Pec Deck",
            popularity = 0
        ),
        Exercise(
            exerciseId = 76,
            name = "Remo Invertido",
            description = "Trabaja toda la espalda alta con peso corporal",
            instructions = "Bajo una barra fija, jala el pecho hacia la barra manteniendo el cuerpo recto",
            muscleGroupId = 17,
            difficulty = "Intermedio",
            equipment = "Barra fija baja",
            popularity = 0
        ),
        Exercise(
            exerciseId = 77,
            name = "Hiperextensiones",
            description = "Fortalece la región lumbar y mejora la postura",
            instructions = "En banco de hiperextensiones, baja el torso y súbelo lentamente",
            muscleGroupId = 18,
            difficulty = "Principiante",
            equipment = "Banco de hiperextensiones",
            popularity = 0
        ),
        Exercise(
            exerciseId = 78,
            name = "Buenos Días con Banda Elástica",
            description = "Ejercicio funcional para espalda baja sin peso libre",
            instructions = "Pisa la banda, colócala en los hombros e inclínate manteniendo la espalda recta",
            muscleGroupId = 18,
            difficulty = "Principiante",
            equipment = EQUIP_BANDA_ELASTICA,
            popularity = 0
        ),
        Exercise(
            exerciseId = 79,
            name = "Peso Muerto a una Pierna",
            description = "Mejora equilibrio y trabaja espalda baja de forma unilateral",
            instructions = "Inclínate con una pierna extendida atrás mientras bajas la pesa hacia el suelo",
            muscleGroupId = 18,
            difficulty = "Intermedio",
            equipment = "Mancuerna o pesa rusa",
            popularity = 0
        ),
        Exercise(
            exerciseId = 80,
            name = "Superman",
            description = "Ejercicio de peso corporal para la espalda baja",
            instructions = "Acostado boca abajo, eleva brazos y piernas simultáneamente manteniendo la contracción",
            muscleGroupId = 18,
            difficulty = "Principiante",
            equipment = null,
            popularity = 0
        )
    )
}
