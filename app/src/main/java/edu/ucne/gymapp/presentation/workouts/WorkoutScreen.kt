package edu.ucne.gymapp.presentation.workouts

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.ui.theme.RetrofitColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        onEvent(WorkoutEvent.LoadAvailableRoutines(state.userId))
        onEvent(WorkoutEvent.LoadRecentWorkouts)
    }

    LaunchedEffect(state.isWorkoutActive, state.isPaused) {
        if (state.isWorkoutActive && !state.isPaused) {
            while (true) {
                delay(1000)
                onEvent(WorkoutEvent.UpdateWorkoutTimer)
            }
        }
    }

    LaunchedEffect(state.isResting, state.restTimer) {
        if (state.isResting && state.restTimer > 0) {
            while (state.restTimer > 0) {
                delay(1000)
                onEvent(WorkoutEvent.UpdateRestTimer)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        RetrofitColors.Background,
                        RetrofitColors.Background.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        when (state.currentScreen) {
            WorkoutScreen.DASHBOARD -> {
                DashboardScreen(
                    state = state,
                    onEvent = onEvent,
                    onNavigateBack = onNavigateBack
                )
            }
            WorkoutScreen.ROUTINE_SELECTOR -> {
                RoutineSelectorScreen(
                    state = state,
                    onEvent = onEvent
                )
            }
            WorkoutScreen.ACTIVE_WORKOUT -> {
                ActiveWorkoutScreen(
                    state = state,
                    onEvent = onEvent
                )
            }
            WorkoutScreen.REST_SCREEN -> {
                RestScreen(
                    state = state,
                    onEvent = onEvent
                )
            }
            WorkoutScreen.WORKOUT_COMPLETE -> {
                WorkoutCompleteScreen(
                    state = state,
                    onEvent = onEvent
                )
            }
            WorkoutScreen.WORKOUT_HISTORY -> {
                WorkoutHistoryScreen(
                    state = state,
                    onEvent = onEvent
                )
            }
        }

        AnimatedVisibility(
            visible = state.showMotivationDialog,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            MotivationDialog(
                message = state.motivationMessage,
                onDismiss = { onEvent(WorkoutEvent.DismissDialogs) }
            )
        }

        AnimatedVisibility(
            visible = state.showCompletionCelebration,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            CelebrationDialog(
                onDismiss = { onEvent(WorkoutEvent.DismissDialogs) }
            )
        }

        state.errorMessage?.let { error ->
            LaunchedEffect(error) {
                delay(3000)
                onEvent(WorkoutEvent.ClearMessages)
            }

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = error,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        state.successMessage?.let { success ->
            LaunchedEffect(success) {
                delay(3000)
                onEvent(WorkoutEvent.ClearMessages)
            }

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = success,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun RoutineSelectorScreen(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEvent(WorkoutEvent.BackToDashboard) },
                modifier = Modifier
                    .background(
                        color = RetrofitColors.Surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = RetrofitColors.onSurface
                )
            }

            Text(
                text = "ELEGIR RUTINA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface
            )

            Box(modifier = Modifier.size(48.dp))
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = RetrofitColors.Primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.availableRoutines) { routine ->
                    RoutineCard(
                        routine = routine,
                        onClick = { onEvent(WorkoutEvent.StartWorkoutWithRoutine(routine)) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoutineCard(
    routine: edu.ucne.gymapp.data.local.entities.Routine,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(150)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = routine.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface
                    )
                    if (routine.description.isNotEmpty()) {
                        Text(
                            text = routine.description,
                            fontSize = 14.sp,
                            color = RetrofitColors.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                DifficultyChip(difficulty = routine.difficulty)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoChip(
                    icon = Icons.Default.Schedule,
                    text = "${routine.estimatedDuration} min"
                )
                InfoChip(
                    icon = Icons.Default.FitnessCenter,
                    text = routine.targetMuscleGroups
                )
            }

            if (routine.isActive) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = RetrofitColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Rutina activa",
                        fontSize = 12.sp,
                        color = RetrofitColors.Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyChip(difficulty: String) {
    val (backgroundColor, textColor) = when (difficulty) {
        "Principiante" -> Pair(Color.Green.copy(alpha = 0.2f), Color.Green)
        "Intermedio" -> Pair(Color.Yellow.copy(alpha = 0.2f), Color.Yellow.copy(red = 0.8f))
        "Avanzado" -> Pair(Color.Red.copy(alpha = 0.2f), Color.Red)
        else -> Pair(RetrofitColors.Gray.copy(alpha = 0.2f), RetrofitColors.Gray)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = difficulty,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                RetrofitColors.Primary.copy(alpha = 0.1f),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RetrofitColors.Primary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = RetrofitColors.Gray
        )
    }
}

@Composable
fun ActiveWorkoutScreen(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WorkoutHeader(
            routineName = state.selectedRoutine?.name ?: "Entrenamiento Libre",
            duration = state.workoutTimer,
            isPaused = state.isPaused,
            onPause = { onEvent(WorkoutEvent.PauseWorkout(state.currentWorkout?.workoutId ?: 0)) },
            onStop = { onEvent(WorkoutEvent.CancelWorkout(state.currentWorkout?.workoutId ?: 0)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.selectedRoutine != null && state.exercises.isNotEmpty()) {
            ExerciseProgressSection(
                state = state,
                onEvent = onEvent
            )
        } else {
            FreeWorkoutSection(
                duration = state.workoutTimer,
                onFinish = { onEvent(WorkoutEvent.FinishWorkout(state.currentWorkout?.workoutId ?: 0)) }
            )
        }
    }
}

@Composable
fun WorkoutHeader(
    routineName: String,
    duration: Long,
    isPaused: Boolean,
    onPause: () -> Unit,
    onStop: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ENTRENANDO",
                    fontSize = 12.sp,
                    color = RetrofitColors.Primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = routineName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )
            }

            Icon(
                imageVector = if (isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = if (isPaused) Color.Yellow else RetrofitColors.Primary,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatDuration(duration.toInt()),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onPause,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaused) RetrofitColors.Primary else Color.DarkGray,
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isPaused) "Continuar" else "Pausar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = onStop,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Finalizar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ExerciseProgressSection(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit
) {
    if (state.exercises.isNotEmpty() && state.currentExerciseIndex < state.exercises.size) {
        val currentExercise = state.exercises[state.currentExerciseIndex]
        val routineExercise = state.routineExercises.find { it.exerciseId == currentExercise.exerciseId }

        CurrentExerciseCard(
            exercise = currentExercise,
            routineExercise = routineExercise,
            currentSet = state.currentSet,
            isResting = state.isResting,
            restTimer = state.restTimer,
            onCompleteSet = { onEvent(WorkoutEvent.CompleteSet(state.currentSet)) },
            onSkipRest = { onEvent(WorkoutEvent.SkipRest) },
            onNextExercise = { onEvent(WorkoutEvent.NextExercise) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExerciseProgressIndicator(
            currentExercise = state.currentExerciseIndex + 1,
            totalExercises = state.exercises.size
        )
    }
}

@Composable
fun CurrentExerciseCard(
    exercise: edu.ucne.gymapp.data.local.entities.Exercise,
    routineExercise: edu.ucne.gymapp.data.local.entities.RoutineExercise?,
    currentSet: Int,
    isResting: Boolean,
    restTimer: Long,
    onCompleteSet: () -> Unit,
    onSkipRest: () -> Unit,
    onNextExercise: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface
            )

            if (exercise.description.isNotEmpty()) {
                Text(
                    text = exercise.description,
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SetInfoCard(
                    title = "Serie",
                    value = "$currentSet/${routineExercise?.sets ?: 3}",
                    modifier = Modifier.weight(1f)
                )
                SetInfoCard(
                    title = "Reps",
                    value = routineExercise?.reps ?: "10",
                    modifier = Modifier.weight(1f)
                )
                routineExercise?.weight?.let { weight ->
                    SetInfoCard(
                        title = "Peso",
                        value = "${weight}kg",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isResting) {
                RestTimerCard(
                    timeLeft = restTimer,
                    onSkip = onSkipRest
                )
            } else {
                Button(
                    onClick = onCompleteSet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Completar Serie",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SetInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Background
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = RetrofitColors.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface
            )
        }
    }
}

@Composable
fun RestTimerCard(
    timeLeft: Long,
    onSkip: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Yellow.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DESCANSANDO",
                fontSize = 14.sp,
                color = Color.Yellow.copy(red = 0.8f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatDuration(timeLeft.toInt()),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSkip,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow.copy(red = 0.8f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Saltar Descanso",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ExerciseProgressIndicator(
    currentExercise: Int,
    totalExercises: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Progreso del entrenamiento",
                fontSize = 14.sp,
                color = RetrofitColors.Gray
            )
            Text(
                text = "$currentExercise / $totalExercises ejercicios",
                fontSize = 14.sp,
                color = RetrofitColors.onSurface,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = currentExercise.toFloat() / totalExercises.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = RetrofitColors.Primary,
            trackColor = RetrofitColors.Gray.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun FreeWorkoutSection(
    duration: Long,
    onFinish: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = RetrofitColors.Primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Entrenamiento Libre",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tiempo transcurrido: ${formatDuration(duration.toInt())}",
                fontSize = 14.sp,
                color = RetrofitColors.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Finalizar Entrenamiento",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MotivationDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        title = {
            Text(
                text = "¡Sigue así!",
                color = RetrofitColors.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                color = RetrofitColors.Gray
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                )
            ) {
                Text("¡Vamos!", color = Color.White)
            }
        }
    )
}

@Composable
fun CelebrationDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        title = {
            Text(
                text = "🎉 ¡Entrenamiento Completado!",
                color = RetrofitColors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "¡Excelente trabajo! Has completado otro entrenamiento exitoso.",
                color = RetrofitColors.Gray,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                )
            ) {
                Text("¡Genial!", color = Color.White)
            }
        }
    )
}

@Composable
fun RestScreen(state: WorkoutUiState, onEvent: (WorkoutEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Rest Screen - En desarrollo", color = RetrofitColors.onSurface)
    }
}

@Composable
fun WorkoutCompleteScreen(state: WorkoutUiState, onEvent: (WorkoutEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Workout Complete Screen - En desarrollo", color = RetrofitColors.onSurface)
    }
}

@Composable
fun WorkoutHistoryScreen(state: WorkoutUiState, onEvent: (WorkoutEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Workout History Screen - En desarrollo", color = RetrofitColors.onSurface)
    }
}

private fun formatDuration(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%d:%02d", minutes, secs)
    }
}

private fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val days = diff / (24 * 60 * 60 * 1000)

    return when {
        days == 0L -> "Hoy"
        days == 1L -> "Ayer"
        days < 7 -> "${days} días"
        else -> "${days / 7} semanas"
    }
}

@Composable
fun DashboardScreen(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(
                        color = RetrofitColors.Surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = RetrofitColors.onSurface
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ENTRENAMIENTOS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )
                Text(
                    text = "Supera tus límites",
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = RetrofitColors.Primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Entrenamientos",
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        QuickActionCards(onEvent = onEvent)

        Spacer(modifier = Modifier.height(32.dp))

        RecentWorkoutsSection(
            workouts = state.recentWorkouts,
            onEvent = onEvent
        )
    }
}

@Composable
fun QuickActionCards(onEvent: (WorkoutEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { onEvent(WorkoutEvent.ShowRoutineSelector) },
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Entrenar con Rutina",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface
                    )
                    Text(
                        text = "Elige una rutina y domina el día",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = RetrofitColors.Primary,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable { onEvent(WorkoutEvent.StartQuickWorkout) },
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Inicio Rápido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface
                    )
                    Text(
                        text = "Solo cronómetro, sin rutina",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = RetrofitColors.Primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = RetrofitColors.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecentWorkoutsSection(
    workouts: List<Workout>,
    onEvent: (WorkoutEvent) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Entrenamientos Recientes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (workouts.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = RetrofitColors.Surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = RetrofitColors.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Comienza tu primer entrenamiento!",
                        fontSize = 16.sp,
                        color = RetrofitColors.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workouts) { workout ->
                    WorkoutHistoryCard(
                        workout = workout,
                        onClick = { onEvent(WorkoutEvent.SelectWorkout(workout)) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard(
    workout: Workout,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = RetrofitColors.Primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetrofitColors.onSurface
                )
                Text(
                    text = formatDate(workout.startTime),
                    fontSize = 12.sp,
                    color = RetrofitColors.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatDuration(workout.totalDuration),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetrofitColors.onSurface
                )
            }
        }
    }
}