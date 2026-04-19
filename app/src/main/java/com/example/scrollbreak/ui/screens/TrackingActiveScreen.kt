package com.example.scrollbreak.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbreak.model.SessionManager
import com.example.scrollbreak.service.AppTrackerService

private val TrackingBg = Color(0xFF1A0A3B)
private val TrackingAccent = Color(0xFF7C3AED)

@Composable
fun TrackingActiveScreen(onStopTracking: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrackingBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("👁️", fontSize = 72.sp)
            
            Text(
                "Tracking Active",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                "You can now close ScrollBreak.\nWe are running in the background, monitoring your selected apps. Once you hit your allowed limit, we'll pull you out.",
                fontSize = 16.sp,
                color = Color(0xFFD8B4FE),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    SessionManager.isTracking = false
                    context.stopService(Intent(context, AppTrackerService::class.java))
                    onStopTracking()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TrackingAccent)
            ) {
                Text("Stop Tracking & Cancel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
