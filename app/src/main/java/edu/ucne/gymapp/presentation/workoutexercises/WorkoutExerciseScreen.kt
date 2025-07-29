package edu.ucne.gymapp.presentation.workoutexercises

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.data.local.entities.WorkoutExercise
import edu.ucne.gymapp.ui.theme.RetrofitColors

@Composable
fun WorkoutExerciseScreen(
    state: WorkoutExerciseUiState,
    onEvent: (WorkoutExerciseEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExerciseSet: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var exerciseToDelete by remember { mutableStateOf<WorkoutExercise?>(null) }

    LaunchedEffect(Unit) {
        if (state.workoutId > 0) {
            onEvent(WorkoutExerciseEvent.LoadWorkoutExercisesByWorkout(state.workoutId))
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = RetrofitColors.onSurface
                    )
                }

                Text(
                    text = "EJERCICIOS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            RetrofitColors.Surface,
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = state.workoutProgress,
                        color = RetrofitColors.Primary,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${(state.workoutProgress * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface
                    )
                }
            }

            if (state.isExerciseActive && state.selectedWorkoutExercise != null) {
                ActiveExerciseCard(
                    workoutExercise = state.selectedWorkoutExercise,
                    currentSet = state.currentSet,
                    exerciseProgress = state.exerciseProgress,
                    onCompleteSet = { onEvent(WorkoutExerciseEvent.CompleteSet(state.selectedWorkoutExercise.workoutExerciseId)) },
                    onCompleteExercise = { onEvent(WorkoutExerciseEvent.CompleteExercise(state.selectedWorkoutExercise.workoutExerciseId)) },
                    onSkipExercise = { onEvent(WorkoutExerciseEvent.SkipExercise(state.selectedWorkoutExercise.workoutExerciseId)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.totalExercises > 1) {
                ExerciseNavigationCard(
                    currentIndex = state.currentExerciseIndex,
                    totalExercises = state.totalExercises,
                    isFirstExercise = state.isFirstExercise,
                    isLastExercise = state.isLastExercise,
                    onPrevious = { onEvent(WorkoutExerciseEvent.MoveToPreviousExercise) },
                    onNext = { onEvent(WorkoutExerciseEvent.MoveToNextExercise) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(listOf("Todos", "Pendientes", "En Progreso", "Completados")) { status ->
                    FilterChip(
                        onClick = {
                            when (status) {
                                "Todos" -> onEvent(WorkoutExerciseEvent.LoadWorkoutExercisesByWorkout(state.workoutId))
                                "Pendientes" -> onEvent(WorkoutExerciseEvent.LoadPendingWorkoutExercises)
                                "En Progreso" -> onEvent(WorkoutExerciseEvent.LoadActiveWorkoutExercises)
                                "Completados" -> onEvent(WorkoutExerciseEvent.LoadCompletedWorkoutExercises)
                            }
                        },
                        label = { Text(status) },
                        selected = false,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RetrofitColors.Surface,
                            labelColor = RetrofitColors.onSurface,
                            selectedContainerColor = RetrofitColors.Primary,
                            selectedLabelColor = RetrofitColors.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    items(state.workoutExercises) { exercise ->
                        WorkoutExerciseCard(
                            workoutExercise = exercise,
                            isSelected = state.selectedWorkoutExercise?.workoutExerciseId == exercise.exerciseId,
                            onSelect = { onEvent(WorkoutExerciseEvent.SelectWorkoutExercise(exercise)) },
                            onStart = { onEvent(WorkoutExerciseEvent.StartExercise(exercise.exerciseId)) },
                            onDelete = {
                                exerciseToDelete = exercise
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showDeleteDialog && exerciseToDelete != null) {
            DeleteExerciseConfirmationDialog(
                exerciseName = "Ejercicio #${exerciseToDelete!!.order}",
                onConfirm = {
                    onEvent(WorkoutExerciseEvent.SelectWorkoutExercise(exerciseToDelete!!))
                    onEvent(WorkoutExerciseEvent.DeleteWorkoutExercise)
                    showDeleteDialog = false
                    exerciseToDelete = null
                },
                onDismiss = {
                    showDeleteDialog = false
                    exerciseToDelete = null
                }
            )
        }

        state.errorMessage?.let { errorMsg ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red.copy(alpha = 0.9f)
                )
            ) {
                Text(
                    text = errorMsg,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        state.successMessage?.let { successMsg ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.9f)
                )
            ) {
                Text(
                    text = successMsg,
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
fun ActiveExerciseCard(
    workoutExercise: WorkoutExercise,
    currentSet: Int,
    exerciseProgress: Float,
    onCompleteSet: () -> Unit,
    onCompleteExercise: () -> Unit,
    onSkipExercise: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, RetrofitColors.Primary)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EJERCICIO ACTIVO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.Primary
                    )
                    Text(
                        text = "Ejercicio #${workoutExercise.order}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetrofitColors.onSurface
                    )
                }

                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Set $currentSet de ${workoutExercise.plannedSets}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                    Text(
                        text = "${(exerciseProgress * 100).toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.Primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = exerciseProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = RetrofitColors.Primary,
                    trackColor = RetrofitColors.Surface
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCompleteSet,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = currentSet <= workoutExercise.plannedSets
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Completar Set",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onCompleteExercise,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Finalizar",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = onSkipExercise,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.Yellow.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Saltar",
                        tint = Color.Yellow
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseNavigationCard(
    currentIndex: Int,
    totalExercises: Int,
    isFirstExercise: Boolean,
    isLastExercise: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevious,
                enabled = !isFirstExercise,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (!isFirstExercise) RetrofitColors.Primary.copy(alpha = 0.1f) else RetrofitColors.Gray.copy(alpha = 0.1f),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Anterior",
                    tint = if (!isFirstExercise) RetrofitColors.Primary else RetrofitColors.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ejercicio ${currentIndex + 1} de $totalExercises",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RetrofitColors.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / totalExercises.toFloat(),
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = RetrofitColors.Primary,
                    trackColor = RetrofitColors.Gray.copy(alpha = 0.3f)
                )
            }

            IconButton(
                onClick = onNext,
                enabled = !isLastExercise,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (!isLastExercise) RetrofitColors.Primary.copy(alpha = 0.1f) else RetrofitColors.Gray.copy(alpha = 0.1f),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Siguiente",
                    tint = if (!isLastExercise) RetrofitColors.Primary else RetrofitColors.Gray
                )
            }
        }
    }
}

@Composable
fun WorkoutExerciseCard(
    workoutExercise: WorkoutExercise,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onStart: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        2.dp,
                        RetrofitColors.Primary,
                        RoundedCornerShape(12.dp)
                    )
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) RetrofitColors.Primary.copy(alpha = 0.05f) else RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp)
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
                        text = "Ejercicio #${workoutExercise.order}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetrofitColors.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${workoutExercise.completedSets}/${workoutExercise.plannedSets} sets",
                        fontSize = 12.sp,
                        color = RetrofitColors.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExerciseStatusChip(status = workoutExercise.status)

                    Spacer(modifier = Modifier.width(8.dp))

                    if (workoutExercise.status == "PENDING") {
                        IconButton(
                            onClick = onStart,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Iniciar",
                                tint = Color.Green,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (workoutExercise.completedSets > 0) {
                Spacer(modifier = Modifier.height(8.dp))

                val progress = workoutExercise.completedSets.toFloat() / workoutExercise.plannedSets.toFloat()
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = RetrofitColors.Primary,
                    trackColor = RetrofitColors.Gray.copy(alpha = 0.3f)
                )
            }

            workoutExercise.notes?.let { notes ->
                if (notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = notes,
                        fontSize = 12.sp,
                        color = RetrofitColors.LightGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseStatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "COMPLETED" -> Pair(Color.Green.copy(alpha = 0.2f), Color.Green)
        "IN_PROGRESS" -> Pair(RetrofitColors.Primary.copy(alpha = 0.2f), RetrofitColors.Primary)
        "PENDING" -> Pair(Color.Yellow.copy(alpha = 0.2f), Color.Yellow)
        "SKIPPED" -> Pair(Color.Yellow.copy(alpha = 0.2f), Color.Yellow)
        else -> Pair(RetrofitColors.Gray.copy(alpha = 0.2f), RetrofitColors.Gray)
    }

    val displayText = when (status) {
        "COMPLETED" -> "Completado"
        "IN_PROGRESS" -> "En Progreso"
        "PENDING" -> "Pendiente"
        "SKIPPED" -> "Saltado"
        else -> "Desconocido"
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = displayText,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DeleteExerciseConfirmationDialog(
    exerciseName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "Confirmar eliminación",
                color = RetrofitColors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Estás seguro de que deseas eliminar el ejercicio?",
                    color = RetrofitColors.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\"$exerciseName\"",
                    color = RetrofitColors.Primary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Esta acción no se puede deshacer.",
                    color = RetrofitColors.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Eliminar",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = RetrofitColors.Gray
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}