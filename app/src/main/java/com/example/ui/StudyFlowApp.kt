package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.HeaderBar
import com.example.ui.components.NavigationTab
import com.example.ui.components.Sidebar
import com.example.ui.screens.*
import com.example.ui.theme.BackgroundLight

@Composable
fun StudyFlowApp(
    viewModel: StudyViewModel = viewModel()
) {
    val onboardingData by viewModel.onboardingData.collectAsStateWithLifecycle()
    val examCountdownDays by viewModel.examCountdownDays.collectAsStateWithLifecycle()
    val examName by viewModel.examName.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val weeklyPlan by viewModel.weeklyPlan.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val selectedSubjectId by viewModel.selectedSubjectId.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val dailyGoal by viewModel.dailyGoal.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(NavigationTab.HOME) }
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    if (!onboardingData.isCompleted) {
        OnboardingScreen(
            onFinishOnboarding = { examTarget, selectedSubjects, dailyHours, goal ->
                viewModel.completeOnboarding(examTarget, selectedSubjects, dailyHours, goal)
            }
        )
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isDesktopOrTablet = maxWidth >= 600.dp

            Scaffold(
                containerColor = BackgroundLight,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    HeaderBar(
                        examTarget = onboardingData.examTarget,
                        onRestartOnboarding = { viewModel.restartOnboarding() }
                    )
                },
                bottomBar = {
                    if (!isDesktopOrTablet) {
                        BottomNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (isDesktopOrTablet) {
                        Sidebar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            examTarget = onboardingData.examTarget
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .testTag("main_screen_container")
                    ) {
                        Crossfade(
                            targetState = selectedTab,
                            label = "tab_crossfade"
                        ) { tab ->
                            when (tab) {
                                NavigationTab.HOME -> HomeScreen(
                                    tasks = tasks,
                                    examCountdownDays = examCountdownDays,
                                    examName = examName,
                                    streakDays = stats.streakDays,
                                    availableSubjects = onboardingData.subjects,
                                    dailyGoal = dailyGoal,
                                    reminders = reminders,
                                    onToggleTask = { viewModel.toggleTask(it) },
                                    onDeleteTask = { viewModel.deleteTask(it) },
                                    onAddTask = { subject, topic, minutes, priority ->
                                        viewModel.addTask(subject, topic, minutes, priority)
                                    },
                                    onUpdateTask = { id, subject, topic, minutes, priority ->
                                        viewModel.updateTask(id, subject, topic, minutes, priority)
                                    },
                                    onUpdateCountdown = { days, name ->
                                        viewModel.updateExamCountdown(days, name)
                                    },
                                    onUpdateDailyGoalTarget = { viewModel.setDailyGoalTarget(it) },
                                    onUpdateCompletedStudyTime = { viewModel.setCompletedStudyMinutes(it) },
                                    onAddReminder = { title, subject, topic, date, time ->
                                        viewModel.addReminder(title, subject, topic, date, time)
                                    },
                                    onEditReminder = { id, title, subject, topic, date, time, enabled ->
                                        viewModel.editReminder(id, title, subject, topic, date, time, enabled)
                                    },
                                    onDeleteReminder = { viewModel.deleteReminder(it) },
                                    onToggleReminder = { viewModel.toggleReminder(it) }
                                )


                                NavigationTab.PLAN -> PlanScreen(
                                    weeklyPlan = weeklyPlan
                                )

                                NavigationTab.SUBJECTS -> SubjectsScreen(
                                    subjects = subjects,
                                    selectedSubjectId = selectedSubjectId,
                                    onSelectSubject = { viewModel.selectSubject(it) },
                                    onCycleChapterStatus = { subjectId, chapterId ->
                                        viewModel.cycleChapterStatus(subjectId, chapterId)
                                    },
                                    onAddSubject = { viewModel.addSubject(it) },
                                    onDeleteSubject = { viewModel.deleteSubject(it) },
                                    onAddChapter = { subjectId, title, description, status, progressPercent ->
                                        viewModel.addChapter(subjectId, title, description, status, progressPercent)
                                    },
                                    onEditChapter = { subjectId, chapterId, title, description, status, progressPercent ->
                                        viewModel.editChapter(subjectId, chapterId, title, description, status, progressPercent)
                                    },
                                    onDeleteChapter = { subjectId, chapterId ->
                                        viewModel.deleteChapter(subjectId, chapterId)
                                    }
                                )

                                NavigationTab.PROGRESS -> ProgressScreen(
                                    stats = stats
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
