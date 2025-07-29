package edu.ucne.gymapp.presentation.users


sealed interface UserEvent {
    data class FirstNameChange(val firstName: String) : UserEvent
    data class LastNameChange(val lastName: String) : UserEvent
    data class UsernameChange(val username: String) : UserEvent
    data class EmailChange(val email: String) : UserEvent
    data class PasswordChange(val password: String) : UserEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : UserEvent
    data class BirthDateChange(val birthDate: Long) : UserEvent
    data class GenderChange(val gender: String) : UserEvent
    data object GenerateUsername : UserEvent
    data object RegisterUser : UserEvent
    data object LoginUser : UserEvent
    data object ClearError : UserEvent
    data object ClearMessages : UserEvent
}