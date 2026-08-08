package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.StudyStats
import com.example.ui.components.ProgressChart
import com.example.ui.theme.*

@Composable
fun ProgressScreen(
    stats: StudyStats,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Page Title
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "অগ্রগতি ও পরিসংখ্যান",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "আপনার পড়াশোনার ধারাবাহিকতা একনজরে",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }

        // Stats Grid (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "সর্বমোট অগ্রগতি",
                        value = "${stats.overallProgress}%",
                        icon = Icons.Default.TrendingUp,
                        iconTint = PrimaryBlue,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_card_overall"
                    )
                    StatCard(
                        title = "সাপ্তাহিক পড়ার সময়",
                        value = stats.weeklyStudyHours,
                        icon = Icons.Default.Schedule,
                        iconTint = SecondaryTeal,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_card_weekly_time"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "সম্পন্ন কাজ",
                        value = "${stats.completedTasksCount} / ${stats.totalTasksCount}",
                        icon = Icons.Default.CheckCircle,
                        iconTint = SuccessGreen,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_card_completed_tasks"
                    )
                    StatCard(
                        title = "ধারাবাহিকতা (Streak)",
                        value = "${stats.streakDays} দিন",
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = AccentAmber,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_card_streak"
                    )
                }
            }
        }

        // Weekly Study Chart
        item {
            ProgressChart(dailyHours = stats.dailyStudyHours)
        }

        // Motivation & Consistency Card
        item {
            Surface(
                color = PrimaryBlueLight,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("motivation_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = AccentAmber,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "ধারাবাহিকতা বজায় রাখুন",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryBlue
                        )
                    }

                    Text(
                        text = "আপনি টানা ${stats.streakDays} দিন ধরে নিয়মিত পড়াশোনা করছেন! আপনার লক্ষ্য অর্জনে এভাবে এগিয়ে যান।",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )

                    Text(
                        text = "পরামর্শ: পরীক্ষার আগে একরাতে অতিরিক্ত পড়ার চেয়ে প্রতিদিন ৩-৪ ঘণ্টা মনোযোগ দিয়ে পড়া পড়া মনে রাখতে সাহায্য করে।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        color = SurfaceLight,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        shadowElevation = 1.dp,
        modifier = modifier.testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = iconTint.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    modifier = Modifier.padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = TextPrimary
            )
        }
    }
}
