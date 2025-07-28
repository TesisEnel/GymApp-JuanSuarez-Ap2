package edu.ucne.gymapp.presentation.routine

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.ui.theme.RetrofitColors
import kotlinx.coroutines.delay

@Composable
fun RoutineScreen(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToRoutineExercise: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.successMessage, state.errorMessage) {
        if (state.successMessage != null || state.errorMessage != null) {
            delay(3000)
            onEvent(RoutineEvent.ClearMessages)
        }
    }

    LaunchedEffect(Unit) {
        onEvent(RoutineEvent.LoadAllRoutines)
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
                    .padding(bottom = 16.dp),
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
                    text = "RUTINAS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                IconButton(onClick = { showFilterDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
                        tint = RetrofitColors.Primary
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nueva Rutina",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                OutlinedButton(
                    onClick = { onEvent(RoutineEvent.LoadActiveRoutines) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    border = BorderStroke(1.dp, RetrofitColors.Primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Solo Activas",
                        color = RetrofitColors.Primary,
                        fontSize = 14.sp
                    )
                }
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(state.routines) { routine ->
                        RoutineCard(
                            routine = routine,
                            onToggleActive = { onEvent(RoutineEvent.ToggleRoutineActive(routine.routineId)) },
                            onComplete = { onEvent(RoutineEvent.IncrementTimesCompleted(routine.routineId)) },
                            onSelect = { onEvent(RoutineEvent.SelectRoutine(routine)) }
                        )
                    }
                }
            }
        }

        state.successMessage?.let { message ->
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
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        state.errorMessage?.let { message ->
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
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showCreateDialog || state.selectedRoutine != null) {
        CreateEditRoutineDialog(
            state = state,
            onEvent = onEvent,
            onDismiss = {
                showCreateDialog = false
                onEvent(RoutineEvent.SelectRoutine(Routine()))
            }
        )
    }

    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onFilterByDifficulty = { difficulty ->
                onEvent(RoutineEvent.LoadRoutinesByDifficulty(difficulty))
                showFilterDialog = false
            },
            onFilterByMuscleGroup = { muscleGroup ->
                onEvent(RoutineEvent.LoadRoutinesByMuscleGroups(muscleGroup))
                showFilterDialog = false
            },
            onShowAll = {
                onEvent(RoutineEvent.LoadAllRoutines)
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun RoutineCard(
    routine: Routine,
    onToggleActive: () -> Unit,
    onComplete: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = routine.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = routine.isActive,
                    onCheckedChange = { onToggleActive() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = RetrofitColors.Primary,
                        uncheckedThumbColor = RetrofitColors.Gray,
                        uncheckedTrackColor = RetrofitColors.Gray.copy(alpha = 0.3f)
                    )
                )
            }

            if (routine.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = routine.description,
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = RetrofitColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${routine.estimatedDuration} min",
                        fontSize = 12.sp,
                        color = RetrofitColors.Gray
                    )
                }

                Text(
                    text = routine.difficulty,
                    fontSize = 12.sp,
                    color = when (routine.difficulty) {
                        "Principiante" -> Color.Green
                        "Intermedio" -> Color.Yellow
                        "Avanzado" -> Color.Red
                        else -> RetrofitColors.Gray
                    },
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (routine.targetMuscleGroups.isNotEmpty()) {
                Text(
                    text = "Músculos: ${routine.targetMuscleGroups}",
                    fontSize = 12.sp,
                    color = RetrofitColors.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Completada ${routine.timesCompleted} veces",
                    fontSize = 12.sp,
                    color = RetrofitColors.Gray
                )

                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "Completar",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditRoutineDialog(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = state.selectedRoutine != null && state.selectedRoutine.routineId != 0

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (isEditing) "Editar Rutina" else "Nueva Rutina",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(RoutineEvent.NameChange(it)) },
                    label = { Text("Nombre de la rutina") },
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
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.description,
                    onValueChange = { onEvent(RoutineEvent.DescriptionChange(it)) },
                    label = { Text("Descripción") },
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
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.estimatedDuration.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { duration ->
                            onEvent(RoutineEvent.EstimatedDurationChange(duration))
                        }
                    },
                    label = { Text("Duración (minutos)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetrofitColors.Primary,
                        unfocusedBorderColor = RetrofitColors.Gray,
                        focusedLabelColor = RetrofitColors.Primary,
                        unfocusedLabelColor = RetrofitColors.Gray,
                        focusedTextColor = RetrofitColors.onSurface,
                        unfocusedTextColor = RetrofitColors.onSurface,
                        cursorColor = RetrofitColors.Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                var difficultyExpanded by remember { mutableStateOf(false) }
                val difficulties = listOf("Principiante", "Intermedio", "Avanzado")

                ExposedDropdownMenuBox(
                    expanded = difficultyExpanded,
                    onExpandedChange = { difficultyExpanded = !difficultyExpanded }
                ) {
                    OutlinedTextField(
                        value = state.difficulty,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dificultad") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RetrofitColors.Primary,
                            unfocusedBorderColor = RetrofitColors.Gray,
                            focusedLabelColor = RetrofitColors.Primary,
                            unfocusedLabelColor = RetrofitColors.Gray,
                            focusedTextColor = RetrofitColors.onSurface,
                            unfocusedTextColor = RetrofitColors.onSurface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = difficultyExpanded,
                        onDismissRequest = { difficultyExpanded = false }
                    ) {
                        difficulties.forEach { difficulty ->
                            DropdownMenuItem(
                                text = { Text(difficulty) },
                                onClick = {
                                    onEvent(RoutineEvent.DifficultyChange(difficulty))
                                    difficultyExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.targetMuscleGroups,
                    onValueChange = { onEvent(RoutineEvent.TargetMuscleGroupsChange(it)) },
                    label = { Text("Grupos musculares") },
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
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rutina activa",
                        color = RetrofitColors.onSurface,
                        fontSize = 16.sp
                    )

                    Switch(
                        checked = state.isActive,
                        onCheckedChange = { onEvent(RoutineEvent.IsActiveChange(it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = RetrofitColors.Primary,
                            uncheckedThumbColor = RetrofitColors.Gray,
                            uncheckedTrackColor = RetrofitColors.Gray.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, RetrofitColors.Gray),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            color = RetrofitColors.Gray
                        )
                    }

                    Button(
                        onClick = {
                            if (isEditing) {
                                onEvent(RoutineEvent.UpdateRoutine)
                            } else {
                                onEvent(RoutineEvent.CreateRoutine)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RetrofitColors.Primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = if (isEditing) "Actualizar" else "Crear",
                                color = Color.White
                            )
                        }
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onEvent(RoutineEvent.DeleteRoutine) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Eliminar Rutina",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onFilterByDifficulty: (String) -> Unit,
    onFilterByMuscleGroup: (String) -> Unit,
    onShowAll: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Filtrar Rutinas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Por Dificultad:",
                    fontSize = 16.sp,
                    color = RetrofitColors.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Principiante", "Intermedio", "Avanzado").forEach { difficulty ->
                        Button(
                            onClick = { onFilterByDifficulty(difficulty) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RetrofitColors.Primary.copy(alpha = 0.8f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = difficulty,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Por Grupo Muscular:",
                    fontSize = 16.sp,
                    color = RetrofitColors.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.height(120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val muscleGroups = listOf("Pecho", "Espalda", "Piernas", "Brazos", "Hombros", "Core")
                    items(muscleGroups) { muscle ->
                        OutlinedButton(
                            onClick = { onFilterByMuscleGroup(muscle) },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, RetrofitColors.Primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = muscle,
                                color = RetrofitColors.Primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, RetrofitColors.Gray),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            color = RetrofitColors.Gray
                        )
                    }

                    Button(
                        onClick = onShowAll,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RetrofitColors.Primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Mostrar Todas",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
