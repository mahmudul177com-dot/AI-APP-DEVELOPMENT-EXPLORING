package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.models.TaskPriority
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    availableSubjects: List<String>,
    initialSubject: String? = null,
    initialTopic: String = "",
    initialMinutes: Int = 60,
    initialPriority: TaskPriority = TaskPriority.MEDIUM,
    isEditMode: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (subject: String, topic: String, minutes: Int, priority: TaskPriority) -> Unit
) {
    val subjects = if (availableSubjects.isEmpty()) listOf("পদার্থবিজ্ঞান", "রসায়ন", "জীববিজ্ঞান", "উচ্চতর গণিত", "ইংরেজি", "আইসিটি") else availableSubjects
    var selectedSubject by remember { mutableStateOf(initialSubject ?: subjects.first()) }
    var topic by remember { mutableStateOf(initialTopic) }
    var minutesText by remember { mutableStateOf(initialMinutes.toString()) }
    var selectedPriority by remember { mutableStateOf(initialPriority) }
    var expandedSubjectDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "কাজের তথ্য সংশোধন করুন" else "আজকের কাজ যোগ করুন",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Subject Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedSubjectDropdown,
                    onExpandedChange = { expandedSubjectDropdown = !expandedSubjectDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("বিষয়") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubjectDropdown) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("add_task_subject_field")
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSubjectDropdown,
                        onDismissRequest = { expandedSubjectDropdown = false }
                    ) {
                        subjects.forEach { subject ->
                            DropdownMenuItem(
                                text = { Text(subject) },
                                onClick = {
                                    selectedSubject = subject
                                    expandedSubjectDropdown = false
                                }
                            )
                        }
                    }
                }

                // Topic Name
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("টপিকের নাম") },
                    placeholder = { Text("যেমন: চল তড়িৎ") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_task_topic_field")
                )

                // Estimated Time (minutes)
                OutlinedTextField(
                    value = minutesText,
                    onValueChange = { minutesText = it.filter { char -> char.isDigit() } },
                    label = { Text("আনুমানিক সময় (মিনিট)") },
                    placeholder = { Text("৬০") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_task_minutes_field")
                )

                // Priority Selection
                Text(
                    text = "গুরুত্ব (Priority)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskPriority.entries.forEach { priority ->
                        val priorityLabel = when (priority) {
                            TaskPriority.HIGH -> "🔴 জরুরি"
                            TaskPriority.MEDIUM -> "🟡 গুরুত্বপূর্ণ"
                            TaskPriority.LOW -> "🟢 সাধারণ"
                        }
                        FilterChip(
                            selected = selectedPriority == priority,
                            onClick = { selectedPriority = priority },
                            label = {
                                Text(
                                    text = priorityLabel,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (topic.isNotBlank()) {
                        val minutes = minutesText.toIntOrNull() ?: 30
                        onConfirm(selectedSubject, topic.trim(), minutes, selectedPriority)
                    }
                },
                enabled = topic.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.testTag("confirm_add_task_button")
            ) {
                Text(if (isEditMode) "আপডেট করুন" else "যোগ করুন")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_add_task_button")
            ) {
                Text("বাতিল")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

