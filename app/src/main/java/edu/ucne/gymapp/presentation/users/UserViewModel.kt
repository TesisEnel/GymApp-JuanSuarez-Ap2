package edu.ucne.gymapp.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.entities.User
import edu.ucne.gymapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserUiState())
    val state = _state.asStateFlow()

    private fun registerUser() {
        viewModelScope.launch {
            val currentState = _state.value

            if (!isValidEmail(currentState.email)) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Formato de email inválido"
                    )
                }
                return@launch
            }

            if (!isValidPassword(currentState.password)) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
                    )
                }
                return@launch
            }

            if (!passwordsMatch(currentState.password, currentState.confirmPassword)) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Las contraseñas no coinciden"
                    )
                }
                return@launch
            }

            if (currentState.birthDate != null && !isValidAge(currentState.birthDate)) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Debes tener al menos 16 años para registrarte"
                    )
                }
                return@launch
            }

            val user = User(
                firstName = currentState.firstName.trim(),
                lastName = currentState.lastName.trim(),
                username = currentState.username.trim(),
                email = currentState.email.trim(),
                password = currentState.password,
                confirmPassword = currentState.confirmPassword,
                birthDate = currentState.birthDate ?: 0L,
                gender = currentState.gender
            )

            userRepository.registerUser(user).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRegistered = true,
                                successMessage = "Usuario registrado exitosamente",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al registrar usuario",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            val currentState = _state.value

            if (!isValidEmail(currentState.email)) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Formato de email inválido"
                    )
                }
                return@launch
            }

            userRepository.loginUser(currentState.email.trim(), currentState.password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                                successMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                currentUser = result.data,
                                successMessage = "Inicio de sesión exitoso",
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido al iniciar sesión",
                                successMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun generateUsername() {
        val currentState = _state.value
        if (currentState.firstName.isNotEmpty() && currentState.lastName.isNotEmpty()) {
            val username = "${currentState.firstName.first().lowercase()}${currentState.lastName.lowercase().replace(" ", "")}"
            _state.update { it.copy(generatedUsername = username, username = username) }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )
        return pattern.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\\\\|,.<>\\/?]).{8,}\$"
        )
        return pattern.matcher(password).matches()
    }

    private fun isValidAge(birthDate: Long): Boolean {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        val age = (currentTime - birthDate) / (365.25 * 24 * 60 * 60 * 1000)
        return age >= 16
    }

    private fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.FirstNameChange -> {
                _state.update { it.copy(firstName = event.firstName) }
            }
            is UserEvent.LastNameChange -> {
                _state.update { it.copy(lastName = event.lastName) }
            }
            is UserEvent.UsernameChange -> {
                _state.update { it.copy(username = event.username) }
            }
            is UserEvent.EmailChange -> {
                _state.update { it.copy(email = event.email) }
            }
            is UserEvent.PasswordChange -> {
                _state.update { it.copy(password = event.password) }
            }
            is UserEvent.ConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword) }
            }
            is UserEvent.BirthDateChange -> {
                _state.update { it.copy(birthDate = event.birthDate) }
            }
            is UserEvent.GenderChange -> {
                _state.update { it.copy(gender = event.gender) }
            }
            is UserEvent.GenerateUsername -> {
                generateUsername()
            }
            is UserEvent.RegisterUser -> {
                registerUser()
            }
            is UserEvent.LoginUser -> {
                loginUser()
            }
            is UserEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is UserEvent.ClearMessages -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }
        }
    }
}