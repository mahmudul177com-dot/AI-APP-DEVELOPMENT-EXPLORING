package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.models.StudyReminder
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    availableSubjects: List<String>,
    initialReminder: StudyReminder? = null,
    onDismiss: () -> Unit,
    onConfirm: (title: String, subject: String, chapterOrTopic: String, date: String, time: String) -> Unit
) {
    val subjects = if (availableSubjects.isEmpty()) listOf("পদার্থবিজ্ঞান", "রসায়ন", "জীববিজ্ঞান", "উচ্চতর গণিত", "ইংরেজি", "আইসিটি") else availableSubjects

    var title by remember { mutableStateOf(initialReminder?.title ?: "পড়ার সময় হয়েছে") }
    var selectedSubject by remember { mutableStateOf(initialReminder?.subject ?: subjects.first()) }
    var chapterOrTopic by remember { mutableStateOf(initialReminder?.chapterOrTopic ?: "") }
    var dateText by remember { mutableStateOf(initialReminder?.date ?: "আজ") }
    var timeText by remember { mutableStateOf(initialReminder?.time ?: "৮:০০ PM") }

    var expandedSubjectDropdown by remember { mutableStateOf(false) }

    val isEdit = initialReminder != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEdit) "রিমাইন্ডার এডিট করুন" else "নতুন স্টাডি রিমাইন্ডার",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("রিমাইন্ডার শিরোনাম") },
                    placeholder = { Text("যেমন: পদার্থবিজ্ঞান পড়ার সময় হয়েছে") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reminder_title_input")
                )

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
                            .testTag("reminder_subject_field")
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSubjectDropdown,
                        onDismissRequest = { expandedSubjectDropdown = false }
                    ) {
                        subjects.forEach { sub ->
                            DropdownMenuItem(
                                text = { Text(sub) },
                                onClick = {
                                    selectedSubject = sub
                                    expandedSubjectDropdown = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = chapterOrTopic,
                    onValueChange = { chapterOrTopic = it },
                    label = { Text("অধ্যায় বা টপিক (ঐচ্ছিক)") },
                    placeholder = { Text("যেমন: ভেক্টর") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reminder_topic_input")
                )

                Text(
                    text = "তারিখ (Date)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("আজ", "আগামীকাল", "পরশু").forEach { dateOption ->
                        FilterChip(
                            selected = dateText == dateOption,
                            onClick = { dateText = dateOption },
                            label = { Text(dateOption) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("নির্দিষ্ট তারিখ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "সময় (Time)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("৭:০০ AM", "৪:০০ PM", "৮:০০ PM", "১০:০০ PM").forEach { timeOption ->
                        FilterChip(
                            selected = timeText == timeOption,
                            onClick = { timeText = timeOption },
                            label = { Text(timeOption) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text("নির্দিষ্ট সময়") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reminder_time_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), selectedSubject, chapterOrTopic.trim(), dateText.trim(), timeText.trim())
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.testTag("confirm_save_reminder_button")
            ) {
                Text(if (isEdit) "আপডেট করুন" else "রিমাইন্ডার সেট করুন")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_save_reminder_button")
            ) {
                Text("বাতিল")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
