package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DayPlan
import com.example.ui.components.DayPlanCard
import com.example.ui.components.getSubjectColor
import com.example.ui.theme.*

@Composable
fun PlanScreen(
    weeklyPlan: List<DayPlan>,
    modifier: Modifier = Modifier
) {
    var selectedDayCode by remember { mutableStateOf("MON") }

    val selectedPlan = weeklyPlan.find { it.dayCode == selectedDayCode } ?: weeklyPlan.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Page Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "সাপ্তাহিক রুটিন",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "পরীক্ষার প্রস্তুতির জন্য সুনির্দিষ্ট পড়ার রুটিন",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }

        // Days List
        items(weeklyPlan, key = { it.dayCode }) { dayPlan ->
            DayPlanCard(
                dayPlan = dayPlan,
                isSelected = dayPlan.dayCode == selectedDayCode,
                onClick = { selectedDayCode = dayPlan.dayCode }
            )
        }

        // Selected Day Details Card
        if (selectedPlan != null) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = SurfaceLight,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("selected_day_details_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                            Text(
                                text = "${selectedPlan.dayName}-এর মূল ফোকাস",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        Text(
                            text = "মূল বিষয়: ${selectedPlan.focusArea}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.SemiBold
                        )

                        Divider(color = BorderLight, thickness = 1.dp)

                        Text(
                            text = "${selectedPlan.dayName}-এর জন্য প্রস্তাবিত বিষয়সমূহ:",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )

                        selectedPlan.subjects.forEach { subjectName ->
                            val color = getSubjectColor(subjectName)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = color,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = subjectName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
