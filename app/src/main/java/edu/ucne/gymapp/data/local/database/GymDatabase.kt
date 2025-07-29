package edu.ucne.gymapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.ucne.gymapp.data.local.dao.ExerciseDao
import edu.ucne.gymapp.data.local.dao.ExerciseSetDao
import edu.ucne.gymapp.data.local.dao.MuscleGroupDao
import edu.ucne.gymapp.data.local.dao.RoutineDao
import edu.ucne.gymapp.data.local.dao.RoutineExerciseDao
import edu.ucne.gymapp.data.local.dao.UserDao
import edu.ucne.gymapp.data.local.dao.UserPreferencesDao
import edu.ucne.gymapp.data.local.dao.WorkoutDao
import edu.ucne.gymapp.data.local.dao.WorkoutExerciseDao
import edu.ucne.gymapp.data.local.entities.Exercise
import edu.ucne.gymapp.data.local.entities.ExerciseSet
import edu.ucne.gymapp.data.local.entities.MuscleGroup
import edu.ucne.gymapp.data.local.entities.Routine
import edu.ucne.gymapp.data.local.entities.RoutineExercise
import edu.ucne.gymapp.data.local.entities.User
import edu.ucne.gymapp.data.local.entities.UserPreferences
import edu.ucne.gymapp.data.local.entities.Workout
import edu.ucne.gymapp.data.local.entities.WorkoutExercise

@Database(
    entities = [
        User::class,
        Exercise::class,
        ExerciseSet::class,
        MuscleGroup::class,
        Routine::class,
        RoutineExercise::class,
        Workout::class,
        WorkoutExercise::class,
        UserPreferences::class
    ],
    version = 12,
    exportSchema = false
)
abstract class GymDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseSetDao(): ExerciseSetDao
    abstract fun muscleGroupDao(): MuscleGroupDao
    abstract fun routineDao(): RoutineDao
    abstract fun routineExerciseDao(): RoutineExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun userPreferencesDao(): UserPreferencesDao
}