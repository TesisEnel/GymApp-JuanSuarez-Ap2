package edu.ucne.gymapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.gymapp.presentation.users.UserViewModel
import androidx.compose.runtime.getValue
import edu.ucne.gymapp.presentation.exercises.ExerciseScreen
import edu.ucne.gymapp.presentation.exercises.ExerciseViewModel
import edu.ucne.gymapp.presentation.exercisesets.ExerciseSetScreen
import edu.ucne.gymapp.presentation.exercisesets.ExerciseSetViewModel
import edu.ucne.gymapp.presentation.mainscreen.MainScreen
import edu.ucne.gymapp.presentation.musclegroup.MuscleGroupScreen
import edu.ucne.gymapp.presentation.musclegroup.MuscleGroupViewModel
import edu.ucne.gymapp.presentation.routine.RoutineScreen
import edu.ucne.gymapp.presentation.routine.RoutineViewModel
import edu.ucne.gymapp.presentation.routinexercise.RoutineExerciseScreen
import edu.ucne.gymapp.presentation.routinexercise.RoutineExerciseViewModel
import edu.ucne.gymapp.presentation.userpreferences.UserPreferencesEvent
import edu.ucne.gymapp.presentation.userpreferences.UserPreferencesScreen
import edu.ucne.gymapp.presentation.userpreferences.UserPreferencesViewModel
import edu.ucne.gymapp.presentation.users.LoginScreen
import edu.ucne.gymapp.presentation.users.RegisterScreen
import edu.ucne.gymapp.presentation.workoutexercises.WorkoutExerciseScreen
import edu.ucne.gymapp.presentation.workoutexercises.WorkoutExerciseViewModel
import edu.ucne.gymapp.presentation.workouts.WorkoutScreen
import edu.ucne.gymapp.presentation.workouts.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.state.collectAsState()

    val exerciseViewModel: ExerciseViewModel = hiltViewModel()
    val exerciseState by exerciseViewModel.state.collectAsState()

    val exerciseSetViewModel: ExerciseSetViewModel = hiltViewModel()
    val exerciseSetState by exerciseSetViewModel.state.collectAsState()

    val muscleGroupViewModel: MuscleGroupViewModel = hiltViewModel()
    val muscleGroupState by muscleGroupViewModel.state.collectAsState()

    val routineViewModel: RoutineViewModel = hiltViewModel()
    val routineState by routineViewModel.state.collectAsState()

    val routineExerciseViewModel: RoutineExerciseViewModel = hiltViewModel()
    val routineExerciseState by routineExerciseViewModel.state.collectAsState()

    val workoutViewModel: WorkoutViewModel = hiltViewModel()
    val workoutState by workoutViewModel.state.collectAsState()

    val workoutExerciseViewModel: WorkoutExerciseViewModel = hiltViewModel()
    val workoutExerciseState by workoutExerciseViewModel.state.collectAsState()

    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
    val userPreferencesState by userPreferencesViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ) {
        composable<Screen.Login> {
            LoginScreen(
                state = userState,
                onEvent = userViewModel::onEvent,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )

            LaunchedEffect(userState.isLoggedIn) {
                if (userState.isLoggedIn) {
                    navController.navigate(Screen.Main) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            }
        }

        composable<Screen.Register> {
            RegisterScreen(
                state = userState,
                onEvent = userViewModel::onEvent,
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )

            LaunchedEffect(userState.isRegistered) {
                if (userState.isRegistered) {
                    navController.navigateUp()
                }
            }
        }

        composable<Screen.Main> {
            MainScreen(
                onNavigateToExercise = {
                    navController.navigate(Screen.Exercise)
                },
                onNavigateToRoutine = {
                    navController.navigate(Screen.Routine)
                },
                onNavigateToWorkout = {
                    navController.navigate(Screen.Workout)
                },
                onNavigateToMuscleGroup = {
                    navController.navigate(Screen.MuscleGroup)
                },
                onNavigateToUserPreferences = {
                    navController.navigate(Screen.UserPreferences)
                }
            )
        }

        composable<Screen.Exercise> {
            ExerciseScreen(
                state = exerciseState,
                onEvent = exerciseViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToExerciseSet = {
                    navController.navigate(Screen.ExerciseSet)
                }
            )
        }

        composable<Screen.ExerciseSet> {
            ExerciseSetScreen(
                state = exerciseSetState,
                onEvent = exerciseSetViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.MuscleGroup> {
            MuscleGroupScreen(
                state = muscleGroupState,
                onEvent = muscleGroupViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToExercise = {
                    navController.navigate(Screen.Exercise)
                }
            )
        }

        composable<Screen.Routine> {
            RoutineScreen(
                state = routineState,
                onEvent = routineViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToRoutineExercise = {
                    navController.navigate(Screen.RoutineExercise)
                }
            )
        }

        composable<Screen.RoutineExercise> {
            RoutineExerciseScreen(
                state = routineExerciseState,
                onEvent = routineExerciseViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToExercise = {
                    navController.navigate(Screen.Exercise)
                }
            )
        }

        composable<Screen.Workout> {
            WorkoutScreen(
                state = workoutState,
                onEvent = workoutViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToWorkoutExercise = {
                    navController.navigate(Screen.WorkoutExercise)
                }
            )
        }

        composable<Screen.WorkoutExercise> {
            WorkoutExerciseScreen(
                state = workoutExerciseState,
                onEvent = workoutExerciseViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToExerciseSet = {
                    navController.navigate(Screen.ExerciseSet)
                }
            )
        }

        composable<Screen.UserPreferences> {
            UserPreferencesScreen(
                state = userPreferencesState,
                onEvent = userPreferencesViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                /*onLogout = {
                    userPreferencesViewModel.onEvent(UserPreferencesEvent.Logout)
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }*/
            )
        }
    }
}