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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.window.DialogProperties
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.RoutineExercise
import edu.ucne.gymapp.ui.theme.RetrofitColors
import kotlinx.coroutines.delay


import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*



@Composable
fun RoutineScreen(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToRoutineExercise: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Simplify LaunchedEffects into one block when possible
    LaunchedEffect(state.successMessage, state.errorMessage, state.isCreated, state.isUpdated, state.isDeleted) {
        if (state.successMessage != null || state.errorMessage != null) {
            delay(3000)
            onEvent(RoutineEvent.ClearMessages)
        }
        if (state.isCreated || state.isUpdated || state.isDeleted) {
            showCreateDialog = false
        }
    }

    // Load all routines on first composition
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
            RoutineScreenTopBar(onNavigateBack = onNavigateBack, onFilterClick = { showFilterDialog = true })

            Spacer(modifier = Modifier.height(16.dp))

            RoutineScreenActions(
                onNewRoutine = {
                    onEvent(RoutineEvent.SelectRoutine(Routine()))
                    showCreateDialog = true
                },
                onLoadActive = { onEvent(RoutineEvent.LoadActiveRoutines) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                LoadingIndicator()
            } else {
                RoutineList(
                    routines = state.routines,
                    onToggleActive = { routineId -> onEvent(RoutineEvent.ToggleRoutineActive(routineId)) },
                    onComplete = { routineId -> onEvent(RoutineEvent.IncrementTimesCompleted(routineId)) },
                    onSelect = { routine -> onEvent(RoutineEvent.ViewRoutine(routine)) }
                )
            }
        }

        // Success and error messages
        state.successMessage?.let { SuccessMessage(message = it) }
        state.errorMessage?.let { ErrorMessage(message = it, onClose = { onEvent(RoutineEvent.ClearMessages) }) }

        if (showCreateDialog) {
            CreateRoutineDialog(
                state = state,
                onEvent = onEvent,
                onDismiss = {
                    showCreateDialog = false
                    onEvent(RoutineEvent.HideExerciseSelection)
                }
            )
        }

        if (showFilterDialog) {
            FilterDialogContent(
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

        if (state.showRoutineDetails && state.routineToView != null) {
            RoutineDetailsDialogContent(
                routine = state.routineToView,
                routineExercises = state.selectedExercises,
                getExerciseDetails = { exerciseId ->
                    state.routineExercises.find { it.exerciseId == exerciseId }
                },
                onEdit = {
                    onEvent(RoutineEvent.SelectRoutine(state.routineToView!!))
                    onEvent(RoutineEvent.ClearMessages)
                    showCreateDialog = true
                },
                onDismiss = { onEvent(RoutineEvent.ClearMessages) }
            )
        }
    }
}

@Composable
private fun RoutineScreenTopBar(
    onNavigateBack: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
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

        Text(
            text = "RUTINAS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = RetrofitColors.onSurface
        )

        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .background(
                    color = RetrofitColors.Primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtrar",
                tint = RetrofitColors.Primary
            )
        }
    }
}

@Composable
private fun RoutineScreenActions(
    onNewRoutine: () -> Unit,
    onLoadActive: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onNewRoutine,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RetrofitColors.Primary),
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
            onClick = onLoadActive,
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
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = RetrofitColors.Primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun RoutineList(
    routines: List<Routine>,
    onToggleActive: (Int) -> Unit,
    onComplete: (Int) -> Unit,
    onSelect: (Routine) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(routines) { routine ->
            RoutineCard(
                routine = routine,
                onToggleActive = { onToggleActive(routine.routineId) },
                onComplete = { onComplete(routine.routineId) },
                onSelect = { onSelect(routine) }
            )
        }
    }
}

@Composable
private fun SuccessMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.9f)),
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


