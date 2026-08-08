package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.*
import com.example.data.repository.StudyFlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudyViewModel(
    private val repository: StudyFlowRepository = StudyFlowRepository()
) : ViewModel() {

    val onboardingData: StateFlow<OnboardingData> = repository.onboardingData
    val examCountdownDays: StateFlow<Int> = repository.examCountdownDays
    val examName: StateFlow<String> = repository.examName
    val tasks: StateFlow<List<Task>> = repository.tasks
    val subjects: StateFlow<List<Subject>> = repository.subjects
    val weeklyPlan: StateFlow<List<DayPlan>> = repository.weeklyPlan
    val stats: StateFlow<StudyStats> = repository.stats
    val dailyGoal: StateFlow<DailyGoal> = repository.dailyGoal
    val reminders: StateFlow<List<StudyReminder>> = repository.reminders


    private val _selectedSubjectId = MutableStateFlow<String?>(null)
    val selectedSubjectId: StateFlow<String?> = _selectedSubjectId.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun completeOnboarding(examTarget: String, selectedSubjects: List<String>, dailyHours: String, goal: String) {
        repository.completeOnboarding(
            OnboardingData(
                examTarget = examTarget,
                subjects = selectedSubjects,
                dailyHours = dailyHours,
                goal = goal,
                isCompleted = true
            )
        )
    }

    fun restartOnboarding() {
        repository.restartOnboarding()
    }

    fun toggleTask(taskId: String) {
        val feedback = repository.toggleTaskCompletion(taskId)
        if (feedback != null) {
            _snackbarMessage.value = feedback
        }
    }

    fun addTask(subject: String, topic: String, minutes: Int, priority: TaskPriority) {
        repository.addTask(subject, topic, minutes, priority)
        _snackbarMessage.value = "নতুন কাজ যুক্ত হয়েছে ✓"
    }

    fun updateTask(taskId: String, subject: String, topic: String, minutes: Int, priority: TaskPriority) {
        repository.updateTask(taskId, subject, topic, minutes, priority)
        _snackbarMessage.value = "কাজের তথ্য আপডেট করা হয়েছে ✓"
    }

    fun deleteTask(taskId: String) {
        repository.deleteTask(taskId)
        _snackbarMessage.value = "কাজটি মুছে ফেলা হয়েছে"
    }

    fun setDailyGoalTarget(targetMinutes: Int) {
        repository.setDailyGoalTarget(targetMinutes)
        _snackbarMessage.value = "পড়াশোনার লক্ষ্য আপডেট করা হয়েছে ✓"
    }

    fun setCompletedStudyMinutes(minutes: Int) {
        repository.setCompletedStudyMinutes(minutes)
        _snackbarMessage.value = "পড়াশোনার সময় আপডেট হয়েছে ✓"
    }

    fun addReminder(title: String, subject: String, chapterOrTopic: String, date: String, time: String) {
        repository.addReminder(title, subject, chapterOrTopic, date, time)
        _snackbarMessage.value = "স্টাডি রিমাইন্ডার যোগ করা হয়েছে ✓"
    }

    fun editReminder(id: String, title: String, subject: String, chapterOrTopic: String, date: String, time: String, isEnabled: Boolean) {
        repository.editReminder(id, title, subject, chapterOrTopic, date, time, isEnabled)
        _snackbarMessage.value = "রিমাইন্ডার আপডেট করা হয়েছে ✓"
    }

    fun deleteReminder(id: String) {
        repository.deleteReminder(id)
        _snackbarMessage.value = "রিমাইন্ডার মুছে ফেলা হয়েছে"
    }

    fun toggleReminder(id: String) {
        repository.toggleReminder(id)
    }


    fun selectSubject(subjectId: String?) {
        _selectedSubjectId.value = subjectId
    }

    fun cycleChapterStatus(subjectId: String, chapterId: String) {
        repository.cycleChapterStatus(subjectId, chapterId)
    }

    fun addSubject(name: String) {
        repository.addSubject(name)
        _snackbarMessage.value = "নতুন বিষয় যুক্ত হয়েছে ✓"
    }

    fun deleteSubject(subjectId: String) {
        repository.deleteSubject(subjectId)
        if (_selectedSubjectId.value == subjectId) {
            _selectedSubjectId.value = null
        }
        _snackbarMessage.value = "বিষয় মুছে ফেলা হয়েছে"
    }

    fun addChapter(subjectId: String, title: String, description: String, status: ChapterStatus, progressPercent: Int) {
        repository.addChapter(subjectId, title, description, status, progressPercent)
        _snackbarMessage.value = "নতুন অধ্যায় যুক্ত হয়েছে ✓"
    }

    fun editChapter(subjectId: String, chapterId: String, title: String, description: String, status: ChapterStatus, progressPercent: Int) {
        repository.editChapter(subjectId, chapterId, title, description, status, progressPercent)
        _snackbarMessage.value = "অধ্যায় তথ্য আপডেট করা হয়েছে ✓"
    }

    fun deleteChapter(subjectId: String, chapterId: String) {
        repository.deleteChapter(subjectId, chapterId)
        _snackbarMessage.value = "অধ্যায়টি মুছে ফেলা হয়েছে"
    }

    fun updateExamCountdown(days: Int, name: String) {
        repository.updateCountdown(days, name)
        _snackbarMessage.value = "কাউন্টডাউন আপডেট হয়েছে ✓"
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
