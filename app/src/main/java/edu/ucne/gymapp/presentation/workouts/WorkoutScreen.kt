package edu.ucne.gymapp.presentation.workouts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.ui.theme.RetrofitColors
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutScreen(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToWorkoutExercise: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showActiveWorkout by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var workoutToDelete by remember { mutableStateOf<Workout?>(null) }

    LaunchedEffect(state.isWorkoutActive) {
        showActiveWorkout = state.isWorkoutActive
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
                    text = "ENTRENAMIENTOS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear entrenamiento",
                        tint = RetrofitColors.Primary
                    )
                }
            }

            if (state.isWorkoutActive && state.currentWorkout != null) {
                ActiveWorkoutCard(
                    workout = state.currentWorkout,
                    isPaused = state.isPaused,
                    onPause = { onEvent(WorkoutEvent.PauseWorkout(state.currentWorkout.workoutId)) },
                    onResume = { onEvent(WorkoutEvent.ResumeWorkout(state.currentWorkout.workoutId)) },
                    onFinish = { onEvent(WorkoutEvent.FinishWorkout(state.currentWorkout.workoutId)) },
                    onCancel = { onEvent(WorkoutEvent.CancelWorkout(state.currentWorkout.workoutId)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(listOf("Todos", "Completados", "En Progreso", "Pausados", "Cancelados")) { status ->
                    FilterChip(
                        onClick = {
                            when (status) {
                                "Todos" -> onEvent(WorkoutEvent.LoadAllWorkouts)
                                "Completados" -> onEvent(WorkoutEvent.LoadWorkoutsByStatus("COMPLETED"))
                                "En Progreso" -> onEvent(WorkoutEvent.LoadWorkoutsByStatus("IN_PROGRESS"))
                                "Pausados" -> onEvent(WorkoutEvent.LoadWorkoutsByStatus("PAUSED"))
                                "Cancelados" -> onEvent(WorkoutEvent.LoadWorkoutsByStatus("CANCELLED"))
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
                    items(state.workouts) { workout ->
                        WorkoutCard(
                            workout = workout,
                            onSelect = { onEvent(WorkoutEvent.SelectWorkout(workout)) },
                            onDelete = {
                                workoutToDelete = workout
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateWorkoutDialog(
                state = state,
                onEvent = onEvent,
                onDismiss = { showCreateDialog = false }
            )
        }

        if (showDeleteDialog && workoutToDelete != null) {
            DeleteConfirmationDialog(
                workoutName = workoutToDelete!!.name,
                onConfirm = {
                    onEvent(WorkoutEvent.DeleteWorkout)
                    showDeleteDialog = false
                    workoutToDelete = null
                },
                onDismiss = {
                    showDeleteDialog = false
                    workoutToDelete = null
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
fun DeleteConfirmationDialog(
    workoutName: String,
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
                    text = "¿Estás seguro de que deseas eliminar el entrenamiento?",
                    color = RetrofitColors.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\"$workoutName\"",
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

@Composable
fun ActiveWorkoutCard(
    workout: Workout,
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit
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
                        text = "ENTRENAMIENTO ACTIVO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.Primary
                    )
                    Text(
                        text = workout.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetrofitColors.onSurface
                    )
                }

                Icon(
                    imageVector = if (isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            WorkoutTimer(
                startTime = workout.startTime,
                isPaused = isPaused
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = if (isPaused) onResume else onPause,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPaused) Color.Green else Color.Yellow
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
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onFinish,
                    modifier = Modifier
                        .weight(1f)
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
                        text = "Finalizar",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.Red.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: Workout,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
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
                        text = workout.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetrofitColors.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatDate(workout.startTime),
                        fontSize = 12.sp,
                        color = RetrofitColors.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(status = workout.status)

                    Spacer(modifier = Modifier.width(8.dp))

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

            if (workout.totalDuration > 0) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = RetrofitColors.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatDuration(workout.totalDuration),
                        fontSize = 12.sp,
                        color = RetrofitColors.Gray
                    )
                }
            }

            workout.notes?.let { notes ->
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
fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "COMPLETED" -> Pair(Color.Green.copy(alpha = 0.2f), Color.Green)
        "IN_PROGRESS" -> Pair(RetrofitColors.Primary.copy(alpha = 0.2f), RetrofitColors.Primary)
        "PAUSED" -> Pair(Color.Yellow.copy(alpha = 0.2f), Color.Yellow)
        "CANCELLED" -> Pair(Color.Red.copy(alpha = 0.2f), Color.Red)
        else -> Pair(RetrofitColors.Gray.copy(alpha = 0.2f), RetrofitColors.Gray)
    }

    val displayText = when (status) {
        "COMPLETED" -> "Completado"
        "IN_PROGRESS" -> "En Progreso"
        "PAUSED" -> "Pausado"
        "CANCELLED" -> "Cancelado"
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
fun WorkoutTimer(
    startTime: Long,
    isPaused: Boolean
) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(isPaused) {
        if (!isPaused) {
            while (true) {
                currentTime = System.currentTimeMillis()
                delay(1000)
            }
        }
    }

    val elapsedSeconds = ((currentTime - startTime) / 1000).toInt()
    val formattedTime = formatDuration(elapsedSeconds)

    Text(
        text = formattedTime,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = RetrofitColors.Primary,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CreateWorkoutDialog(
    state: WorkoutUiState,
    onEvent: (WorkoutEvent) -> Unit,
    onDismiss: () -> Unit
) {
    var workoutName by remember { mutableStateOf("") }
    var workoutNotes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        title = {
            Text(
                text = "Nuevo Entrenamiento",
                color = RetrofitColors.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = workoutName,
                    onValueChange = { workoutName = it },
                    label = { Text("Nombre del entrenamiento") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = workoutNotes,
                    onValueChange = { workoutNotes = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(WorkoutEvent.NameChange(workoutName))
                    onEvent(WorkoutEvent.NotesChange(workoutNotes.ifBlank { null }))
                    onEvent(WorkoutEvent.CreateWorkout)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                ),
                enabled = workoutName.isNotBlank()
            ) {
                Text("Crear", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = RetrofitColors.Gray
                )
            ) {
                Text("Cancelar")
            }
        }
    )
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}

private fun formatDuration(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%02d:%02d", minutes, secs)
    }
}