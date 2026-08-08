package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DailyGoal
import com.example.data.models.StudyReminder
import com.example.data.models.Task
import com.example.data.models.TaskPriority
import com.example.ui.components.AddReminderDialog
import com.example.ui.components.AddTaskDialog
import com.example.ui.components.EditCountdownDialog
import com.example.ui.components.EditGoalDialog
import com.example.ui.components.EmptyState
import com.example.ui.components.RemindersListDialog
import com.example.ui.components.TaskCard
import com.example.ui.theme.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tasks: List<Task>,
    examCountdownDays: Int,
    examName: String,
    streakDays: Int,
    availableSubjects: List<String>,
    dailyGoal: DailyGoal,
    reminders: List<StudyReminder>,
    onToggleTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
    onAddTask: (subject: String, topic: String, minutes: Int, priority: TaskPriority) -> Unit,
    onUpdateTask: (taskId: String, subject: String, topic: String, minutes: Int, priority: TaskPriority) -> Unit,
    onUpdateCountdown: (days: Int, name: String) -> Unit,
    onUpdateDailyGoalTarget: (targetMinutes: Int) -> Unit,
    onUpdateCompletedStudyTime: (completedMinutes: Int) -> Unit,
    onAddReminder: (title: String, subject: String, chapterOrTopic: String, date: String, time: String) -> Unit,
    onEditReminder: (id: String, title: String, subject: String, chapterOrTopic: String, date: String, time: String, isEnabled: Boolean) -> Unit,
    onDeleteReminder: (id: String) -> Unit,
    onToggleReminder: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    var showEditCountdownDialog by remember { mutableStateOf(false) }
    var showEditGoalDialog by remember { mutableStateOf(false) }

    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showRemindersListDialog by remember { mutableStateOf(false) }
    var editingReminder by remember { mutableStateOf<StudyReminder?>(null) }

    var selectedPriorityFilter by remember { mutableStateOf<TaskPriority?>(null) }

    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val progressFraction = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()

    val filteredTasks = if (selectedPriorityFilter == null) {
        tasks
    } else {
        tasks.filter { it.priority == selectedPriorityFilter }
    }

    val greetingText = getGreetingByTime()

    // Find next active reminder
    val nextActiveReminder = reminders.firstOrNull { it.isEnabled } ?: reminders.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Top Greeting
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "$greetingText 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "আজকের পড়াশোনার পরিকল্পনা ও লক্ষ্যগুলো দেখে নিন।",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }

        // Exam Countdown Card
        item {
            Surface(
                color = PrimaryBlue,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEditCountdownDialog = true }
                    .testTag("exam_countdown_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "$examCountdownDays দিন বাকি",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "$examName",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "কাউন্টডাউন এডিট করুন",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Feature 2: Daily Study Goal Card ("আজকের পড়াশোনার লক্ষ্য")
        item {
            Surface(
                color = SurfaceLight,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_study_goal_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "আজকের পড়াশোনার লক্ষ্য",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        IconButton(
                            onClick = { showEditGoalDialog = true },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("edit_daily_goal_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "লক্ষ্য পরিবর্তন করুন",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "${formatMinutesToBangla(dailyGoal.completedMinutes)} / ${formatMinutesToBangla(dailyGoal.targetMinutes)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        Text(
                            text = if (dailyGoal.isGoalReached) "লক্ষ্য পূর্ণ 🎉" else "বাকি: ${formatMinutesToBangla(dailyGoal.remainingMinutes)}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (dailyGoal.isGoalReached) SuccessGreen else TextSecondary
                        )
                    }

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { dailyGoal.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = if (dailyGoal.isGoalReached) SuccessGreen else PrimaryBlue,
                        trackColor = PrimaryBlueLight
                    )

                    // Encourage message
                    if (dailyGoal.isGoalReached) {
                        Surface(
                            color = SuccessGreenLight,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "আজকের লক্ষ্য পূরণ হয়েছে 🎉 দুর্দান্ত পারফরম্যান্স!",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = SuccessGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // Feature 2.5: Odommo Tip Card ("অদম্য পরামর্শ")
        item {
            Surface(
                color = SurfaceLight,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("odommo_tip_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = PrimaryBlueLight,
                            shape = CircleShape
                        ) {
                            Box(modifier = Modifier.padding(6.dp)) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = "অদম্য পরামর্শ",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryBlue
                        )
                    }

                    Text(
                        text = "“একদিন বাদ গেলেই যাত্রা শেষ নয়। আবার শুরু করুন।”",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )

                    Text(
                        text = "Build habits. Stay consistent. Move forward.",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Feature 3: Study Reminder Card ("পরবর্তী রিমাইন্ডার")
        item {
            Surface(
                color = SurfaceLight,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("study_reminder_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "পরবর্তী রিমাইন্ডার",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        TextButton(
                            onClick = { showRemindersListDialog = true },
                            modifier = Modifier.testTag("view_all_reminders_button")
                        ) {
                            Text("সব রিমাইন্ডার (${reminders.size})", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    if (nextActiveReminder != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${nextActiveReminder.date}, ${nextActiveReminder.time}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "${nextActiveReminder.title} • ${nextActiveReminder.subject}" +
                                            if (nextActiveReminder.chapterOrTopic.isNotBlank()) " (${nextActiveReminder.chapterOrTopic})" else "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }

                            Switch(
                                checked = nextActiveReminder.isEnabled,
                                onCheckedChange = { onToggleReminder(nextActiveReminder.id) },
                                modifier = Modifier.testTag("toggle_next_reminder_switch")
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "কোনো রিমাইন্ডার সক্রিয় নেই",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                            OutlinedButton(
                                onClick = { showAddReminderDialog = true },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("+ যোগ করুন")
                            }
                        }
                    }
                }
            }
        }

        // Today's Progress Summary & Streak Card
        item {
            Surface(
                color = SurfaceLight,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_progress_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "কাজের সময়সূচী ও স্ট্রিক",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "$completedCount / $totalCount টি কাজ সম্পন্ন হয়েছে",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        // Streak Pill
                        Surface(
                            color = AccentAmberLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = AccentAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "$streakDays দিন টানা",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = AccentAmber
                                )
                            }
                        }
                    }

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = SuccessGreen,
                        trackColor = PrimaryBlueLight
                    )
                }
            }
        }

        // Today's Tasks Heading + Priority Filter Chips + Add Task Button
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "আজকের কাজ",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = TextPrimary
                    )

                    Button(
                        onClick = { showAddTaskDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("add_task_main_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "কাজ যোগ করুন",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "কাজ যোগ করুন", style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Priority Filter Chips (🔴 জরুরি, 🟡 গুরুত্বপূর্ণ, 🟢 সাধারণ)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedPriorityFilter == null,
                        onClick = { selectedPriorityFilter = null },
                        label = { Text("সব (${tasks.size})") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedPriorityFilter == TaskPriority.HIGH,
                        onClick = {
                            selectedPriorityFilter = if (selectedPriorityFilter == TaskPriority.HIGH) null else TaskPriority.HIGH
                        },
                        label = { Text("🔴 জরুরি") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedPriorityFilter == TaskPriority.MEDIUM,
                        onClick = {
                            selectedPriorityFilter = if (selectedPriorityFilter == TaskPriority.MEDIUM) null else TaskPriority.MEDIUM
                        },
                        label = { Text("🟡 গুরুত্বপূর্ণ") },
                        modifier = Modifier.weight(1.2f)
                    )
                    FilterChip(
                        selected = selectedPriorityFilter == TaskPriority.LOW,
                        onClick = {
                            selectedPriorityFilter = if (selectedPriorityFilter == TaskPriority.LOW) null else TaskPriority.LOW
                        },
                        label = { Text("🟢 সাধারণ") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Tasks List or Empty State
        if (filteredTasks.isEmpty()) {
            item {
                EmptyState(
                    title = if (selectedPriorityFilter != null) "এই ধরনের কোনো কাজ নেই।" else "আজকের কোনো কাজ বাকি নেই।",
                    message = if (selectedPriorityFilter != null) "অন্যান্য ফিল্টার দেখুন অথবা নতুন কাজ যোগ করুন।" else "কিছুক্ষণ বিশ্রাম নিন অথবা নতুন পড়াশোনার কাজ যোগ করুন।",
                    buttonText = "কাজ যোগ করুন",
                    onButtonClick = { showAddTaskDialog = true }
                )
            }
        } else {
            items(filteredTasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onToggle = { onToggleTask(task.id) },
                    onDelete = { onDeleteTask(task.id) },
                    onEdit = { editingTask = task }
                )
            }
        }
    }

    // Dialogs
    if (showAddTaskDialog) {
        AddTaskDialog(
            availableSubjects = availableSubjects,
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { subject, topic, minutes, priority ->
                onAddTask(subject, topic, minutes, priority)
                showAddTaskDialog = false
            }
        )
    }

    if (editingTask != null) {
        val taskToEdit = editingTask!!
        AddTaskDialog(
            availableSubjects = availableSubjects,
            initialSubject = taskToEdit.subject,
            initialTopic = taskToEdit.topic,
            initialMinutes = taskToEdit.estimatedMinutes,
            initialPriority = taskToEdit.priority,
            isEditMode = true,
            onDismiss = { editingTask = null },
            onConfirm = { subject, topic, minutes, priority ->
                onUpdateTask(taskToEdit.id, subject, topic, minutes, priority)
                editingTask = null
            }
        )
    }

    if (showEditCountdownDialog) {
        EditCountdownDialog(
            currentDays = examCountdownDays,
            currentExamName = examName,
            onDismiss = { showEditCountdownDialog = false },
            onConfirm = { days, name ->
                onUpdateCountdown(days, name)
                showEditCountdownDialog = false
            }
        )
    }

    if (showEditGoalDialog) {
        EditGoalDialog(
            currentGoal = dailyGoal,
            onDismiss = { showEditGoalDialog = false },
            onConfirmTarget = { targetMinutes ->
                onUpdateDailyGoalTarget(targetMinutes)
            },
            onConfirmCompleted = { completedMinutes ->
                onUpdateCompletedStudyTime(completedMinutes)
            }
        )
    }

    if (showAddReminderDialog) {
        AddReminderDialog(
            availableSubjects = availableSubjects,
            initialReminder = editingReminder,
            onDismiss = {
                showAddReminderDialog = false
                editingReminder = null
            },
            onConfirm = { title, subject, chapterOrTopic, date, time ->
                if (editingReminder != null) {
                    onEditReminder(
                        editingReminder!!.id,
                        title,
                        subject,
                        chapterOrTopic,
                        date,
                        time,
                        editingReminder!!.isEnabled
                    )
                } else {
                    onAddReminder(title, subject, chapterOrTopic, date, time)
                }
                showAddReminderDialog = false
                editingReminder = null
            }
        )
    }

    if (showRemindersListDialog) {
        RemindersListDialog(
            reminders = reminders,
            onDismiss = { showRemindersListDialog = false },
            onAddClick = {
                showRemindersListDialog = false
                editingReminder = null
                showAddReminderDialog = true
            },
            onEditClick = { reminder ->
                showRemindersListDialog = false
                editingReminder = reminder
                showAddReminderDialog = true
            },
            onDeleteClick = onDeleteReminder,
            onToggleClick = onToggleReminder
        )
    }
}

private fun formatMinutesToBangla(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    val toBanglaDigits = { num: Int ->
        val banglaDigits = arrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        num.toString().map { if (it in '0'..'9') banglaDigits[it - '0'] else it }.joinToString("")
    }
    return when {
        hours > 0 && minutes > 0 -> "${toBanglaDigits(hours)} ঘণ্টা ${toBanglaDigits(minutes)} মিনিট"
        hours > 0 -> "${toBanglaDigits(hours)} ঘণ্টা"
        else -> "${toBanglaDigits(minutes)} মিনিট"
    }
}

private fun getGreetingByTime(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour in 5..11 -> "শুভ সকাল"
        hour in 12..16 -> "শুভ দুপুর"
        else -> "শুভ সন্ধ্যা"
    }
}
