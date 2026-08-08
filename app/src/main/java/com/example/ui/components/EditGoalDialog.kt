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
import com.example.data.models.DailyGoal
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EditGoalDialog(
    currentGoal: DailyGoal,
    onDismiss: () -> Unit,
    onConfirmTarget: (targetMinutes: Int) -> Unit,
    onConfirmCompleted: (completedMinutes: Int) -> Unit
) {
    var targetHoursText by remember { mutableStateOf((currentGoal.targetMinutes / 60).toString()) }
    var completedHoursText by remember { mutableStateOf((currentGoal.completedMinutes / 60).toString()) }
    var completedMinsText by remember { mutableStateOf((currentGoal.completedMinutes % 60).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "আজকের পড়াশোনার লক্ষ্য নির্ধারণ",
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
                Text(
                    text = "দৈনিক লক্ষ্য (ঘণ্টা)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )

                // Quick Target Selection Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(2, 3, 4, 5, 6).forEach { hours ->
                        FilterChip(
                            selected = targetHoursText == hours.toString(),
                            onClick = { targetHoursText = hours.toString() },
                            label = { Text("$hours ঘণ্টা") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = targetHoursText,
                    onValueChange = { targetHoursText = it.filter { c -> c.isDigit() } },
                    label = { Text("লক্ষ্যমাত্রা (ঘণ্টা)") },
                    placeholder = { Text("যেমন: ৪") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_target_input")
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Text(
                    text = "আজকের সম্পন্ন হওয়া পড়ার সময় (ঐচ্ছিক)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = completedHoursText,
                        onValueChange = { completedHoursText = it.filter { c -> c.isDigit() } },
                        label = { Text("ঘণ্টা") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("goal_completed_hours_input")
                    )

                    OutlinedTextField(
                        value = completedMinsText,
                        onValueChange = { completedMinsText = it.filter { c -> c.isDigit() } },
                        label = { Text("মিনিট") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("goal_completed_mins_input")
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {
                            val currentTot = (completedHoursText.toIntOrNull() ?: 0) * 60 + (completedMinsText.toIntOrNull() ?: 0) + 30
                            completedHoursText = (currentTot / 60).toString()
                            completedMinsText = (currentTot % 60).toString()
                        },
                        label = { Text("+ ৩০ মিনিট যোগ") }
                    )
                    AssistChip(
                        onClick = {
                            val currentTot = (completedHoursText.toIntOrNull() ?: 0) * 60 + (completedMinsText.toIntOrNull() ?: 0) + 60
                            completedHoursText = (currentTot / 60).toString()
                            completedMinsText = (currentTot % 60).toString()
                        },
                        label = { Text("+ ১ ঘণ্টা যোগ") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val targetHrs = targetHoursText.toIntOrNull() ?: 4
                    val targetMins = targetHrs * 60
                    onConfirmTarget(targetMins)

                    val compHrs = completedHoursText.toIntOrNull() ?: 0
                    val compMins = completedMinsText.toIntOrNull() ?: 0
                    val totalComp = (compHrs * 60) + compMins
                    onConfirmCompleted(totalComp)

                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.testTag("confirm_edit_goal_button")
            ) {
                Text("সংরক্ষণ করুন")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_edit_goal_button")
            ) {
                Text("বাতিল")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
