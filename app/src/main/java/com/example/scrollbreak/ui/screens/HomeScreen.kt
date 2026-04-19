package com.example.scrollbreak.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Soft pastel palette
private val Lavender   = Color(0xFFD8B4FE)
private val Rose       = Color(0xFFFDA4AF)
private val Sky        = Color(0xFF93C5FD)
private val Mint       = Color(0xFF6EE7B7)
private val Peach      = Color(0xFFFBBF24)

@Composable
fun HomeScreen(onGetStarted: () -> Unit) {
    // Infinite animated gradient shift
    val infiniteTransition = rememberInfiniteTransition(label = "bgAnim")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientOffset"
    )

    // Interpolate between two gradient states based on offset
    val color1 = lerp(Lavender, Rose, offset)
    val color2 = lerp(Sky,      Mint, offset)
    val color3 = lerp(Peach,    Lavender, offset)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(color1, color2, color3),
                    radius = 1400f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Floating card
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.82f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "🧘",
                    fontSize = 64.sp
                )

                Text(
                    text = "ScrollBreak",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF3B1F6E),
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "Reclaim your attention.\nOne session at a time.",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C3AED)
                    )
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Text(
                    text = "No account needed · 100% local",
                    fontSize = 12.sp,
                    color = Color(0xFFB0B8C8)
                )
            }
        }
    }
}

// Simple lerp for Color
private fun lerp(a: Color, b: Color, t: Float): Color {
    return Color(
        red   = a.red   + (b.red   - a.red)   * t,
        green = a.green + (b.green - a.green) * t,
        blue  = a.blue  + (b.blue  - a.blue)  * t,
        alpha = 1f
    )
}
