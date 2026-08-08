package com.example.data.repository

import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class StudyFlowRepository {

    private val _onboardingData = MutableStateFlow(
        OnboardingData(
            examTarget = "এইচএসসি প্রস্তুতি",
            subjects = listOf("পদার্থবিজ্ঞান", "রসায়ন", "জীববিজ্ঞান", "উচ্চতর গণিত", "ইংরেজি", "আইসিটি"),
            dailyHours = "৪ ঘণ্টা",
            goal = "সিলেবাস শেষ করা",
            isCompleted = true
        )
    )
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    private val _examCountdownDays = MutableStateFlow(127)
    val examCountdownDays: StateFlow<Int> = _examCountdownDays.asStateFlow()

    private val _examName = MutableStateFlow("এইচএসসি ও ভর্তি পরীক্ষা")
    val examName: StateFlow<String> = _examName.asStateFlow()

    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                id = "1",
                subject = "পদার্থবিজ্ঞান",
                topic = "চল তড়িৎ - সূত্র ও গাণিতিক সমস্যা",
                estimatedMinutes = 60,
                isCompleted = false,
                priority = TaskPriority.HIGH,
                scheduledDay = "TODAY"
            ),
            Task(
                id = "2",
                subject = "রসায়ন",
                topic = "জৈব রসায়ন - নামকরণের নিয়ম",
                estimatedMinutes = 60,
                isCompleted = false,
                priority = TaskPriority.HIGH,
                scheduledDay = "TODAY"
            ),
            Task(
                id = "3",
                subject = "জীববিজ্ঞান",
                topic = "জিনতত্ত্ব ও বিবর্তন - মেন্ডেলের সূত্র",
                estimatedMinutes = 45,
                isCompleted = true,
                priority = TaskPriority.MEDIUM,
                scheduledDay = "TODAY"
            ),
            Task(
                id = "4",
                subject = "উচ্চতর গণিত",
                topic = "যোগজীকরণ (Integration) - অনুশীলনী ৭.১",
                estimatedMinutes = 60,
                isCompleted = false,
                priority = TaskPriority.HIGH,
                scheduledDay = "TODAY"
            ),
            Task(
                id = "5",
                subject = "রিভিশন",
                topic = "পূর্বের পড়া রিভিশন ও নোট তৈরি",
                estimatedMinutes = 30,
                isCompleted = true,
                priority = TaskPriority.LOW,
                scheduledDay = "TODAY"
            )
        )
    )
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _subjects = MutableStateFlow(
        listOf(
            Subject(
                id = "sub_physics",
                name = "পদার্থবিজ্ঞান",
                chapters = listOf(
                    Chapter("ch_p1", "ভেক্টর", ChapterStatus.COMPLETED),
                    Chapter("ch_p2", "নিউটনীয় বলবিজ্ঞান", ChapterStatus.COMPLETED),
                    Chapter("ch_p3", "চল তড়িৎ", ChapterStatus.IN_PROGRESS),
                    Chapter("ch_p4", "তড়িৎ চৌম্বকীয় আবেশ", ChapterStatus.NOT_STARTED),
                    Chapter("ch_p5", "আধুনিক পদার্থবিজ্ঞানের সূচনা", ChapterStatus.NOT_STARTED)
                )
            ),
            Subject(
                id = "sub_chem",
                name = "রসায়ন",
                chapters = listOf(
                    Chapter("ch_c1", "পর্যাবৃত্ত ধর্ম ও রাসায়নিক বন্ধন", ChapterStatus.COMPLETED),
                    Chapter("ch_c2", "গুণগত রসায়ন", ChapterStatus.COMPLETED),
                    Chapter("ch_c3", "জৈব রসায়ন", ChapterStatus.IN_PROGRESS),
                    Chapter("ch_c4", "রাসায়নিক পরিবর্তন", ChapterStatus.NOT_STARTED),
                    Chapter("ch_c5", "তড়িৎ রসায়ন", ChapterStatus.NOT_STARTED)
                )
            ),
            Subject(
                id = "sub_bio",
                name = "জীববিজ্ঞান",
                chapters = listOf(
                    Chapter("ch_b1", "কোষ ও এর গঠন", ChapterStatus.COMPLETED),
                    Chapter("ch_b2", "উদ্ভিদ শারীরতত্ত্ব", ChapterStatus.COMPLETED),
                    Chapter("ch_b3", "জিনতত্ত্ব ও বিবর্তন", ChapterStatus.IN_PROGRESS),
                    Chapter("ch_b4", "মানব শারীরতত্ত্ব", ChapterStatus.NOT_STARTED),
                    Chapter("ch_b5", "জীবপ্রযুক্তি", ChapterStatus.NOT_STARTED)
                )
            ),
            Subject(
                id = "sub_math",
                name = "উচ্চতর গণিত",
                chapters = listOf(
                    Chapter("ch_m1", "ম্যাট্রিক্স ও নির্ণায়ক", ChapterStatus.COMPLETED),
                    Chapter("ch_m2", "অন্তরীকরণ (Differentiation)", ChapterStatus.IN_PROGRESS),
                    Chapter("ch_m3", "যোগজীকরণ (Integration)", ChapterStatus.IN_PROGRESS),
                    Chapter("ch_m4", "স্থানাঙ্ক জ্যামিতি", ChapterStatus.NOT_STARTED),
                    Chapter("ch_m5", "ত্রিকোণমিতি ও ভেক্টর", ChapterStatus.NOT_STARTED)
                )
            ),
            Subject(
                id = "sub_eng",
                name = "ইংরেজি",
                chapters = listOf(
                    Chapter("ch_e1", "Grammar & Syntax", ChapterStatus.COMPLETED),
                    Chapter("ch_e2", "Vocabulary & Synonyms", ChapterStatus.COMPLETED),
                    Chapter("ch_e3", "Comprehension & Passage", ChapterStatus.COMPLETED),
                    Chapter("ch_e4", "Composition & Writing", ChapterStatus.IN_PROGRESS)
                )
            ),
            Subject(
                id = "sub_ict",
                name = "আইসিটি",
                chapters = listOf(
                    Chapter("ch_i1", "তথ্য ও যোগাযোগ প্রযুক্তি বিশ্ব ও বাংলাদেশ", ChapterStatus.COMPLETED),
                    Chapter("ch_i2", "ওয়েব ডিজাইন পরিচিতি ও HTML", ChapterStatus.COMPLETED),
                    Chapter("ch_i3", "প্রোগ্রামিং ভাষা (C Programming)", ChapterStatus.COMPLETED),
                    Chapter("ch_i4", "ডেটাবেজ ম্যানেজমেন্ট সিস্টেম", ChapterStatus.IN_PROGRESS)
                )
            )
        )
    )
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _weeklyPlan = MutableStateFlow(
        listOf(
            DayPlan("সোম", "সোমবার", "পদার্থবিজ্ঞান + রসায়ন", listOf("পদার্থবিজ্ঞান", "রসায়ন")),
            DayPlan("মঙ্গল", "মঙ্গলবার", "জীববিজ্ঞান + উচ্চতর গণিত", listOf("জীববিজ্ঞান", "উচ্চতর গণিত")),
            DayPlan("বুধ", "বুধবার", "পদার্থবিজ্ঞান + ইংরেজি", listOf("পদার্থবিজ্ঞান", "ইংরেজি")),
            DayPlan("বৃহঃ", "বৃহস্পতিবার", "রসায়ন + জীববিজ্ঞান", listOf("রসায়ন", "জীববিজ্ঞান")),
            DayPlan("শুক্র", "শুক্রবার", "উচ্চতর গণিত + আইসিটি", listOf("উচ্চতর গণিত", "আইসিটি")),
            DayPlan("শনি", "শনিবার", "দুর্বল বিষয় অনুশীলন", listOf("পদার্থবিজ্ঞান", "উচ্চতর গণিত")),
            DayPlan("রবি", "রবিবার", "রিভিশন ও প্র্যাকটিস টেস্ট", listOf("রিভিশন", "নমুনা পরীক্ষা"))
        )
    )
    val weeklyPlan: StateFlow<List<DayPlan>> = _weeklyPlan.asStateFlow()

    private val _stats = MutableStateFlow(
        StudyStats(
            overallProgress = 68,
            weeklyStudyHours = "২৪ ঘণ্টা ৩০ মি.",
            completedTasksCount = 27,
            totalTasksCount = 32,
            streakDays = 5,
            dailyStudyHours = listOf(4.5f, 5.0f, 3.0f, 6.0f, 4.0f, 5.5f, 2.0f)
        )
    )
    val stats: StateFlow<StudyStats> = _stats.asStateFlow()

    private val _dailyGoal = MutableStateFlow(
        DailyGoal(
            targetMinutes = 240, // 4 hours goal
            completedMinutes = 155 // 2h 35m completed
        )
    )
    val dailyGoal: StateFlow<DailyGoal> = _dailyGoal.asStateFlow()

    private val _reminders = MutableStateFlow(
        listOf(
            StudyReminder(
                id = "rem_1",
                title = "পদার্থবিজ্ঞান পড়ার সময় হয়েছে",
                subject = "পদার্থবিজ্ঞান",
                chapterOrTopic = "চল তড়িৎ - গাণিতিক সমস্যা",
                date = "আজ",
                time = "৮:০০ PM",
                isEnabled = true
            ),
            StudyReminder(
                id = "rem_2",
                title = "রসায়ন রিভিশন ও জৈব যৌগ",
                subject = "রসায়ন",
                chapterOrTopic = "জৈব রসায়ন",
                date = "আগামীকাল",
                time = "১০:০০ AM",
                isEnabled = true
            )
        )
    )
    val reminders: StateFlow<List<StudyReminder>> = _reminders.asStateFlow()

    fun completeOnboarding(data: OnboardingData) {
        _onboardingData.value = data.copy(isCompleted = true)
    }

    fun restartOnboarding() {
        _onboardingData.update { it.copy(isCompleted = false) }
    }

    fun setDailyGoalTarget(targetMinutes: Int) {
        _dailyGoal.update { it.copy(targetMinutes = targetMinutes.coerceAtLeast(30)) }
    }

    fun addCompletedStudyMinutes(minutes: Int) {
        _dailyGoal.update { it.copy(completedMinutes = (it.completedMinutes + minutes).coerceAtLeast(0)) }
    }

    fun setCompletedStudyMinutes(minutes: Int) {
        _dailyGoal.update { it.copy(completedMinutes = minutes.coerceAtLeast(0)) }
    }

    fun toggleTaskCompletion(taskId: String): String? {
        var feedback: String? = null
        var minutesChanged = 0
        var wasCompleted = false

        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    val newState = !task.isCompleted
                    wasCompleted = newState
                    minutesChanged = task.estimatedMinutes
                    if (newState) {
                        feedback = "চমৎকার! এভাবে চালিয়ে যান।"
                    }
                    task.copy(isCompleted = newState)
                } else task
            }
        }

        if (wasCompleted) {
            addCompletedStudyMinutes(minutesChanged)
        } else if (minutesChanged > 0) {
            addCompletedStudyMinutes(-minutesChanged)
        }

        // Check if all tasks completed
        val currentTasks = _tasks.value
        if (currentTasks.isNotEmpty() && currentTasks.all { it.isCompleted }) {
            feedback = "সাবাশ! আজকের সব কাজ শেষ হয়েছে 🎉"
        }

        return feedback
    }

    fun addTask(subject: String, topic: String, minutes: Int, priority: TaskPriority) {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            subject = subject,
            topic = topic,
            estimatedMinutes = minutes,
            isCompleted = false,
            priority = priority,
            scheduledDay = "TODAY"
        )
        _tasks.update { it + newTask }
    }

    fun updateTask(taskId: String, subject: String, topic: String, minutes: Int, priority: TaskPriority) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    task.copy(
                        subject = subject,
                        topic = topic,
                        estimatedMinutes = minutes,
                        priority = priority
                    )
                } else task
            }
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.update { list -> list.filterNot { it.id == taskId } }
    }

    // Reminders functions
    fun addReminder(title: String, subject: String, chapterOrTopic: String, date: String, time: String) {
        val newReminder = StudyReminder(
            id = "rem_" + UUID.randomUUID().toString().take(8),
            title = title,
            subject = subject,
            chapterOrTopic = chapterOrTopic,
            date = date,
            time = time,
            isEnabled = true
        )
        _reminders.update { it + newReminder }
    }

    fun editReminder(id: String, title: String, subject: String, chapterOrTopic: String, date: String, time: String, isEnabled: Boolean) {
        _reminders.update { list ->
            list.map { rem ->
                if (rem.id == id) {
                    rem.copy(
                        title = title,
                        subject = subject,
                        chapterOrTopic = chapterOrTopic,
                        date = date,
                        time = time,
                        isEnabled = isEnabled
                    )
                } else rem
            }
        }
    }

    fun deleteReminder(id: String) {
        _reminders.update { list -> list.filterNot { it.id == id } }
    }

    fun toggleReminder(id: String) {
        _reminders.update { list ->
            list.map { rem ->
                if (rem.id == id) rem.copy(isEnabled = !rem.isEnabled) else rem
            }
        }
    }


    fun cycleChapterStatus(subjectId: String, chapterId: String) {
        _subjects.update { subjectList ->
            subjectList.map { subject ->
                if (subject.id == subjectId) {
                    val updatedChapters = subject.chapters.map { chapter ->
                        if (chapter.id == chapterId) {
                            val nextStatus = when (chapter.status) {
                                ChapterStatus.NOT_STARTED -> ChapterStatus.IN_PROGRESS
                                ChapterStatus.IN_PROGRESS -> ChapterStatus.COMPLETED
                                ChapterStatus.COMPLETED -> ChapterStatus.NOT_STARTED
                            }
                            val nextProgress = when (nextStatus) {
                                ChapterStatus.COMPLETED -> 100
                                ChapterStatus.IN_PROGRESS -> 50
                                ChapterStatus.NOT_STARTED -> 0
                            }
                            chapter.copy(status = nextStatus, progressPercent = nextProgress)
                        } else chapter
                    }
                    subject.copy(chapters = updatedChapters)
                } else subject
            }
        }
        recalculateOverallProgress()
    }

    fun addSubject(name: String) {
        val newSubject = Subject(
            id = "sub_" + UUID.randomUUID().toString().take(8),
            name = name,
            chapters = emptyList()
        )
        _subjects.update { it + newSubject }
    }

    fun deleteSubject(subjectId: String) {
        _subjects.update { list -> list.filterNot { it.id == subjectId } }
        recalculateOverallProgress()
    }

    fun addChapter(subjectId: String, title: String, description: String = "", status: ChapterStatus = ChapterStatus.NOT_STARTED, progressPercent: Int = 0) {
        val newChapter = Chapter(
            id = "ch_" + UUID.randomUUID().toString().take(8),
            title = title,
            status = status,
            description = description,
            progressPercent = if (status == ChapterStatus.COMPLETED) 100 else progressPercent
        )
        _subjects.update { subjectList ->
            subjectList.map { subject ->
                if (subject.id == subjectId) {
                    subject.copy(chapters = subject.chapters + newChapter)
                } else subject
            }
        }
        recalculateOverallProgress()
    }

    fun editChapter(subjectId: String, chapterId: String, title: String, description: String, status: ChapterStatus, progressPercent: Int) {
        _subjects.update { subjectList ->
            subjectList.map { subject ->
                if (subject.id == subjectId) {
                    val updatedChapters = subject.chapters.map { chapter ->
                        if (chapter.id == chapterId) {
                            chapter.copy(
                                title = title,
                                description = description,
                                status = status,
                                progressPercent = if (status == ChapterStatus.COMPLETED) 100 else progressPercent
                            )
                        } else chapter
                    }
                    subject.copy(chapters = updatedChapters)
                } else subject
            }
        }
        recalculateOverallProgress()
    }

    fun deleteChapter(subjectId: String, chapterId: String) {
        _subjects.update { subjectList ->
            subjectList.map { subject ->
                if (subject.id == subjectId) {
                    subject.copy(chapters = subject.chapters.filterNot { it.id == chapterId })
                } else subject
            }
        }
        recalculateOverallProgress()
    }

    fun updateCountdown(days: Int, name: String) {
        _examCountdownDays.value = days
        _examName.value = name
    }

    private fun recalculateOverallProgress() {
        val allSubjects = _subjects.value
        val totalChapters = allSubjects.sumOf { it.chapters.size }
        val completedChapters = allSubjects.sumOf { it.completedChaptersCount }
        if (totalChapters > 0) {
            val percentage = (completedChapters * 100) / totalChapters
            _stats.update { it.copy(overallProgress = percentage) }
        }
    }
}
