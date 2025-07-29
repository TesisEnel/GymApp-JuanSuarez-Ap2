package edu.ucne.gymapp.presentation.users

import edu.ucne.gymapp.data.local.entities.User

data class UserUiState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: Long? = null,
    val gender: String = "",
    val generatedUsername: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val currentUser: User? = null,
    val isRegistered: Boolean = false,
    val isLoggedIn: Boolean = false
)