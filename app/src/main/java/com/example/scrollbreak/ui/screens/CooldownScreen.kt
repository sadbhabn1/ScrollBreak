package com.example.scrollbreak.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val CoolBg1 = Color(0xFF0F2027)
private val CoolBg2 = Color(0xFF203A43)
private val CoolBg3 = Color(0xFF2C5364)
private val CoolAccent = Color(0xFF38BDF8)

@Composable
fun CooldownScreen(
    cooldownMinutes: Int,
    onEarnTime: () -> Unit,
    onCooldownComplete: () -> Unit
) {
    val totalSeconds = cooldownMinutes * 60
    var remaining by remember { mutableIntStateOf(totalSeconds) }

    LaunchedEffect(Unit) {
        while (remaining > 0) {
            delay(1000L)
            remaining--
        }
        onCooldownComplete()
    }

    val minutes = remaining / 60
    val seconds = remaining % 60
    val progress = remaining.toFloat() / totalSeconds.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(colors = listOf(CoolBg1, CoolBg2, CoolBg3))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = "🧊", fontSize = 64.sp)

            Text(
                text = "Cooldown Active",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "You've hit your daily limit.\nTake a break — your future self will thank you.",
                fontSize = 15.sp,
                color = Color(0xFFBAE6FD),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            // Big timer
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = "%02d:%02d".format(minutes, seconds),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CoolAccent
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = CoolAccent,
                        trackColor = CoolAccent.copy(alpha = 0.2f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("cooling down…", fontSize = 13.sp, color = Color(0xFF7DD3FC))
                }
            }

            Text(
                text = "Or skip the wait by completing a task:",
                fontSize = 14.sp,
                color = Color(0xFF94A3B8)
            )

            Button(
                onClick = onEarnTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoolAccent)
            ) {
                Text(
                    "Earn Time ✨",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F2027)
                )
            }
        }
    }
}
