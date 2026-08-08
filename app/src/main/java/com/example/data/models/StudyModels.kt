package com.example.data.models

data class OnboardingData(
    val examTarget: String = "HSC Preparation",
    val subjects: List<String> = listOf("Physics", "Chemistry", "Biology", "Higher Math", "English", "ICT"),
    val dailyHours: String = "4 hours",
    val goal: String = "Finish syllabus",
    val isCompleted: Boolean = true
)

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

data class Task(
    val id: String,
    val subject: String,
    val topic: String,
    val estimatedMinutes: Int,
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val scheduledDay: String = "TODAY",
    val chapterName: String? = null
)

enum class ChapterStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

data class Chapter(
    val id: String,
    val title: String,
    val status: ChapterStatus = ChapterStatus.NOT_STARTED,
    val description: String = "",
    val progressPercent: Int = when (status) {
        ChapterStatus.COMPLETED -> 100
        ChapterStatus.IN_PROGRESS -> 50
        ChapterStatus.NOT_STARTED -> 0
    }
)

data class Subject(
    val id: String,
    val name: String,
    val chapters: List<Chapter> = emptyList()
) {
    val completedChaptersCount: Int
        get() = chapters.count { it.status == ChapterStatus.COMPLETED }

    val completionPercentage: Int
        get() = if (chapters.isEmpty()) 0 else {
            val totalPercent = chapters.sumOf { chapter ->
                when (chapter.status) {
                    ChapterStatus.COMPLETED -> 100
                    ChapterStatus.NOT_STARTED -> chapter.progressPercent.coerceAtLeast(0)
                    ChapterStatus.IN_PROGRESS -> if (chapter.progressPercent > 0) chapter.progressPercent else 50
                }
            }
            (totalPercent / chapters.size).coerceIn(0, 100)
        }
}

data class DayPlan(
    val dayCode: String,
    val dayName: String,
    val focusArea: String,
    val subjects: List<String>
)

data class StudyStats(
    val overallProgress: Int = 68,
    val weeklyStudyHours: String = "24h 30m",
    val completedTasksCount: Int = 27,
    val totalTasksCount: Int = 32,
    val streakDays: Int = 5,
    val dailyStudyHours: List<Float> = listOf(4.5f, 5.0f, 3.0f, 6.0f, 4.0f, 5.5f, 2.0f)
)

data class DailyGoal(
    val targetMinutes: Int = 240, // Default 4 hours (240 minutes)
    val completedMinutes: Int = 155 // Default 2 hours 35 minutes
) {
    val progressFraction: Float
        get() = if (targetMinutes <= 0) 0f else (completedMinutes.toFloat() / targetMinutes.toFloat()).coerceIn(0f, 1f)

    val remainingMinutes: Int
        get() = (targetMinutes - completedMinutes).coerceAtLeast(0)

    val isGoalReached: Boolean
        get() = completedMinutes >= targetMinutes && targetMinutes > 0
}

data class StudyReminder(
    val id: String,
    val title: String,
    val subject: String,
    val chapterOrTopic: String = "",
    val date: String = "আজ",
    val time: String = "৮:০০ PM",
    val isEnabled: Boolean = true
)

