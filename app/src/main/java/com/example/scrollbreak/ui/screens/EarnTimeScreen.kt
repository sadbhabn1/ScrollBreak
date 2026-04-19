package com.example.scrollbreak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbreak.model.ProductivityTask
import com.example.scrollbreak.model.allProductivityTasks
import kotlinx.coroutines.delay

private val EarnBg1     = Color(0xFFF0FDF4)
private val EarnBg2     = Color(0xFFECFDF5)
private val EarnGreen   = Color(0xFF10B981)
private val EarnPurple  = Color(0xFF7C3AED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarnTimeScreen(
    rewardMinutes: Int,
    onTaskCompleted: (earnedMinutes: Int) -> Unit,
    onBack: () -> Unit
) {
    var selectedTask by remember { mutableStateOf<ProductivityTask?>(null) }
    var taskCountdown by remember { mutableIntStateOf(0) }
    var taskRunning by remember { mutableStateOf(false) }
    var taskDone by remember { mutableStateOf(false) }

    // Task countdown ticker
    LaunchedEffect(taskRunning) {
        if (taskRunning && taskCountdown > 0) {
            while (taskCountdown > 0 && taskRunning) {
                delay(1000L)
                taskCountdown--
            }
            if (taskCountdown == 0) {
                taskDone = true
                taskRunning = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Earn Time", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EarnBg1)
            )
        },
        containerColor = EarnBg1
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Complete a task to earn $rewardMinutes bonus minutes 🎁",
                    fontSize = 15.sp,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!taskRunning && !taskDone) {
                items(allProductivityTasks) { task ->
                    TaskCard(
                        task = task,
                        selected = selectedTask?.id == task.id,
                        onClick = { selectedTask = task }
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            selectedTask?.let {
                                taskCountdown = it.durationSeconds
                                taskRunning = true
                            }
                        },
                        enabled = selectedTask != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EarnGreen)
                    ) {
                        Text(
                            "Start Task  →",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            if (taskRunning) {
                item {
                    TaskProgressCard(task = selectedTask!!, remaining = taskCountdown)
                }
            }

            if (taskDone) {
                item {
                    TaskCompleteCard(
                        task = selectedTask!!,
                        rewardMinutes = rewardMinutes,
                        onClaim = { onTaskCompleted(rewardMinutes) }
                    )
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun TaskCard(task: ProductivityTask, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) 2.dp else 1.dp
    val borderColor = if (selected) EarnGreen else Color(0xFFE5E7EB)
    val bg = if (selected) EarnGreen.copy(alpha = 0.07f) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(border, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(task.emoji, fontSize = 28.sp)
        Column(Modifier.weight(1f)) {
            Text(task.label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1F2937))
            Text(
                "${task.durationSeconds}s task",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )
        }
        if (selected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = EarnGreen, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun TaskProgressCard(task: ProductivityTask, remaining: Int) {
    val progress = remaining.toFloat() / task.durationSeconds.toFloat()
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(task.emoji, fontSize = 48.sp)
            Text(task.label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1F2937))
            Text(
                "${remaining}s remaining",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = EarnGreen
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = EarnGreen,
                trackColor = EarnGreen.copy(0.2f)
            )
        }
    }
}

@Composable
private fun TaskCompleteCard(task: ProductivityTask, rewardMinutes: Int, onClaim: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EarnGreen.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🎉", fontSize = 56.sp)
            Text("Task Complete!", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color(0xFF065F46))
            Text(
                "+$rewardMinutes bonus minutes unlocked",
                fontSize = 15.sp,
                color = Color(0xFF374151)
            )
            Button(
                onClick = onClaim,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EarnPurple)
            ) {
                Text("Claim & Resume Focus  🚀", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}
