package edu.ucne.gymapp.presentation.exercises

import edu.ucne.gymapp.data.local.entities.Exercise

sealed interface ExerciseEvent {
    data class NameChange(val name: String) : ExerciseEvent
    data class DescriptionChange(val description: String) : ExerciseEvent
    data class InstructionsChange(val instructions: String) : ExerciseEvent
    data class MuscleGroupChange(val muscleGroupId: Int) : ExerciseEvent
    data class DifficultyChange(val difficulty: String) : ExerciseEvent
    data class VideoUrlChange(val videoUrl: String?) : ExerciseEvent
    data class ThumbnailUrlChange(val thumbnailUrl: String?) : ExerciseEvent
    data class VideoDurationChange(val videoDuration: Int?) : ExerciseEvent
    data class IsVideoAvailableChange(val isVideoAvailable: Boolean) : ExerciseEvent
    data class EquipmentChange(val equipment: String?) : ExerciseEvent
    data class TipsChange(val tips: String?) : ExerciseEvent
    data class CommonMistakesChange(val commonMistakes: String?) : ExerciseEvent
    data class LoadExerciseById(val id: Int) : ExerciseEvent
    data class LoadExercisesByMuscleGroup(val muscleGroupId: Int) : ExerciseEvent
    data class LoadExercisesByDifficulty(val difficulty: String) : ExerciseEvent
    data class LoadExercisesByMuscleGroupsAndDifficulties(
        val muscleGroupIds: List<Int>,
        val difficulties: List<String>
    ) : ExerciseEvent
    data class IncrementPopularity(val exerciseId: Int) : ExerciseEvent
    data class FilterByDifficulties(val difficulties: List<String>) : ExerciseEvent
    data class FilterByMuscleGroups(val muscleGroupIds: List<Int>) : ExerciseEvent
    data class InsertPredefinedExercises(val exercises: List<Exercise>) : ExerciseEvent
    data object LoadExercisesByPopularity : ExerciseEvent
    data object CreateExercise : ExerciseEvent
    data object UpdateExercise : ExerciseEvent
    data object DeleteExercise : ExerciseEvent
    data object LoadAllExercises : ExerciseEvent
    data object ClearError : ExerciseEvent
    data object ClearMessages : ExerciseEvent
}