@Composable
private fun ErrorMessage(message: String, onClose: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


// Dialogs (assuming these composables exist with same signatures)
@Composable
private fun CreateRoutineDialog(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit,
    onDismiss: () -> Unit
) {
    RoutineWizardDialog(state = state, onEvent = onEvent, onDismiss = onDismiss)
}

@Composable
private fun FilterDialogContent(
    onDismiss: () -> Unit,
    onFilterByDifficulty: (String) -> Unit,
    onFilterByMuscleGroup: (String) -> Unit,
    onShowAll: () -> Unit
) {
    FilterDialog(
        onDismiss = onDismiss,
        onFilterByDifficulty = onFilterByDifficulty,
        onFilterByMuscleGroup = onFilterByMuscleGroup,
        onShowAll = onShowAll
    )
}

@Composable
private fun RoutineDetailsDialogContent(
    routine: Routine,
    routineExercises: List<Exercise>,
    getExerciseDetails: (Int) -> RoutineExercise?,
    onEdit: () -> Unit,
    onDismiss: () -> Unit
) {
    RoutineDetailsDialog(
        routine = routine,
        routineExercises = routineExercises,
        getExerciseDetails = getExerciseDetails,
        onEdit = onEdit,
        onDismiss = onDismiss
    )
}
@Composable
fun RoutineDetailsDialog(
    routine: Routine,
    routineExercises: List<Exercise> = emptyList(),
    getExerciseDetails: (Int) -> RoutineExercise? = { null },
    onEdit: () -> Unit,
    onDismiss: () -> Unit
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = routine.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = RetrofitColors.Gray
                        )
                    }
                }

                if (routine.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = routine.description,
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = RetrofitColors.Background
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Duración:", fontSize = 14.sp, color = RetrofitColors.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = RetrofitColors.Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "${routine.estimatedDuration} min",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = RetrofitColors.onSurface
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Dificultad:", fontSize = 14.sp, color = RetrofitColors.Gray)
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = when (routine.difficulty) {
                                        "Principiante" -> Color.Green.copy(alpha = 0.2f)
                                        "Intermedio" -> Color.Yellow.copy(alpha = 0.2f)
                                        "Avanzado" -> Color.Red.copy(alpha = 0.2f)
                                        else -> RetrofitColors.Gray.copy(alpha = 0.2f)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = routine.difficulty,
                                    fontSize = 12.sp,
                                    color = when (routine.difficulty) {
                                        "Principiante" -> Color.Green
                                        "Intermedio" -> Color.Yellow.copy(red = 0.8f)
                                        "Avanzado" -> Color.Red
                                        else -> RetrofitColors.Gray
                                    },
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Músculos:", fontSize = 14.sp, color = RetrofitColors.Gray)
                            Text(
                                routine.targetMuscleGroups,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = RetrofitColors.onSurface,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Completada:", fontSize = 14.sp, color = RetrofitColors.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = RetrofitColors.Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "${routine.timesCompleted} veces",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = RetrofitColors.onSurface
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estado:", fontSize = 14.sp, color = RetrofitColors.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = if (routine.isActive) RetrofitColors.Primary else RetrofitColors.Gray,
                                            shape = androidx.compose.foundation.shape.CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (routine.isActive) "Activa" else "Inactiva",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (routine.isActive) RetrofitColors.Primary else RetrofitColors.Gray
                                )
                            }
                        }
                    }
                }

                if (routineExercises.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ejercicios incluidos:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetrofitColors.onSurface
                        )

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = RetrofitColors.Primary.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${routineExercises.size} ejercicios",
                                fontSize = 12.sp,
                                color = RetrofitColors.Primary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.height(180.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(routineExercises) { exercise ->
                            val details = getExerciseDetails(exercise.exerciseId)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = RetrofitColors.Background
                                ),
                                border = BorderStroke(1.dp, RetrofitColors.Gray.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = RetrofitColors.Primary,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = exercise.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = RetrofitColors.onSurface
                                        )

                                        if (exercise.description.isNotEmpty()) {
                                            Text(
                                                text = exercise.description,
                                                fontSize = 12.sp,
                                                color = RetrofitColors.Gray,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.padding(top = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Card(
                                                colors = CardDefaults.cardColors(
                                                    containerColor = when (exercise.difficulty) {
                                                        "Principiante" -> Color.Green.copy(alpha = 0.2f)
                                                        "Intermedio" -> Color.Yellow.copy(alpha = 0.2f)
                                                        "Avanzado" -> Color.Red.copy(alpha = 0.2f)
                                                        else -> RetrofitColors.Gray.copy(alpha = 0.2f)
                                                    }
                                                ),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = exercise.difficulty,
                                                    fontSize = 10.sp,
                                                    color = when (exercise.difficulty) {
                                                        "Principiante" -> Color.Green
                                                        "Intermedio" -> Color.Yellow.copy(red = 0.8f)
                                                        "Avanzado" -> Color.Red
                                                        else -> RetrofitColors.Gray
                                                    },
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }

                                            if (exercise.equipment?.isNotBlank() == true) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "• ${exercise.equipment}",
                                                    fontSize = 10.sp,
                                                    color = RetrofitColors.Gray
                                                )
                                            }
                                        }
                                    }


                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = "${details?.sets ?: 3} series",
                                            fontSize = 10.sp,
                                            color = RetrofitColors.Gray
                                        )
                                        Text(
                                            text = "${details?.reps ?: "10"} reps",
                                            fontSize = 10.sp,
                                            color = RetrofitColors.Gray
                                        )
                                        Text(
                                            text = "${details?.restTime ?: 90}s",
                                            fontSize = 10.sp,
                                            color = RetrofitColors.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = RetrofitColors.Background
                        ),
                        border = BorderStroke(1.dp, RetrofitColors.Gray.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
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
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "No hay ejercicios asignados",
                                fontSize = 14.sp,
                                color = RetrofitColors.Gray,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Edita la rutina para agregar ejercicios",
                                fontSize = 12.sp,
                                color = RetrofitColors.Gray.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
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
                        Text("Cerrar", color = RetrofitColors.Gray)
                    }

                    Button(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RetrofitColors.Primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", color = Color.White)
                    }
                }
            }
        }
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

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (routine.difficulty) {
                            "Principiante" -> Color.Green.copy(alpha = 0.2f)
                            "Intermedio" -> Color.Yellow.copy(alpha = 0.2f)
                            "Avanzado" -> Color.Red.copy(alpha = 0.2f)
                            else -> RetrofitColors.Gray.copy(alpha = 0.2f)
                        }
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = routine.difficulty,
                        fontSize = 12.sp,
                        color = when (routine.difficulty) {
                            "Principiante" -> Color.Green
                            "Intermedio" -> Color.Yellow.copy(red = 0.8f)
                            "Avanzado" -> Color.Red
                            else -> RetrofitColors.Gray
                        },
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (routine.targetMuscleGroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
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

@Composable
fun RoutineWizardDialog(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = state.selectedRoutine != null && state.selectedRoutine.routineId != 0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
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
                WizardHeader(
                    isEditing = isEditing,
                    currentStep = state.currentStep,
                    onDismiss = onDismiss
                )

                Spacer(modifier = Modifier.height(24.dp))

                when (state.currentStep) {
                    RoutineCreationStep.BASIC_INFO -> {
                        BasicInfoStep(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                    RoutineCreationStep.MUSCLE_GROUP -> {
                        MuscleGroupStep(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                    RoutineCreationStep.EXERCISE -> {
                        ExerciseSelectionStep(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                    RoutineCreationStep.EXERCISE_CONFIG -> {
                        ExerciseConfigStep(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                    RoutineCreationStep.REVIEW -> {
                        ReviewStep(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                WizardNavigation(
                    state = state,
                    onEvent = onEvent,
                    isEditing = isEditing,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
fun WizardHeader(
    isEditing: Boolean,
    currentStep: RoutineCreationStep,
    onDismiss: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEditing) "Editar Rutina" else "Nueva Rutina",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface
            )

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = RetrofitColors.Gray
                )
            }
        }

        if (!isEditing) {
            Spacer(modifier = Modifier.height(12.dp))

            val progress = when (currentStep) {
                RoutineCreationStep.BASIC_INFO -> 0.2f
                RoutineCreationStep.MUSCLE_GROUP -> 0.4f
                RoutineCreationStep.EXERCISE -> 0.6f
                RoutineCreationStep.EXERCISE_CONFIG -> 0.8f
                RoutineCreationStep.REVIEW -> 1.0f
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = RetrofitColors.Primary,
                trackColor = RetrofitColors.Primary.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (currentStep) {
                    RoutineCreationStep.BASIC_INFO -> "Paso 1 de 5: Información básica"
                    RoutineCreationStep.MUSCLE_GROUP -> "Paso 2 de 5: Seleccionar grupo muscular"
                    RoutineCreationStep.EXERCISE -> "Paso 3 de 5: Elegir ejercicios"
                    RoutineCreationStep.EXERCISE_CONFIG -> "Paso 4 de 5: Configurar ejercicios"
                    RoutineCreationStep.REVIEW -> "Paso 5 de 5: Revisar y crear"
                },
                fontSize = 14.sp,
                color = RetrofitColors.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoStep(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
    }
}

@Composable
fun MuscleGroupStep(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        if (state.muscleGroups.isEmpty()) {
            onEvent(RoutineEvent.LoadMuscleGroups)
        }
    }

    Column {
        Text(
            text = "Selecciona el grupo muscular principal:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = RetrofitColors.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RetrofitColors.Primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.muscleGroups) { muscleGroup ->
                    MuscleGroupCard(
                        muscleGroup = muscleGroup,
                        isSelected = state.selectedMuscleGroups.any { it.muscleGroupId == muscleGroup.muscleGroupId },                        onSelect = { onEvent(RoutineEvent.SelectMuscleGroup(muscleGroup)) }
                    )
                }
            }
        }
    }
}

@Composable
fun MuscleGroupCard(
    muscleGroup: MuscleGroup,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                RetrofitColors.Primary.copy(alpha = 0.1f)
            else
                RetrofitColors.Background
        ),
        border = if (isSelected)
            BorderStroke(2.dp, RetrofitColors.Primary)
        else
            BorderStroke(1.dp, RetrofitColors.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = muscleGroup.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetrofitColors.onSurface
                )

                Text(
                    text = muscleGroup.description,
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ExerciseSelectionStep(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ejercicios para ${state.selectedMuscleGroups.joinToString(", ") { it.name }}:",                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RetrofitColors.onSurface,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${state.selectedExercises.size} seleccionados",
                fontSize = 14.sp,
                color = RetrofitColors.Primary,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedTextField(
            value = state.exerciseSearchQuery,
            onValueChange = { onEvent(RoutineEvent.SearchExercises(it)) },
            label = { Text("Buscar ejercicios") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = RetrofitColors.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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

        if (state.selectedExercises.isNotEmpty()) {
            Text(
                text = "Ejercicios seleccionados:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = RetrofitColors.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(state.selectedExercises) { exercise ->
                    SelectedExerciseChip(
                        exercise = exercise,
                        onRemove = { onEvent(RoutineEvent.RemoveExercise(exercise)) }
                    )
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RetrofitColors.Primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.height(250.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.filteredExercises) { exercise ->
                    ExerciseSelectionCard(
                        exercise = exercise,
                        isSelected = state.selectedExercises.any { it.exerciseId == exercise.exerciseId },
                        onToggleSelect = { onEvent(RoutineEvent.SelectExercise(exercise)) }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedExerciseChip(
    exercise: Exercise,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.name,
                fontSize = 12.sp,
                color = RetrofitColors.Primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remover",
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun ExerciseSelectionCard(
    exercise: Exercise,
    isSelected: Boolean,
    onToggleSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                RetrofitColors.Primary.copy(alpha = 0.1f)
            else
                RetrofitColors.Background
        ),
        border = if (isSelected)
            BorderStroke(2.dp, RetrofitColors.Primary)
        else
            BorderStroke(1.dp, RetrofitColors.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetrofitColors.onSurface
                )

                Text(
                    text = exercise.description,
                    fontSize = 12.sp,
                    color = RetrofitColors.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when (exercise.difficulty) {
                                "Principiante" -> Color.Green.copy(alpha = 0.2f)
                                "Intermedio" -> Color.Yellow.copy(alpha = 0.2f)
                                "Avanzado" -> Color.Red.copy(alpha = 0.2f)
                                else -> RetrofitColors.Gray.copy(alpha = 0.2f)
                            }
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = exercise.difficulty,
                            fontSize = 10.sp,
                            color = when (exercise.difficulty) {
                                "Principiante" -> Color.Green
                                "Intermedio" -> Color.Yellow.copy(red = 0.8f)
                                "Avanzado" -> Color.Red
                                else -> RetrofitColors.Gray
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (exercise.equipment?.isNotBlank() == true) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Equipo",
                            tint = RetrofitColors.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ExerciseConfigStep(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit
) {
    Column {
        Text(
            text = "Configuración de ejercicios:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = RetrofitColors.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.selectedExercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay ejercicios seleccionados",
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.selectedExercises) { exercise ->
                    ExerciseConfigCard(exercise = exercise)
                }
            }
        }
    }
}

@Composable
fun ExerciseConfigCard(exercise: Exercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Background
        ),
        border = BorderStroke(1.dp, RetrofitColors.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RetrofitColors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = RetrofitColors.Surface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Series",
                            fontSize = 12.sp,
                            color = RetrofitColors.Gray
                        )
                        Text(
                            text = "3",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetrofitColors.onSurface
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = RetrofitColors.Surface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Reps",
                            fontSize = 12.sp,
                            color = RetrofitColors.Gray
                        )
                        Text(
                            text = "10",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetrofitColors.onSurface
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = RetrofitColors.Surface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Descanso",
                            fontSize = 12.sp,
                            color = RetrofitColors.Gray
                        )
                        Text(
                            text = "90s",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetrofitColors.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewStep(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Resumen de la rutina:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = RetrofitColors.onSurface
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Background
            ),
            border = BorderStroke(1.dp, RetrofitColors.Primary.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nombre:",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )
                    Text(
                        text = state.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Duración:",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )
                    Text(
                        text = "${state.estimatedDuration} minutos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Dificultad:",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )
                    Text(
                        text = state.difficulty,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Grupo muscular:",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )
                    Text(
                        text = state.selectedMuscleGroups.joinToString(", ") { it.name }.ifEmpty { "No seleccionado" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ejercicios:",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )
                    Text(
                        text = "${state.selectedExercises.size} ejercicios",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.Primary
                    )
                }
            }
        }

        if (state.selectedExercises.isNotEmpty()) {
            Text(
                text = "Ejercicios incluidos:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = RetrofitColors.onSurface
            )

            LazyColumn(
                modifier = Modifier.height(150.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(state.selectedExercises) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = RetrofitColors.Primary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = exercise.name,
                            fontSize = 14.sp,
                            color = RetrofitColors.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WizardNavigation(
    state: RoutineUiState,
    onEvent: (RoutineEvent) -> Unit,
    isEditing: Boolean,
    onDismiss: () -> Unit
) {
    if (isEditing) {
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
                onClick = { onEvent(RoutineEvent.UpdateRoutine) },
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
                    Text("Actualizar", color = Color.White)
                }
            }
        }

        if (state.selectedRoutine != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onEvent(RoutineEvent.DeleteRoutine) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Eliminar Rutina", color = Color.White)
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.currentStep != RoutineCreationStep.BASIC_INFO) {
                OutlinedButton(
                    onClick = { onEvent(RoutineEvent.PreviousStep) },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, RetrofitColors.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Anterior", color = RetrofitColors.Gray)
                }
            } else {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, RetrofitColors.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", color = RetrofitColors.Gray)
                }
            }

            Button(
                onClick = {
                    when (state.currentStep) {
                        RoutineCreationStep.REVIEW -> {
                            onEvent(RoutineEvent.CreateRoutine)
                        }
                        RoutineCreationStep.MUSCLE_GROUP -> {
                            if (state.selectedMuscleGroups.isNotEmpty()) {
                                onEvent(RoutineEvent.NextStep)
                            }
                        }
                        else -> {
                            onEvent(RoutineEvent.NextStep)
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading && canProceedToNextStep(state)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = when (state.currentStep) {
                            RoutineCreationStep.REVIEW -> "Crear Rutina"
                            else -> "Siguiente"
                        },
                        color = Color.White
                    )
                    if (state.currentStep != RoutineCreationStep.REVIEW) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

fun canProceedToNextStep(state: RoutineUiState): Boolean {
    return when (state.currentStep) {
        RoutineCreationStep.BASIC_INFO -> {
            state.name.isNotBlank() && state.estimatedDuration > 0
        }
        RoutineCreationStep.MUSCLE_GROUP -> {
            state.selectedMuscleGroups.isNotEmpty()
        }
        RoutineCreationStep.EXERCISE -> {
            state.selectedExercises.isNotEmpty()
        }
        RoutineCreationStep.EXERCISE_CONFIG -> true
        RoutineCreationStep.REVIEW -> true
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