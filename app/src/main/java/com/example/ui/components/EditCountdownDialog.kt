package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary

@Composable
fun EditCountdownDialog(
    currentDays: Int,
    currentExamName: String,
    onDismiss: () -> Unit,
    onConfirm: (days: Int, examName: String) -> Unit
) {
    var examName by remember { mutableStateOf(currentExamName) }
    var daysText by remember { mutableStateOf(currentDays.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "পরীক্ষার কাউন্টডাউন",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = examName,
                    onValueChange = { examName = it },
                    label = { Text("পরীক্ষার নাম") },
                    placeholder = { Text("যেমন: এইচএসসি ২০২৬ / মেডিকেল ভর্তি পরীক্ষা") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_exam_name_field")
                )

                OutlinedTextField(
                    value = daysText,
                    onValueChange = { daysText = it.filter { char -> char.isDigit() } },
                    label = { Text("বাকি দিন") },
                    placeholder = { Text("১২৭") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_exam_days_field")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val days = daysText.toIntOrNull() ?: 100
                    onConfirm(days, examName.ifBlank { "পরীক্ষা" })
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.testTag("confirm_countdown_button")
            ) {
                Text("সংরক্ষণ করুন")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_countdown_button")
            ) {
                Text("বাতিল")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
