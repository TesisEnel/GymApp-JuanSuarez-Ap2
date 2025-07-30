package edu.ucne.gymapp.presentation.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.ui.theme.RetrofitColors

@Composable
fun MainScreen(
    onNavigateToExercise: () -> Unit,
    onNavigateToRoutine: () -> Unit,
    onNavigateToWorkout: () -> Unit,
    onNavigateToMuscleGroup: () -> Unit,
    onNavigateToUserPreferences: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RetrofitColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = "Gym",
                modifier = Modifier.size(120.dp),
                tint = RetrofitColors.Primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡Bienvenido a RetroFit!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu compañero de entrenamiento",
                fontSize = 16.sp,
                color = RetrofitColors.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RetrofitColors.Surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Listo para entrenar?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetrofitColors.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Comienza tu entrenamiento ahora",
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onNavigateToWorkout,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RetrofitColors.Primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Comenzar Entrenamiento",
                            color = RetrofitColors.onPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    MainOptionCard(
                        title = "Rutinas",
                        subtitle = "Crear y gestionar",
                        icon = Icons.Default.List,
                        onClick = onNavigateToRoutine
                    )
                }

                item {
                    MainOptionCard(
                        title = "Ejercicios",
                        subtitle = "Ver catálogo",
                        icon = Icons.Default.FitnessCenter,
                        onClick = onNavigateToExercise
                    )
                }

                item {
                    MainOptionCard(
                        title = "Músculos",
                        subtitle = "Por grupo",
                        icon = Icons.Default.Accessibility,
                        onClick = onNavigateToMuscleGroup
                    )
                }

                item {
                    MainOptionCard(
                        title = "Perfil",
                        subtitle = "Configuración",
                        icon = Icons.Default.Person,
                        onClick = onNavigateToUserPreferences
                    )
                }
            }
        }
    }
}

@Composable
fun MainOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = RetrofitColors.Primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = RetrofitColors.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = RetrofitColors.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
