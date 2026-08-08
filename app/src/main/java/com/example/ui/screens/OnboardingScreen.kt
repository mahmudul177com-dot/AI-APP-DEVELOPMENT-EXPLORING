package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onFinishOnboarding: (examTarget: String, selectedSubjects: List<String>, dailyHours: String, goal: String) -> Unit
) {
    var step by remember { mutableStateOf(1) }

    var examTarget by remember { mutableStateOf("এইচএসসি প্রস্তুতি") }
    var selectedSubjects by remember { mutableStateOf(setOf("পদার্থবিজ্ঞান", "রসায়ন", "জীববিজ্ঞান", "উচ্চতর গণিত", "ইংরেজি", "আইসিটি")) }
    var dailyHours by remember { mutableStateOf("৪ ঘণ্টা") }
    var goal by remember { mutableStateOf("সিলেবাস শেষ করা") }

    val examOptions = listOf("এইচএসসি প্রস্তুতি", "বিশ্ববিদ্যালয় ভর্তি", "মেডিকেল ভর্তি", "প্রকৌশল (ইঞ্জিনিয়ারিং) ভর্তি", "অন্যান্য")
    val allSubjects = listOf("পদার্থবিজ্ঞান", "রসায়ন", "জীববিজ্ঞান", "উচ্চতর গণিত", "ইংরেজি", "আইসিটি")
    val timeOptions = listOf("২ ঘণ্টা", "৩ ঘণ্টা", "৪ ঘণ্টা", "৫ ঘণ্টা", "৬+ ঘণ্টা")
    val goalOptions = listOf("সিলেবাস শেষ করা", "দুর্বল বিষয় উন্নত করা", "পরীক্ষার জন্য রিভিশন", "নিয়মিত পড়া ধরে রাখা")

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            if (step > 1 && step < 5) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { step-- },
                        modifier = Modifier.testTag("onboarding_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "আগের ধাপে যান",
                            tint = TextPrimary
                        )
                    }
                    Text(
                        text = "ধাপ $step / ৪",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMuted,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress Dots
                if (step <= 4) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        (1..4).forEach { i ->
                            Box(
                                modifier = Modifier
                                    .size(width = if (i == step) 28.dp else 10.dp, height = 10.dp)
                                    .clip(CircleShape)
                                    .background(if (i == step) PrimaryBlue else PrimaryBlueLight)
                            )
                        }
                    }
                }

                when (step) {
                    1 -> {
                        OnboardingHeader(
                            title = "আপনি কিসের প্রস্তুতি নিচ্ছেন?",
                            subtitle = "আপনার কাঙ্ক্ষিত পরীক্ষা বা লক্ষ্য নির্বাচন করুন"
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        examOptions.forEach { option ->
                            SelectableCard(
                                title = option,
                                isSelected = examTarget == option,
                                onClick = { examTarget = option },
                                testTag = "exam_option_$option"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    2 -> {
                        OnboardingHeader(
                            title = "আপনি কোন কোন বিষয় পড়ছেন?",
                            subtitle = "আপনার প্রধান পড়ার বিষয়সমূহ সিলেক্ট করুন"
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        allSubjects.forEach { subject ->
                            val isSelected = selectedSubjects.contains(subject)
                            SelectableCard(
                                title = subject,
                                isSelected = isSelected,
                                onClick = {
                                    selectedSubjects = if (isSelected) {
                                        if (selectedSubjects.size > 1) selectedSubjects - subject else selectedSubjects
                                    } else {
                                        selectedSubjects + subject
                                    }
                                },
                                testTag = "subject_option_$subject"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    3 -> {
                        OnboardingHeader(
                            title = "প্রতিদিন কত ঘণ্টা পড়তে পারবেন?",
                            subtitle = "ধারাবাহিকতা বজায় রাখতে একটি বাস্তবসম্মত সময় বেছে নিন"
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        timeOptions.forEach { option ->
                            SelectableCard(
                                title = option,
                                isSelected = dailyHours == option,
                                onClick = { dailyHours = option },
                                testTag = "time_option_$option"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    4 -> {
                        OnboardingHeader(
                            title = "আপনার মূল লক্ষ্য কী?",
                            subtitle = "আপনার লক্ষ্য অনুযায়ী দৈনিক পড়ার রুটিন তৈরি করা হবে"
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        goalOptions.forEach { option ->
                            SelectableCard(
                                title = option,
                                isSelected = goal == option,
                                onClick = { goal = option },
                                testTag = "goal_option_$option"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    5 -> {
                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "আপনার পড়ার রুটিন প্রস্তুত!",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            ),
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "$examTarget-এর জন্য ${selectedSubjects.size}টি বিষয় নিয়ে আপনার পড়ালেখার একটি সুন্দর পরিকল্পনা তৈরি হয়েছে।",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }

            // Bottom Action Button
            Button(
                onClick = {
                    if (step < 5) {
                        step++
                    } else {
                        onFinishOnboarding(
                            examTarget,
                            selectedSubjects.toList(),
                            dailyHours,
                            goal
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("onboarding_next_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (step < 5) "পরবর্তী" else "পড়াশোনা শুরু করুন",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingHeader(title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectableCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) PrimaryBlueLight else SurfaceLight,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) PrimaryBlue else BorderLight
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isSelected) PrimaryBlue else TextPrimary
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
