package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.models.ChapterStatus
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary

@Composable
fun AddChapterDialog(
    initialTitle: String = "",
    initialDescription: String = "",
    initialStatus: ChapterStatus = ChapterStatus.NOT_STARTED,
    initialProgress: Int = 0,
    isEditMode: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, status: ChapterStatus, progressPercent: Int) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }
    var status by remember { mutableStateOf(initialStatus) }
    var progressPercent by remember { mutableFloatStateOf(initialProgress.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "অধ্যায় সংশোধন করুন" else "নতুন অধ্যায় যোগ করুন",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("অধ্যায়ের নাম") },
                    placeholder = { Text("যেমন: গতিবিদ্যা") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chapter_title_input")
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("বিবরণ / নোট (ঐচ্ছিক)") },
                    placeholder = { Text("যেমন: গুরুত্বপূর্ণ সূত্র ও সমস্যা") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chapter_description_input")
                )

                Text(
                    text = "অবস্থা (Status)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val statusOptions = listOf(
                        ChapterStatus.NOT_STARTED to "শুরু হয়নি",
                        ChapterStatus.IN_PROGRESS to "চলমান",
                        ChapterStatus.COMPLETED to "সম্পন্ন"
                    )

                    statusOptions.forEach { (optionStatus, label) ->
                        FilterChip(
                            selected = status == optionStatus,
                            onClick = {
                                status = optionStatus
                                if (optionStatus == ChapterStatus.COMPLETED) {
                                    progressPercent = 100f
                                } else if (optionStatus == ChapterStatus.NOT_STARTED && progressPercent == 100f) {
                                    progressPercent = 0f
                                }
                            },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (status != ChapterStatus.COMPLETED) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "অগ্রগতি (Progress)",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = TextPrimary
                            )
                            Text(
                                text = "${progressPercent.toInt()}%",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryBlue
                            )
                        }

                        Slider(
                            value = progressPercent,
                            onValueChange = {
                                progressPercent = it
                                if (it.toInt() == 100) {
                                    status = ChapterStatus.COMPLETED
                                } else if (it.toInt() > 0 && status == ChapterStatus.NOT_STARTED) {
                                    status = ChapterStatus.IN_PROGRESS
                                }
                            },
                            valueRange = 0f..100f,
                            steps = 19,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("chapter_progress_slider")
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(
                            title.trim(),
                            description.trim(),
                            status,
                            progressPercent.toInt()
                        )
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.testTag("confirm_save_chapter_button")
            ) {
                Text(if (isEditMode) "আপডেট করুন" else "সংরক্ষণ করুন")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_save_chapter_button")
            ) {
                Text("বাতিল")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
