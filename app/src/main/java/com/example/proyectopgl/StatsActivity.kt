package com.example.proyectopgl

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class StatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = getSharedPreferences("UserStats", Context.MODE_PRIVATE)
            StatsScreen(sharedPreferences) {
                finish()
            }
        }
    }
}

@Composable
fun StatsScreen(sharedPreferences: SharedPreferences, backToQuestions: () -> Unit) {
    var totalCorrectAnswers by remember { mutableStateOf(sharedPreferences.getInt("TotalCorrectAnswers", 0)) }
    var totalWrongAnswers by remember { mutableStateOf(sharedPreferences.getInt("TotalWrongAnswers", 0)) }
    var totalClicks by remember { mutableStateOf(sharedPreferences.getInt("TotalClicks", 0)) }

    val totalQuestions = totalCorrectAnswers + totalWrongAnswers
    val correctPercentage = if (totalQuestions > 0) (totalCorrectAnswers.toFloat() / totalQuestions * 100) else 0.0
    val wrongPercentage = if (totalQuestions > 0) (totalWrongAnswers.toFloat() / totalQuestions * 100) else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("MSI AFTERBURNER", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        StatItem("Preguntas respuestas bien", totalCorrectAnswers)
        StatItem("Preguntas respuestas mal", totalWrongAnswers)
        StatItem("Porcentaje del bien", correctPercentage)
        StatItem("Porcentaje de maldad", wrongPercentage)
        StatItem("Total de pálpitos", totalClicks)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { backToQuestions() },
            modifier = Modifier
                .padding(8.dp)
                .height(50.dp)
                .fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver al modo de Práctica")
        }
    }
}

@Composable
fun StatItem(title: String, value: Any) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(title, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value.toString(), fontWeight = FontWeight.Bold)
    }
}