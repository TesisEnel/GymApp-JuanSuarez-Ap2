package edu.ucne.gymapp.presentation.exercisesets

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.data.local.entities.ExerciseSet
import edu.ucne.gymapp.ui.theme.RetrofitColors

@Composable
fun ExerciseSetScreen(
    state: ExerciseSetUiState,
    onEvent: (ExerciseSetEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showCreateSetDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }

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
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = RetrofitColors.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "SERIES",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                IconButton(onClick = { showCreateSetDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar serie",
                        tint = RetrofitColors.Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = RetrofitColors.Primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.sets) { exerciseSet ->
                        SetItemCard(
                            exerciseSet = exerciseSet,
                            onMarkCompleted = { onEvent(ExerciseSetEvent.MarkAsCompleted(exerciseSet.exerciseSetId)) },
                            onEdit = {
                                onEvent(ExerciseSetEvent.LoadSetById(exerciseSet.exerciseSetId))
                                showUpdateDialog = true
                            },
                            onDelete = {
                                onEvent(ExerciseSetEvent.LoadSetById(exerciseSet.exerciseSetId))
                                onEvent(ExerciseSetEvent.DeleteSet)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onEvent(ExerciseSetEvent.LoadCompletedSets) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Surface
                    )
                ) {
                    Text(
                        text = "Completadas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                }

                Button(
                    onClick = { onEvent(ExerciseSetEvent.LoadPendingSets) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    )
                ) {
                    Text(
                        text = "Pendientes",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            state.errorMessage?.let { errorMsg ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMsg,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            state.successMessage?.let { successMsg ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Green.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = successMsg,
                        color = Color.Green,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (showCreateSetDialog) {
            CreateSetDialog(
                state = state,
                onEvent = onEvent,
                onDismiss = { showCreateSetDialog = false }
            )
        }

        if (showUpdateDialog) {
            UpdateSetDialog(
                state = state,
                onEvent = onEvent,
                onDismiss = { showUpdateDialog = false }
            )
        }
    }
}

@Composable
fun SetItemCard(
    exerciseSet: ExerciseSet,
    onMarkCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (exerciseSet.isCompleted)
                RetrofitColors.Primary.copy(alpha = 0.1f)
            else
                RetrofitColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Serie #${exerciseSet.setNumber}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface
                    )

                    Text(
                        text = "${exerciseSet.reps} reps",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )

                    exerciseSet.weight?.let { weight ->
                        Text(
                            text = "${weight}kg",
                            fontSize = 14.sp,
                            color = RetrofitColors.Gray
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!exerciseSet.isCompleted) {
                        IconButton(
                            onClick = onMarkCompleted,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Marcar completada",
                                tint = Color.Green,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = RetrofitColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (exerciseSet.isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completada",
                        tint = Color.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Completada",
                        fontSize = 12.sp,
                        color = Color.Green,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            exerciseSet.restTime?.let { rest ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Descanso: ${rest}s",
                    fontSize = 12.sp,
                    color = RetrofitColors.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dificultad: ${exerciseSet.difficulty}/10",
                fontSize = 12.sp,
                color = RetrofitColors.Gray
            )
        }
    }
}

@Composable
fun CreateSetDialog(
    state: ExerciseSetUiState,
    onEvent: (ExerciseSetEvent) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        title = {
            Text(
                text = "Crear Nueva Serie",
                color = RetrofitColors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.setNumber.toString(),
                    onValueChange = { onEvent(ExerciseSetEvent.SetNumberChange(it.toIntOrNull() ?: 1)) },
                    label = { Text("Número de Serie") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = state.reps.toString(),
                    onValueChange = { onEvent(ExerciseSetEvent.RepsChange(it.toIntOrNull() ?: 0)) },
                    label = { Text("Repeticiones") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = state.weight?.toString() ?: "",
                    onValueChange = { onEvent(ExerciseSetEvent.WeightChange(it.toFloatOrNull())) },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(ExerciseSetEvent.CreateSet)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                )
            ) {
                Text("Crear", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = RetrofitColors.Gray)
            }
        }
    )
}

@Composable
fun UpdateSetDialog(
    state: ExerciseSetUiState,
    onEvent: (ExerciseSetEvent) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = RetrofitColors.Surface,
        title = {
            Text(
                text = "Actualizar Serie",
                color = RetrofitColors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.reps.toString(),
                    onValueChange = { onEvent(ExerciseSetEvent.RepsChange(it.toIntOrNull() ?: 0)) },
                    label = { Text("Repeticiones") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = state.weight?.toString() ?: "",
                    onValueChange = { onEvent(ExerciseSetEvent.WeightChange(it.toFloatOrNull())) },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Text(
                    text = "Dificultad: ${state.difficulty}/10",
                    color = RetrofitColors.onSurface,
                    fontSize = 14.sp
                )

                Slider(
                    value = state.difficulty.toFloat(),
                    onValueChange = { onEvent(ExerciseSetEvent.DifficultyChange(it.toInt())) },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = RetrofitColors.Primary,
                        activeTrackColor = RetrofitColors.Primary,
                        inactiveTrackColor = RetrofitColors.Gray
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(ExerciseSetEvent.UpdateSet)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                )
            ) {
                Text("Actualizar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = RetrofitColors.Gray)
            }
        }
    )
}
