package com.example.scrollbreak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbreak.model.SessionSettings
import com.example.scrollbreak.model.allDistractingApps

private val CompleteBg1   = Color(0xFF1A0A3B)
private val CompleteBg2   = Color(0xFF2E1065)
private val CompleteGold  = Color(0xFFFBBF24)
private val CompletePurple = Color(0xFF7C3AED)

@Composable
fun SessionCompleteScreen(
    settings: SessionSettings,
    onRestart: () -> Unit
) {
    val appCount = settings.selectedAppIds.size
    val appNames = allDistractingApps
        .filter { it.id in settings.selectedAppIds }
        .joinToString(" · ") { it.emoji }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(CompleteBg2, CompleteBg1),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = "🏆", fontSize = 72.sp)

            Text(
                text = "Session Complete!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "You stayed focused and resisted scrolling.\nThat's a win. 🙌",
                fontSize = 16.sp,
                color = Color(0xFFCBB5FF),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            // Stats row
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(
                        emoji = "⏱️",
                        value = "${settings.allowedAppMinutes}m",
                        label = "Limit"
                    )
                    StatItem(
                        emoji = "🚫",
                        value = "$appCount",
                        label = "Apps blocked"
                    )
                    StatItem(
                        emoji = "🎯",
                        value = "100%",
                        label = "Goal hit"
                    )
                }
            }

            // Blocked apps row
            if (appNames.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White.copy(alpha = 0.07f)
                ) {
                    Text(
                        text = appNames,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onRestart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CompleteGold)
            ) {
                Text(
                    "Start a New Session",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A0A3B)
                )
            }
        }
    }
}

@Composable
private fun StatItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(emoji, fontSize = 22.sp)
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = CompleteGold)
        Text(label, fontSize = 11.sp, color = Color(0xFF9CA3AF), textAlign = TextAlign.Center)
    }
}
