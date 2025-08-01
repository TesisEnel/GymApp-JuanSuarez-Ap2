package edu.ucne.gymapp.presentation.musclegroup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.ui.theme.RetrofitColors
import edu.ucne.gymapp.R

@Composable
fun MuscleGroupScreen(
    state: MuscleGroupUiState,
    onEvent: (MuscleGroupEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExercise: (MuscleGroup) -> Unit
) {
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
                Spacer(modifier = Modifier.width(48.dp))
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
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.muscleGroups) { muscleGroup ->
                        MuscleGroupItem(
                            muscleGroup = muscleGroup,
                            onClick = {
                                onEvent(MuscleGroupEvent.SelectMuscleGroup(muscleGroup))
                                onNavigateToExercise(muscleGroup)
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
}

@Composable
private fun MuscleGroupItem(
    muscleGroup: MuscleGroup,
    onClick: () -> Unit
) {
    val iconColor = Color(0xFF64B5F6)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
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
                        containerColor = iconColor.copy(alpha = 0.15f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = getMuscleGroupIcon(muscleGroup.iconResource),
                            contentDescription = muscleGroup.name,
                            tint = iconColor,
                            modifier = Modifier.size(36.dp)
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

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ver ejercicios",
                tint = RetrofitColors.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun getMuscleGroupIcon(iconResource: String?): Painter {
    return when (iconResource) {
        "chest" -> painterResource(R.drawable.chest_icon)
        "back" -> painterResource(R.drawable.back_icon)
        "shoulder" -> painterResource(R.drawable.shoulder_icon)
        "biceps" -> painterResource(R.drawable.biceps_icon)
        "triceps" -> painterResource(R.drawable.triceps_icon)
        "forearms" -> painterResource(R.drawable.forearms_icon)
        "abs" -> painterResource(R.drawable.abs_icon)
        "quadriceps" -> painterResource(R.drawable.quadriceps_icon)
        "hamstrings" -> painterResource(R.drawable.hamstrings_icon)
        "buttocks" -> painterResource(R.drawable.buttocks_icon)
        "calves" -> painterResource(R.drawable.calves_icon)
        "abductors1" -> painterResource(R.drawable.abductors_icon)
        "abductors2" -> painterResource(R.drawable.abductors_icon_2)
        "neck" -> painterResource(R.drawable.neck_icon)
        "trapeze" -> painterResource(R.drawable.trapeze_icon)
        "lower_back" -> painterResource(R.drawable.lower_back_icon)
        else -> painterResource(R.drawable.full_body_icon)
    }
}