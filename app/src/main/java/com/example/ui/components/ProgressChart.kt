package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ProgressChart(
    dailyHours: List<Float>, // Mon to Sun hours e.g. [4.5, 5, 3, 6, 4, 5.5, 2]
    modifier: Modifier = Modifier
) {
    val days = listOf("সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি", "রবি")
    val maxHours = (dailyHours.maxOrNull() ?: 6f).coerceAtLeast(1f)

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("progress_chart")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "সাপ্তাহিক পড়ার সময় (ঘণ্টা)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                dailyHours.forEachIndexed { index, hours ->
                    val dayName = days.getOrElse(index) { "" }
                    val heightRatio = (hours / maxHours).coerceIn(0.1f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        // Value label on top
                        Text(
                            text = if (hours % 1f == 0f) "${hours.toInt()}ঘ." else "${hours}ঘ.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            ),
                            color = if (hours == maxHours) PrimaryBlue else TextMuted
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Bar
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .fillMaxHeight(heightRatio * 0.75f)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    if (hours == maxHours) PrimaryBlue else PrimaryBlueLight
                                )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Day label
                        Text(
                            text = dayName,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (hours == maxHours) PrimaryBlue else TextMuted
                        )
                    }
                }
            }
        }
    }
}
