package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import edu.ucne.gymapp.data.local.entities.User
import edu.ucne.gymapp.data.local.relation.UserWithPreferences
import edu.ucne.gymapp.data.local.relation.UserWithRoutines
import edu.ucne.gymapp.data.local.relation.UserWithWorkouts

@Dao
interface UserDao{
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun loginUser(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserWithRoutines(userId: Int): UserWithRoutines?

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserWithWorkouts(userId: Int): UserWithWorkouts?

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserWithPreferences(userId: Int): UserWithPreferences?
}