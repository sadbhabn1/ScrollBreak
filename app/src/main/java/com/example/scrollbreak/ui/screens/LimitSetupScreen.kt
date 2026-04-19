package com.example.scrollbreak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbreak.model.SessionSettings

private val BgColor2      = Color(0xFFF8F5FF)
private val PrimaryPurple2 = Color(0xFF7C3AED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimitSetupScreen(
    settings: SessionSettings,
    onSettingsChanged: (SessionSettings) -> Unit,
    onBack: () -> Unit,
    onStartFocus: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Set Your Limits", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgColor2)
            )
        },
        containerColor = BgColor2
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = "Customize your focus session below.",
                fontSize = 15.sp,
                color = Color(0xFF6B7280)
            )

            LimitSliderCard(
                title = "Allowed App Time",
                emoji = "⏱️",
                value = settings.allowedAppMinutes,
                range = 1f..60f,
                unit = "min",
                steps = 58,
                onValueChange = { onSettingsChanged(settings.copy(allowedAppMinutes = it)) }
            )

            LimitSliderCard(
                title = "Cooldown Duration",
                emoji = "❄️",
                value = settings.cooldownMinutes,
                range = 1f..30f,
                unit = "min",
                steps = 28,
                onValueChange = { onSettingsChanged(settings.copy(cooldownMinutes = it)) }
            )

            LimitSliderCard(
                title = "Reward Minutes",
                emoji = "🎁",
                value = settings.rewardMinutes,
                range = 1f..30f,
                unit = "min",
                steps = 28,
                onValueChange = { onSettingsChanged(settings.copy(rewardMinutes = it)) }
            )

            Spacer(Modifier.height(4.dp))

            // Summary pill
            SummaryCard(settings)

            Button(
                onClick = onStartFocus,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple2)
            ) {
                Text("Start Tracking  🚀", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LimitSliderCard(
    title: String,
    emoji: String,
    value: Int,
    range: ClosedFloatingPointRange<Float>,
    unit: String,
    steps: Int,
    onValueChange: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = emoji, fontSize = 22.sp)
                    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1F2937))
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryPurple2.copy(alpha = 0.10f)
                ) {
                    Text(
                        text = "$value $unit",
                        color = PrimaryPurple2,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Slider(
                value = value.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = range,
                steps = steps,
                colors = SliderDefaults.colors(
                    thumbColor = PrimaryPurple2,
                    activeTrackColor = PrimaryPurple2,
                    inactiveTrackColor = PrimaryPurple2.copy(alpha = 0.20f)
                )
            )
        }
    }
}

@Composable
private fun SummaryCard(settings: SessionSettings) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryPurple2.copy(alpha = 0.07f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SummaryItem("⏱️", "${settings.allowedAppMinutes}m", "Allowed")
            SummaryItem("❄️", "${settings.cooldownMinutes}m", "Cooldown")
            SummaryItem("🎁", "${settings.rewardMinutes}m", "Reward")
        }
    }
}

@Composable
private fun SummaryItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 22.sp)
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = PrimaryPurple2)
        Text(label, fontSize = 12.sp, color = Color(0xFF9CA3AF))
    }
}
