package edu.ucne.gymapp.presentation.users

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.gymapp.ui.theme.RetrofitColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: UserUiState,
    onEvent: (UserEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigateToLogin() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Regresar",
                        tint = RetrofitColors.onSurface
                    )
                }

                Text(
                    text = "Crear cuenta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetrofitColors.onSurface
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Unete a la familia de los gymbros!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RetrofitColors.Primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Comienza tu transformacion hoy mismo!",
                fontSize = 16.sp,
                color = RetrofitColors.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.firstName,
                onValueChange = { onEvent(UserEvent.FirstNameChange(it)) },
                label = { Text("Nombre") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nombre",
                        tint = RetrofitColors.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.lastName,
                onValueChange = { onEvent(UserEvent.LastNameChange(it)) },
                label = { Text("Apellido") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Apellido",
                        tint = RetrofitColors.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.username,
                onValueChange = { onEvent(UserEvent.UsernameChange(it)) },
                label = { Text("Nombre de usuario") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Nombre de usuario",
                        tint = RetrofitColors.Gray
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { onEvent(UserEvent.GenerateUsername) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Generar nombre de usuario",
                            tint = RetrofitColors.Primary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                )
            )

            Text(
                text = "Se genera automáticamente el nombre de usuario",
                color = RetrofitColors.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(UserEvent.EmailChange(it)) },
                label = { Text("Correo electrónico") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo electrónico",
                        tint = RetrofitColors.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(UserEvent.PasswordChange(it)) },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = RetrofitColors.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = RetrofitColors.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Text(
                text = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial",
                color = RetrofitColors.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { onEvent(UserEvent.ConfirmPasswordChange(it)) },
                label = { Text("Confirmar contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirmar contraseña",
                        tint = RetrofitColors.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = RetrofitColors.Gray
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (state.birthDate != null && state.birthDate != 0L) {
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(Date(state.birthDate!!))
                } else "",
                onValueChange = { },
                label = { Text("Fecha de nacimiento") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Fecha de nacimiento",
                        tint = RetrofitColors.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha",
                            tint = RetrofitColors.Primary
                        )
                    }
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showDatePicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetrofitColors.Primary,
                    unfocusedBorderColor = RetrofitColors.Gray,
                    focusedLabelColor = RetrofitColors.Primary,
                    unfocusedLabelColor = RetrofitColors.Gray,
                    focusedTextColor = RetrofitColors.onSurface,
                    unfocusedTextColor = RetrofitColors.onSurface,
                    cursorColor = RetrofitColors.Primary,
                    focusedLeadingIconColor = RetrofitColors.Primary
                )
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    onEvent(UserEvent.BirthDateChange(millis))
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false }
                        ) {
                            Text("Cancelar", color = RetrofitColors.Gray)
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = RetrofitColors.Primary,
                            todayDateBorderColor = RetrofitColors.Primary,
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Genero",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RetrofitColors.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onEvent(UserEvent.GenderChange("Masculino")) }
                        .background(
                            if(state.gender == "Masculino")
                                RetrofitColors.Primary.copy(alpha = 0.1f)
                            else
                                RetrofitColors.Surface
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.gender == "Masculino",
                        onClick = { onEvent(UserEvent.GenderChange("Masculino")) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = RetrofitColors.Primary,
                            unselectedColor = RetrofitColors.Gray
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Masculino",
                        color = RetrofitColors.onSurface,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onEvent(UserEvent.GenderChange("Femenino")) }
                        .background(
                            if(state.gender == "Femenino")
                                RetrofitColors.Primary.copy(alpha = 0.1f)
                            else
                                RetrofitColors.Surface
                        )
                        .padding(16.dp),
                ) {
                    RadioButton(
                        selected = state.gender == "Femenino",
                        onClick = { onEvent(UserEvent.GenderChange("Femenino")) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = RetrofitColors.Primary,
                            unselectedColor = RetrofitColors.Gray
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Femenino",
                        color = RetrofitColors.onSurface,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onEvent(UserEvent.RegisterUser) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RetrofitColors.Primary
                ),
                enabled = !state.isLoading
            ){
                if(state.isLoading){
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Registrarse",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta?",
                    color = RetrofitColors.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = " Inicia sesión",
                    color = RetrofitColors.Primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.errorMessage?.let { errorMsg ->
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
    }
}