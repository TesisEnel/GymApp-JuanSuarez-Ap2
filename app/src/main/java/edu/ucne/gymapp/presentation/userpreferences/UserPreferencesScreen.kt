package edu.ucne.gymapp.presentation.userpreferences

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VideoSettings
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.ui.theme.RetrofitColors

@Composable
fun UserPreferencesScreen(
    state: UserPreferencesUiState,
    onEvent: (UserPreferencesEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showRestTimeDialog by remember { mutableStateOf(false) }
    var tempRestTime by remember { mutableStateOf(state.defaultRestTime.toString()) }

    LaunchedEffect(state.defaultRestTime) {
        tempRestTime = state.defaultRestTime.toString()
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
                        tint = RetrofitColors.onSurface
                    )
                }

                Text(
                    text = "Preferencias",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )

                IconButton(onClick = { onEvent(UserPreferencesEvent.ResetToDefaults) }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restablecer",
                        tint = RetrofitColors.Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    PreferenceSectionTitle("Entrenamiento")
                }

                item {
                    PreferenceCard(
                        title = "Tiempo de descanso por defecto",
                        subtitle = "${state.defaultRestTime} segundos",
                        icon = Icons.Default.Timer,
                        onClick = { showRestTimeDialog = true }
                    )
                }

                item {
                    PreferenceDropdownCard(
                        title = "Unidad de peso",
                        subtitle = state.weightUnit,
                        icon = Icons.Default.FitnessCenter,
                        options = state.availableWeightUnits,
                        selectedOption = state.weightUnit,
                        onOptionSelected = { onEvent(UserPreferencesEvent.WeightUnitChange(it)) }
                    )
                }

                item {
                    PreferenceSectionTitle("Video y Multimedia")
                }

                item {
                    PreferenceSwitchCard(
                        title = "Reproducción automática de videos",
                        subtitle = "Los videos se reproducirán automáticamente",
                        icon = Icons.Default.PlayArrow,
                        checked = state.autoVideoPlay,
                        onCheckedChange = { onEvent(UserPreferencesEvent.AutoVideoPlayChange(it)) }
                    )
                }

                item {
                    PreferenceDropdownCard(
                        title = "Calidad de video",
                        subtitle = state.videoQuality,
                        icon = Icons.Default.VideoSettings,
                        options = state.availableVideoQualities,
                        selectedOption = state.videoQuality,
                        onOptionSelected = { onEvent(UserPreferencesEvent.VideoQualityChange(it)) }
                    )
                }

                item {
                    PreferenceSectionTitle("General")
                }

                item {
                    PreferenceSwitchCard(
                        title = "Notificaciones",
                        subtitle = "Recibir notificaciones de la app",
                        icon = Icons.Default.Notifications,
                        checked = state.notificationsEnabled,
                        onCheckedChange = { onEvent(UserPreferencesEvent.NotificationsEnabledChange(it)) }
                    )
                }

                item {
                    PreferenceSwitchCard(
                        title = "Modo oscuro",
                        subtitle = "Usar tema oscuro en la aplicación",
                        icon = Icons.Default.DarkMode,
                        checked = state.darkMode,
                        onCheckedChange = { onEvent(UserPreferencesEvent.DarkModeChange(it)) }
                    )
                }

                item {
                    PreferenceSwitchCard(
                        title = "Mantener pantalla encendida",
                        subtitle = "La pantalla no se apagará durante los entrenamientos",
                        icon = Icons.Default.ScreenLockRotation,
                        checked = state.keepScreenOn,
                        onCheckedChange = { onEvent(UserPreferencesEvent.KeepScreenOnChange(it)) }
                    )
                }
            }

            if (state.hasUnsavedChanges) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onEvent(UserPreferencesEvent.SaveAllPreferences) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetrofitColors.Primary
                    ),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Guardar cambios",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
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
                        IconButton(onClick = { onEvent(UserPreferencesEvent.ClearError) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
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
                        IconButton(onClick = { onEvent(UserPreferencesEvent.ClearMessages) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Green,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showRestTimeDialog) {
        AlertDialog(
            onDismissRequest = { showRestTimeDialog = false },
            containerColor = RetrofitColors.Surface,
            title = {
                Text(
                    text = "Tiempo de descanso",
                    color = RetrofitColors.onSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Ingresa el tiempo de descanso por defecto (1-300 segundos)",
                        color = RetrofitColors.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempRestTime,
                        onValueChange = { tempRestTime = it },
                        label = { Text("Segundos") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                TextButton(
                    onClick = {
                        val restTime = tempRestTime.toIntOrNull()
                        if (restTime != null && restTime in 1..300) {
                            onEvent(UserPreferencesEvent.DefaultRestTimeChange(restTime))
                            showRestTimeDialog = false
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = RetrofitColors.Primary
                    )
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRestTimeDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = RetrofitColors.Gray
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PreferenceSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = RetrofitColors.Primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun PreferenceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RetrofitColors.Primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetrofitColors.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = RetrofitColors.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PreferenceSwitchCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RetrofitColors.Primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetrofitColors.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = RetrofitColors.Gray
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = RetrofitColors.Primary,
                    uncheckedThumbColor = RetrofitColors.Gray,
                    uncheckedTrackColor = RetrofitColors.Background
                )
            )
        }
    }
}

@Composable
private fun PreferenceDropdownCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = RetrofitColors.Surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = RetrofitColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = RetrofitColors.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = RetrofitColors.Gray
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = RetrofitColors.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (expanded) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                expanded = false
                            }
                            .padding(horizontal = 56.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = RetrofitColors.Primary,
                                unselectedColor = RetrofitColors.Gray
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            color = RetrofitColors.onSurface
                        )
                    }
                }
            }
        }
    }
}
