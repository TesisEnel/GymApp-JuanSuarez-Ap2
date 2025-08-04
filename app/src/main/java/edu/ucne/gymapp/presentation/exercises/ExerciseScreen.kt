package edu.ucne.gymapp.presentation.exercises

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.ui.theme.RetrofitColors

@Composable
fun ExerciseScreen(
    state: ExerciseUiState,
    onEvent: (ExerciseEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExerciseSet: () -> Unit,
    viewModel: ExerciseViewModel,
    muscleGroupId: Int? = null
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    LaunchedEffect(state.isCreated) {
        if (state.isCreated) {
            showCreateDialog = false
            onEvent(ExerciseEvent.ClearMessages)

        }
    }

    LaunchedEffect(state.isUpdated) {
        if (state.isUpdated) {
            showEditDialog = false
            onEvent(ExerciseEvent.ClearMessages)
        }
    }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            showDeleteDialog = false
            onEvent(ExerciseEvent.ClearMessages)
        }
    }

    LaunchedEffect(muscleGroupId) {
        if (muscleGroupId != null) {
            onEvent(ExerciseEvent.LoadExercisesByMuscleGroup(muscleGroupId))
        } else {
            onEvent(ExerciseEvent.LoadAllExercises)
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
                    text = "EJERCICIOS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                Button(
                    onClick = {
                        onEvent(ExerciseEvent.ClearMessages)
                        showCreateDialog = true
                    },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear ejercicio",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                item {
                    FilterChip(
                        selected = false,
                        onClick = {
                            if (muscleGroupId != null) {
                                onEvent(ExerciseEvent.LoadExercisesByMuscleGroup(muscleGroupId))
                            } else {
                                onEvent(ExerciseEvent.LoadExercisesByPopularity)
                            }
                        },
                        label = {
                            Text(
                                text = "Populares",
                                color = RetrofitColors.onSurface
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RetrofitColors.Surface,
                            selectedContainerColor = RetrofitColors.Primary
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = false,
                        onClick = {
                            if (muscleGroupId != null) {
                                // Filtrar principiantes SOLO del grupo muscular específico
                                onEvent(ExerciseEvent.LoadExercisesByMuscleGroupsAndDifficulties(
                                    listOf(muscleGroupId),
                                    listOf("Principiante")
                                ))
                            } else {
                                onEvent(ExerciseEvent.LoadExercisesByDifficulty("Principiante"))
                            }
                        },
                        label = {
                            Text(
                                text = "Principiante",
                                color = RetrofitColors.onSurface
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RetrofitColors.Surface,
                            selectedContainerColor = RetrofitColors.Primary
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = false,
                        onClick = {
                            if (muscleGroupId != null) {
                                // Filtrar intermedios SOLO del grupo muscular específico
                                onEvent(ExerciseEvent.LoadExercisesByMuscleGroupsAndDifficulties(
                                    listOf(muscleGroupId),
                                    listOf("Intermedio")
                                ))
                            } else {
                                onEvent(ExerciseEvent.LoadExercisesByDifficulty("Intermedio"))
                            }
                        },
                        label = {
                            Text(
                                text = "Intermedio",
                                color = RetrofitColors.onSurface
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RetrofitColors.Surface,
                            selectedContainerColor = RetrofitColors.Primary
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = false,
                        onClick = {
                            if (muscleGroupId != null) {
                                // Filtrar avanzados SOLO del grupo muscular específico
                                onEvent(ExerciseEvent.LoadExercisesByMuscleGroupsAndDifficulties(
                                    listOf(muscleGroupId),
                                    listOf("Avanzado")
                                ))
                            } else {
                                onEvent(ExerciseEvent.LoadExercisesByDifficulty("Avanzado"))
                            }
                        },
                        label = {
                            Text(
                                text = "Avanzado",
                                color = RetrofitColors.onSurface
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RetrofitColors.Surface,
                            selectedContainerColor = RetrofitColors.Primary
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = false,
                        onClick = {
                            if (muscleGroupId != null) {
                                onEvent(ExerciseEvent.LoadExercisesByMuscleGroup(muscleGroupId))
                            } else {
                                onEvent(ExerciseEvent.LoadAllExercises)
                            }
                        },
                        label = {
                            Text(
                                text = "Todos",
                                color = RetrofitColors.onSurface
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RetrofitColors.Surface,
                            selectedContainerColor = RetrofitColors.Primary
                        )
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.filteredExercises) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            canEditOrDelete = viewModel.canEditOrDeleteExercise(exercise),
                            onEdit = {
                                if (viewModel.canEditOrDeleteExercise(exercise)) {
                                    onEvent(ExerciseEvent.LoadExerciseById(exercise.exerciseId))
                                    showEditDialog = true
                                }
                            },
                            onDelete = {
                                if (viewModel.canEditOrDeleteExercise(exercise)) {
                                    onEvent(ExerciseEvent.LoadExerciseById(exercise.exerciseId))
                                    showDeleteDialog = true
                                }
                            },
                            onClick = {
                                onEvent(ExerciseEvent.IncrementPopularity(exercise.exerciseId))
                            }
                        )
                    }
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
                    containerColor = Color.Red.copy(alpha = 0.9f)
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
                    Text(
                        text = errorMsg,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onEvent(ExerciseEvent.ClearError) }
                    ) {
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

        state.successMessage?.let { successMsg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.9f)
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
                    Text(
                        text = successMsg,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onEvent(ExerciseEvent.ClearMessages) }
                    ) {
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

    // Dialogs
    if (showCreateDialog) {
        CreateExerciseDialog(
            state = state,
            onEvent = onEvent,
            onDismiss = {
                showCreateDialog = false
                onEvent(ExerciseEvent.ClearMessages)
            }
        )
    }

    if (showEditDialog) {
        EditExerciseDialog(
            state = state,
            onEvent = onEvent,
            onDismiss = {
                showEditDialog = false
                onEvent(ExerciseEvent.ClearMessages)
            }
        )
    }

    if (showDeleteDialog) {
        DeleteExerciseDialog(
            exercise = state.selectedExercise,
            onConfirm = {
                onEvent(ExerciseEvent.DeleteExercise)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
                onEvent(ExerciseEvent.ClearMessages)
            }
        )
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    canEditOrDelete: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = exercise.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetrofitColors.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        // Mostrar indicador para ejercicios predefinidos
                        if (!canEditOrDelete) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = RetrofitColors.Primary.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Sistema",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = RetrofitColors.Primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = exercise.description,
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (canEditOrDelete) {
                    Row {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.DarkGray,
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
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
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
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = exercise.difficulty,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (exercise.difficulty) {
                                "Principiante" -> Color.Green
                                "Intermedio" -> Color.Yellow.copy(red = 0.8f)
                                "Avanzado" -> Color.Red
                                else -> RetrofitColors.Gray
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (exercise.equipment?.isNotBlank() == true) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Equipo requerido",
                            tint = RetrofitColors.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = exercise.equipment,
                            fontSize = 12.sp,
                            color = RetrofitColors.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Popularidad",
                        tint = RetrofitColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = exercise.popularity.toString(),
                        fontSize = 12.sp,
                        color = RetrofitColors.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExerciseDialog(
    state: ExerciseUiState,
    onEvent: (ExerciseEvent) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 600.dp),
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Crear Ejercicio",
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
                }

                item {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(ExerciseEvent.NameChange(it)) },
                        label = { Text("Nombre del ejercicio") },
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
                }

                item {
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { onEvent(ExerciseEvent.DescriptionChange(it)) },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
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

                item {
                    OutlinedTextField(
                        value = state.instructions,
                        onValueChange = { onEvent(ExerciseEvent.InstructionsChange(it)) },
                        label = { Text("Instrucciones") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
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

                item {
                    var expanded by remember { mutableStateOf(false) }
                    val difficulties = listOf("Principiante", "Intermedio", "Avanzado")

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = state.difficulty,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Dificultad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(RetrofitColors.Surface)
                        ) {
                            difficulties.forEach { difficulty ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = difficulty,
                                            color = RetrofitColors.onSurface
                                        )
                                    },
                                    onClick = {
                                        onEvent(ExerciseEvent.DifficultyChange(difficulty))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = state.equipment ?: "",
                        onValueChange = { onEvent(ExerciseEvent.EquipmentChange(it.takeIf { it.isNotBlank() })) },
                        label = { Text("Equipo (opcional)") },
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
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RetrofitColors.Gray
                            )
                        ) {
                            Text("Cancelar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                onEvent(ExerciseEvent.CreateExercise)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RetrofitColors.Primary
                            ),
                            enabled = !state.isLoading
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text("Crear", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DeleteExerciseDialog(
    exercise: Exercise?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (exercise != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Eliminar ejercicio",
                    color = RetrofitColors.onSurface
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que quieres eliminar \"${exercise.name}\"? Esta acción no se puede deshacer.",
                    color = RetrofitColors.Gray
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = RetrofitColors.Gray)
                }
            },
            containerColor = RetrofitColors.Surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExerciseDialog(
    state: ExerciseUiState,
    onEvent: (ExerciseEvent) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 600.dp),
            colors = CardDefaults.cardColors(
                containerColor = RetrofitColors.Surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Editar Ejercicio",
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
                }

                item {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(ExerciseEvent.NameChange(it)) },
                        label = { Text("Nombre del ejercicio") },
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
                }

                item {
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { onEvent(ExerciseEvent.DescriptionChange(it)) },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
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

                item {
                    OutlinedTextField(
                        value = state.instructions,
                        onValueChange = { onEvent(ExerciseEvent.InstructionsChange(it)) },
                        label = { Text("Instrucciones") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
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

                item {
                    var expanded by remember { mutableStateOf(false) }
                    val difficulties = listOf("Principiante", "Intermedio", "Avanzado")

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = state.difficulty,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Dificultad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(RetrofitColors.Surface)
                        ) {
                            difficulties.forEach { difficulty ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = difficulty,
                                            color = RetrofitColors.onSurface
                                        )
                                    },
                                    onClick = {
                                        onEvent(ExerciseEvent.DifficultyChange(difficulty))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = state.equipment ?: "",
                        onValueChange = { onEvent(ExerciseEvent.EquipmentChange(it.takeIf { it.isNotBlank() })) },
                        label = { Text("Equipo (opcional)") },
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
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RetrofitColors.Gray
                            )
                        ) {
                            Text("Cancelar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                onEvent(ExerciseEvent.UpdateExercise)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RetrofitColors.Primary
                            ),
                            enabled = !state.isLoading
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text("Actualizar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}