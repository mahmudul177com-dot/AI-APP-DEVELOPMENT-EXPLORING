package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Chapter
import com.example.data.models.ChapterStatus
import com.example.data.models.Subject
import com.example.ui.components.AddChapterDialog
import com.example.ui.components.AddSubjectDialog
import com.example.ui.components.ChapterItem
import com.example.ui.components.SubjectCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SubjectsScreen(
    subjects: List<Subject>,
    selectedSubjectId: String?,
    onSelectSubject: (String?) -> Unit,
    onCycleChapterStatus: (subjectId: String, chapterId: String) -> Unit,
    onAddSubject: (name: String) -> Unit = {},
    onDeleteSubject: (subjectId: String) -> Unit = {},
    onAddChapter: (subjectId: String, title: String, description: String, status: ChapterStatus, progressPercent: Int) -> Unit = { _, _, _, _, _ -> },
    onEditChapter: (subjectId: String, chapterId: String, title: String, description: String, status: ChapterStatus, progressPercent: Int) -> Unit = { _, _, _, _, _, _ -> },
    onDeleteChapter: (subjectId: String, chapterId: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var showAddChapterDialog by remember { mutableStateOf(false) }
    var editingChapter by remember { mutableStateOf<Chapter?>(null) }

    val selectedSubject = subjects.find { it.id == selectedSubjectId }

    if (showAddSubjectDialog) {
        AddSubjectDialog(
            onDismiss = { showAddSubjectDialog = false },
            onConfirm = { subjectName ->
                onAddSubject(subjectName)
                showAddSubjectDialog = false
            }
        )
    }

    if (showAddChapterDialog && selectedSubject != null) {
        AddChapterDialog(
            isEditMode = false,
            onDismiss = { showAddChapterDialog = false },
            onConfirm = { title, description, status, progressPercent ->
                onAddChapter(selectedSubject.id, title, description, status, progressPercent)
                showAddChapterDialog = false
            }
        )
    }

    if (editingChapter != null && selectedSubject != null) {
        val chapterToEdit = editingChapter!!
        AddChapterDialog(
            initialTitle = chapterToEdit.title,
            initialDescription = chapterToEdit.description,
            initialStatus = chapterToEdit.status,
            initialProgress = chapterToEdit.progressPercent,
            isEditMode = true,
            onDismiss = { editingChapter = null },
            onConfirm = { title, description, status, progressPercent ->
                onEditChapter(selectedSubject.id, chapterToEdit.id, title, description, status, progressPercent)
                editingChapter = null
            }
        )
    }

    if (selectedSubject != null) {
        // Dedicated Subject Chapter View
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = { onSelectSubject(null) },
                            modifier = Modifier.testTag("back_to_subjects_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "বিষয়ের তালিকায় ফিরে যান",
                                tint = TextPrimary
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedSubject.name,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                ),
                                color = TextPrimary
                            )
                            Text(
                                text = "${selectedSubject.completedChaptersCount} / ${selectedSubject.chapters.size} টি অধ্যায় সম্পন্ন (${selectedSubject.completionPercentage}%)",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        }
                    }

                    // Add Chapter Button ("+ অধ্যায় যোগ করুন")
                    Button(
                        onClick = { showAddChapterDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("add_chapter_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "অধ্যায় যোগ করুন",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            if (selectedSubject.chapters.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "এই বিষয়ে এখনো কোনো অধ্যায় নেই",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "উপরের '+ অধ্যায় যোগ করুন' বাটনে চাপ দিয়ে প্রথম অধ্যায় যুক্ত করুন।",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(selectedSubject.chapters, key = { it.id }) { chapter ->
                    ChapterItem(
                        chapter = chapter,
                        onCycleStatus = { onCycleChapterStatus(selectedSubject.id, chapter.id) },
                        onEditClick = { editingChapter = chapter },
                        onDeleteClick = { onDeleteChapter(selectedSubject.id, chapter.id) }
                    )
                }
            }
        }
    } else {
        // Subjects Overview & Custom Subject List
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "বিষয় ও অধ্যায়সমূহ",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = TextPrimary
                        )
                        Text(
                            text = "বিষয়ভিত্তিক সিলেবাস ও অধ্যায়ের বিবরণী",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = { showAddSubjectDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("add_subject_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "বিষয় যোগ",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            items(subjects, key = { it.id }) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = { onSelectSubject(subject.id) },
                    onDeleteClick = { onDeleteSubject(subject.id) }
                )
            }
        }
    }
}
