package edu.ucne.gymapp.data.repository

import edu.ucne.gymapp.data.local.Resource
import edu.ucne.gymapp.data.local.dao.UserPreferencesDao
import edu.ucne.gymapp.data.local.entities.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDao: UserPreferencesDao
) {
    fun insertPreferences(userPreferences: UserPreferences): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val preferencesId = userPreferencesDao.insertPreferences(userPreferences)
            emit(Resource.Success(preferencesId))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudieron guardar tus preferencias. Intenta nuevamente o revisa tu conexión."))
        }
    }.flowOn(Dispatchers.IO)

    fun insertOrUpdatePreferences(userPreferences: UserPreferences): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.insertOrUpdatePreferences(userPreferences)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Algo salió mal al intentar guardar tus ajustes. Por favor, verifica los datos."))
        }
    }.flowOn(Dispatchers.IO)

    fun updatePreferences(userPreferences: UserPreferences): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updatePreferences(userPreferences)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error inesperado al actualizar tus preferencias. Inténtalo de nuevo más tarde."))
        }
    }.flowOn(Dispatchers.IO)

    fun deletePreferences(userPreferences: UserPreferences): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.deletePreferences(userPreferences)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudieron eliminar las preferencias. Asegúrate de que existen."))
        }
    }.flowOn(Dispatchers.IO)

    fun getPreferencesByUser(userId: Int): Flow<Resource<UserPreferences?>> = flow {
        try {
            emit(Resource.Loading())
            val preferences = userPreferencesDao.getPreferencesByUser(userId)
            emit(Resource.Success(preferences))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No fue posible recuperar las preferencias del usuario. Verifica el ID o la conexión."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateDefaultRestTime(userId: Int, restTime: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateDefaultRestTime(userId, restTime)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo cambiar el tiempo de descanso. ¿Probamos de nuevo?"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateWeightUnit(userId: Int, weightUnit: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateWeightUnit(userId, weightUnit)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos cambiar la unidad de peso. Verifica el formato ingresado."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateAutoVideoPlay(userId: Int, autoPlay: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateAutoVideoPlay(userId, autoPlay)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Hubo un error al cambiar la reproducción automática. Inténtalo de nuevo."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateVideoQuality(userId: Int, videoQuality: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateVideoQuality(userId, videoQuality)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No fue posible actualizar la calidad del video. Verifica el valor ingresado."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateNotifications(userId: Int, notificationsEnabled: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateNotifications(userId, notificationsEnabled)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Ocurrió un problema al cambiar la configuración de notificaciones."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateDarkMode(userId: Int, darkMode: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateDarkMode(userId, darkMode)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No se pudo activar/desactivar el modo oscuro. Inténtalo más tarde."))
        }
    }.flowOn(Dispatchers.IO)

    fun updateKeepScreenOn(userId: Int, keepScreenOn: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userPreferencesDao.updateKeepScreenOn(userId, keepScreenOn)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "No pudimos mantener la pantalla encendida como solicitaste."))
        }
    }.flowOn(Dispatchers.IO)
}