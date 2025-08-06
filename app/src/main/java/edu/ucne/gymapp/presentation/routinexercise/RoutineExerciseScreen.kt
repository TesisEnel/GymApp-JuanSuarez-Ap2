package edu.ucne.gymapp.presentation.routinexercise

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.data.local.entities.RoutineExercise
import edu.ucne.gymapp.ui.theme.RetrofitColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoutineExerciseScreen(
    state: RoutineExerciseUiState,
    onEvent: (RoutineExerciseEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExercise: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.isCreated, state.isUpdated, state.isDeleted) {
        if (state.isCreated || state.isUpdated || state.isDeleted) {
            showAddDialog = false
            showEditDialog = false
            showDeleteDialog = false
            onEvent(RoutineExerciseEvent.ClearMessages)
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
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RetrofitColors.Surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = RetrofitColors.onSurface
                            )
                        }
                        Text(
                            text = "Ejercicios de Rutina",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetrofitColors.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    IconButton(
                        onClick = { showAddDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar ejercicio",
                            tint = RetrofitColors.Primary
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.exercisesInRoutine) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onEdit = {
                            onEvent(RoutineExerciseEvent.SelectRoutineExercise(exercise))
                            showEditDialog = true
                        },
                        onDelete = {
                            onEvent(RoutineExerciseEvent.SelectRoutineExercise(exercise))
                            showDeleteDialog = true
                        }
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = RetrofitColors.Primary
                    )
                }
            }
        }

        state.errorMessage?.let { errorMsg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMsg,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onEvent(RoutineExerciseEvent.ClearError) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        state.successMessage?.let { successMsg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = successMsg,
                        color = Color.Green,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onEvent(RoutineExerciseEvent.ClearMessages) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Green
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        ExerciseDialog(
            title = "Agregar Ejercicio",
            state = state,
            onEvent = onEvent,
            onDismiss = { showAddDialog = false },
            onConfirm = { onEvent(RoutineExerciseEvent.CreateRoutineExercise) }
        )
    }

    if (showEditDialog) {
        ExerciseDialog(
            title = "Editar Ejercicio",
            state = state,
            onEvent = onEvent,
            onDismiss = { showEditDialog = false },
            onConfirm = { onEvent(RoutineExerciseEvent.UpdateRoutineExercise) }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = RetrofitColors.Surface,
            title = {
                Text(
                    text = "Eliminar Ejercicio",
                    color = RetrofitColors.onSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar este ejercicio de la rutina?",
                    color = RetrofitColors.onSurface
                )
            },
            confirmButton = {
                TextButton (
                    onClick = {
                        onEvent(RoutineExerciseEvent.DeleteRoutineExercise)
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(
                        text = "Cancelar",
                        color = RetrofitColors.Gray
                    )
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseCard(
    exercise: RoutineExercise,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(40.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = RetrofitColors.Primary.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = exercise.order.toString(),
                                color = RetrofitColors.Primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Ejercicio #${exercise.exerciseId}",
                            color = RetrofitColors.onSurface,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${exercise.sets} series × ${exercise.reps} reps",
                            color = RetrofitColors.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = RetrofitColors.Gray
                        )
                    }
                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                    }
                }
            }

            if (exercise.weight != null || exercise.restTime > 0 || !exercise.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    exercise.weight?.let { weight ->
                        InfoChip(
                            icon = Icons.Default.FitnessCenter,
                            text = "${weight}kg"
                        )
                    }

                    if (exercise.restTime > 0) {
                        InfoChip(
                            icon = Icons.Default.Timer,
                            text = "${exercise.restTime}s"
                        )
                    }
                }

                exercise.notes?.let { notes ->
                    if (notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = notes,
                            color = RetrofitColors.Gray,
                            fontSize = 12.sp,
                            style = TextStyle(fontStyle = FontStyle.Italic)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(
    icon: ImageVector,
    text: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RetrofitColors.Primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = RetrofitColors.Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ExerciseDialog(
    title: String,
    state: RoutineExerciseUiState,
    onEvent: (RoutineExerciseEvent) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        title = {
            Text(
                text = title,
                color = RetrofitColors.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.sets.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { sets ->
                            onEvent(RoutineExerciseEvent.SetsChange(sets))
                        }
                    },
                    label = { Text("Series") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.RepeatOne,
                            contentDescription = "Series",
                            tint = RetrofitColors.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary,
                        focusedLeadingIconColor = RetrofitColors.Primary,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = state.reps,
                    onValueChange = { onEvent(RoutineExerciseEvent.RepsChange(it)) },
                    label = { Text("Repeticiones") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repeticiones",
                            tint = RetrofitColors.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary,
                        focusedLeadingIconColor = RetrofitColors.Primary,
                    )
                )

                OutlinedTextField(
                    value = state.weight?.toString() ?: "",
                    onValueChange = {
                        val weight = if (it.isBlank()) null else it.toFloatOrNull()
                        onEvent(RoutineExerciseEvent.WeightChange(weight))
                    },
                    label = { Text("Peso (kg)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Peso",
                            tint = RetrofitColors.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary,
                        focusedLeadingIconColor = RetrofitColors.Primary,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = state.restTime.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { restTime ->
                            onEvent(RoutineExerciseEvent.RestTimeChange(restTime))
                        }
                    },
                    label = { Text("Descanso (seg)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Descanso",
                            tint = RetrofitColors.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary,
                        focusedLeadingIconColor = RetrofitColors.Primary,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = state.notes ?: "",
                    onValueChange = { onEvent(RoutineExerciseEvent.NotesChange(it)) },
                    label = { Text("Notas") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = "Notas",
                            tint = RetrofitColors.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary,
                        focusedLeadingIconColor = RetrofitColors.Primary,
                    ),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                ),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = "Confirmar",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancelar",
                    color = RetrofitColors.Gray
                )
            }
        }
    )
}
