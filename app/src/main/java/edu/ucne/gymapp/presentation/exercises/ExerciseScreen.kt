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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

                Spacer(modifier = Modifier.width(48.dp))
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
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
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
                    Text(
                        text = exercise.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetrofitColors.onSurface
                    )

                    Text(
                        text = exercise.description,
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
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