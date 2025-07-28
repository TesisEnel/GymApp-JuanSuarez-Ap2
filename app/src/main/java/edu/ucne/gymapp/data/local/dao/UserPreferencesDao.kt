package edu.ucne.gymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.ucne.gymapp.data.local.entities.UserPreferences

@Dao
interface UserPreferencesDao {
    @Insert
    suspend fun insertPreferences(userPreferences: UserPreferences) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePreferences(userPreferences: UserPreferences)

    @Update
    suspend fun updatePreferences(userPreferences: UserPreferences)

    @Delete
    suspend fun deletePreferences(userPreferences: UserPreferences)

    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    suspend fun getPreferencesByUser(userId: Int): UserPreferences?

    @Query("UPDATE user_preferences SET defaultRestTime = :restTime WHERE userId = :userId")
    suspend fun updateDefaultRestTime(userId: Int, restTime: Int)

    @Query("UPDATE user_preferences SET weightUnit = :weightUnit WHERE userId = :userId")
    suspend fun updateWeightUnit(userId: Int, weightUnit: String)

    @Query("UPDATE user_preferences SET autoVideoPlay = :autoPlay WHERE userId = :userId")
    suspend fun updateAutoVideoPlay(userId: Int, autoPlay: Boolean)

    @Query("UPDATE user_preferences SET videoQuality = :videoQuality WHERE userId = :userId")
    suspend fun updateVideoQuality(userId: Int, videoQuality: String)

    @Query("UPDATE user_preferences SET notificationsEnabled = :notificationsEnabled WHERE userId = :userId")
    suspend fun updateNotifications(userId: Int, notificationsEnabled: Boolean)

    @Query("UPDATE user_preferences SET darkMode = :darkMode WHERE userId = :userId")
    suspend fun updateDarkMode(userId: Int, darkMode: Boolean)

    @Query("UPDATE user_preferences SET keepScreenOn = :keepScreenOn WHERE userId = :userId")
    suspend fun updateKeepScreenOn(userId: Int, keepScreenOn: Boolean)
}