package edu.ucne.gymapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.gymapp.data.local.dao.ExerciseDao
import edu.ucne.gymapp.data.local.dao.ExerciseSetDao
import edu.ucne.gymapp.data.local.dao.MuscleGroupDao
import edu.ucne.gymapp.data.local.dao.RoutineDao
import edu.ucne.gymapp.data.local.dao.RoutineExerciseDao
import edu.ucne.gymapp.data.local.dao.UserDao
import edu.ucne.gymapp.data.local.dao.UserPreferencesDao
import edu.ucne.gymapp.data.local.dao.WorkoutDao
import edu.ucne.gymapp.data.local.dao.WorkoutExerciseDao
import edu.ucne.gymapp.data.local.database.GymDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGymDatabase(@ApplicationContext context: Context): GymDatabase {
        return Room.databaseBuilder(
            context,
            GymDatabase::class.java,
            "gym_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: GymDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(database: GymDatabase): ExerciseDao {
        return database.exerciseDao()
    }

    @Provides
    @Singleton
    fun provideExerciseSetDao(database: GymDatabase): ExerciseSetDao {
        return database.exerciseSetDao()
    }

    @Provides
    @Singleton
    fun provideMuscleGroupDao(database: GymDatabase): MuscleGroupDao {
        return database.muscleGroupDao()
    }

    @Provides
    @Singleton
    fun provideRoutineDao(database: GymDatabase): RoutineDao {
        return database.routineDao()
    }

    @Provides
    @Singleton
    fun provideRoutineExerciseDao(database: GymDatabase): RoutineExerciseDao {
        return database.routineExerciseDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutDao(database: GymDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutExerciseDao(database: GymDatabase): WorkoutExerciseDao {
        return database.workoutExerciseDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDao(database: GymDatabase): UserPreferencesDao {
        return database.userPreferencesDao()
    }
}