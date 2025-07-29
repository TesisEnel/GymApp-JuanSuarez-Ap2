package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.UserDao
import edu.ucne.gymapp.data.local.entities.User
import edu.ucne.gymapp.data.local.relation.UserWithRoutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
){
    fun registerUser(user: User): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())

            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                emit(Resource.Error("El email ya esta registrado"))
                return@flow
            }

            val existingUsername = userDao.getUserByUsername(user.username)
            if (existingUsername != null) {
                emit(Resource.Error("Ese nombre de usuario ya esta en uso"))
                return@flow
            }

            val userId = userDao.insertUser(user)
            emit(Resource.Success(userId))

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido al momento de registrar el usuario"))
        }
    }.flowOn(Dispatchers.IO)

    fun loginUser(email: String, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val user = userDao.loginUser(email, password)

            if (user != null) {
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Credenciales incorrectas"))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido al momento de iniciar sesion"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserWithRoutines(userId: Int): Flow<Resource<UserWithRoutines?>> = flow {
        try {
            emit(Resource.Loading())
            val userWithRoutines = userDao.getUserWithRoutines(userId)
            emit(Resource.Success(userWithRoutines))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se encontraron rutinas"))
        }
    }.flowOn(Dispatchers.IO)
}