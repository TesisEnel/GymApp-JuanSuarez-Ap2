package edu.ucne.gymapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val birthDate: Long,
    val gender: String,
    val createdAt: Long = System.currentTimeMillis()
)