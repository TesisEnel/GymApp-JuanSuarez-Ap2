package edu.ucne.gymapp.presentation.musclegroup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.ui.theme.RetrofitColors

@Composable
fun MuscleGroupScreen(
    state: MuscleGroupUiState,
    onEvent: (MuscleGroupEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExercise: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onEvent(MuscleGroupEvent.LoadAllMuscleGroups)
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
                    text = "Grupos Musculares",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar grupo muscular",
                        tint = RetrofitColors.Primary
                    )
                }
            }

            if (state.isLoading && state.muscleGroups.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = RetrofitColors.Primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else {
                LazyColumn (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.muscleGroups) { muscleGroup ->
                        MuscleGroupItem(
                            muscleGroup = muscleGroup,
                            onEdit = {
                                onEvent(MuscleGroupEvent.SelectMuscleGroup(muscleGroup))
                                showUpdateDialog = true
                            },
                            onDelete = {
                                onEvent(MuscleGroupEvent.SelectMuscleGroup(muscleGroup))
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }

            state.errorMessage?.let { errorMsg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
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
                            onClick = { onEvent(MuscleGroupEvent.ClearError) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            state.successMessage?.let { successMsg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
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
                            onClick = { onEvent(MuscleGroupEvent.ClearMessages) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Green,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        MuscleGroupDialog(
            title = "Crear Grupo Muscular",
            name = state.name,
            description = state.description,
            iconResource = state.iconResource,
            isLoading = state.isLoading,
            onNameChange = { onEvent(MuscleGroupEvent.NameChange(it)) },
            onDescriptionChange = { onEvent(MuscleGroupEvent.DescriptionChange(it)) },
            onIconResourceChange = { onEvent(MuscleGroupEvent.IconResourceChange(it)) },
            onConfirm = {
                onEvent(MuscleGroupEvent.CreateMuscleGroup)
                if (state.isCreated) {
                    showCreateDialog = false
                    onEvent(MuscleGroupEvent.ClearMessages)
                }
            },
            onDismiss = {
                showCreateDialog = false
                onEvent(MuscleGroupEvent.ClearMessages)
            }
        )
    }

    if (showUpdateDialog) {
        MuscleGroupDialog(
            title = "Actualizar Grupo Muscular",
            name = state.name,
            description = state.description,
            iconResource = state.iconResource,
            isLoading = state.isLoading,
            onNameChange = { onEvent(MuscleGroupEvent.NameChange(it)) },
            onDescriptionChange = { onEvent(MuscleGroupEvent.DescriptionChange(it)) },
            onIconResourceChange = { onEvent(MuscleGroupEvent.IconResourceChange(it)) },
            onConfirm = {
                onEvent(MuscleGroupEvent.UpdateMuscleGroup)
                if (state.isUpdated) {
                    showUpdateDialog = false
                    onEvent(MuscleGroupEvent.ClearMessages)
                }
            },
            onDismiss = {
                showUpdateDialog = false
                onEvent(MuscleGroupEvent.ClearMessages)
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = RetrofitColors.Surface,
            title = {
                Text(
                    text = "Eliminar Grupo Muscular",
                    color = RetrofitColors.onSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar '${state.selectedMuscleGroup?.name}'?",
                    color = RetrofitColors.onSurface
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(MuscleGroupEvent.DeleteMuscleGroup)
                        showDeleteDialog = false
                    },
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "Eliminar",
                            color = Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = "Cancelar",
                        color = RetrofitColors.Gray
                    )
                }
            }
        )
    }
}

@Composable
private fun MuscleGroupItem(
    muscleGroup: MuscleGroup,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = RetrofitColors.Primary.copy(alpha = 0.2f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = muscleGroup.name,
                            tint = RetrofitColors.Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = muscleGroup.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetrofitColors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (muscleGroup.description.isNotEmpty()) {
                        Text(
                            text = muscleGroup.description,
                            fontSize = 14.sp,
                            color = RetrofitColors.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = RetrofitColors.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = onDelete) {
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
}

@Composable
private fun MuscleGroupDialog(
    title: String,
    name: String,
    description: String,
    iconResource: String?,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIconResourceChange: (String?) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
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
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Nombre",
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
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Descripción (opcional)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Descripción",
                            tint = RetrofitColors.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
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
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading && name.trim().isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary,
                    disabledContainerColor = RetrofitColors.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = if (title.contains("Crear")) "Crear" else "Actualizar",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = RetrofitColors.Gray
                )
            }
        }
    )
}
