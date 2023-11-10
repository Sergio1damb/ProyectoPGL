package com.example.proyectopgl

import android.content.Intent
import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectopgl.ui.theme.Purple40

class ModoNormal : ComponentActivity() {
    private val preguntas = listOf(
        Pregunta("Los peruanos comen palomas?", true, R.drawable.imagen1),
        Pregunta("Es Francia mejor que España?", false, R.drawable.imagen2),
        Pregunta("Tiene el profe cierto parecido con Jesús?", true, R.drawable.imagen3),
        Pregunta("El que la saca pa enseñarla es un parguela?", true, R.drawable.imagen4),
        Pregunta("Lo ha enchufao?", false, R.drawable.imagen5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PantallaPreguntas(preguntas, this)
            }
        }
    }
}

data class Pregunta(
    val texto: String,
    val respuesta: Boolean,
    val imagen: Int
)

@Composable
fun PantallaPreguntas(preguntas: List<Pregunta>, context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserStats", Context.MODE_PRIVATE)

    var indiceActual by remember { mutableStateOf(0) }
    var mostrarMensaje by remember { mutableStateOf(false) }
    var mostrarRespuestaCorrecta by remember { mutableStateOf(false) }
    var respuestaSeleccionada by remember { mutableStateOf(false) }

    val preguntaActual = preguntas[indiceActual]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = preguntaActual.imagen),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        )

        ElementoPregunta(
            pregunta = preguntaActual,
            alSeleccionarRespuesta = { respuestaUsuario ->
                respuestaSeleccionada = true
                mostrarMensaje = true
                mostrarRespuestaCorrecta = respuestaUsuario == preguntaActual.respuesta

                val editor = sharedPreferences.edit()
                val totalClicks = sharedPreferences.getInt("TotalClicks", 0)
                editor.putInt("TotalClicks", totalClicks + 1)

                if (respuestaUsuario == preguntaActual.respuesta) {
                    val totalCorrectAnswers = sharedPreferences.getInt("TotalCorrectAnswers", 0)
                    editor.putInt("TotalCorrectAnswers", totalCorrectAnswers + 1)
                } else {
                    val totalWrongAnswers = sharedPreferences.getInt("TotalWrongAnswers", 0)
                    editor.putInt("TotalWrongAnswers", totalWrongAnswers + 1)
                }

                editor.apply()
            },
            respuestaSeleccionada = respuestaSeleccionada,
            respuestaCorrecta = preguntaActual.respuesta
        )

        if (mostrarMensaje) {
            if (mostrarRespuestaCorrecta) {
                Mensaje(texto = "Eres el puto amo, colega. Sigue así.", color = Color.Green)
            } else {
                Mensaje(texto = "Hermano, te la has pegado. Eres un inútil.", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(Modifier.padding(8.dp)) {
            Button(
                onClick = {
                    indiceActual = (indiceActual - 1 + preguntas.size) % preguntas.size
                    mostrarMensaje = false
                    mostrarRespuestaCorrecta = false
                    respuestaSeleccionada = false
                }
            ) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Anterior")
                Text(text = "PREV")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    val randomIndex = (0 until preguntas.size).random()
                    indiceActual = randomIndex
                    mostrarMensaje = false
                    mostrarRespuestaCorrecta = false
                    respuestaSeleccionada = false
                }
            ) {
                Text(text = "RANDOM")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    indiceActual = (indiceActual + 1) % preguntas.size
                    mostrarMensaje = false
                    mostrarRespuestaCorrecta = false
                    respuestaSeleccionada = false
                }
            ) {
                Text(text = "NEXT")
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Siguiente")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, StatsActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Ver Estadisticas")
        }
    }
}

@Composable
fun ElementoPregunta(
    pregunta: Pregunta,
    alSeleccionarRespuesta: (Boolean) -> Unit,
    respuestaSeleccionada: Boolean,
    respuestaCorrecta: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = pregunta.texto,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row {
            Button(
                onClick = {
                    alSeleccionarRespuesta(true)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                , colors = ButtonDefaults.buttonColors(containerColor = (
                        if (respuestaSeleccionada && pregunta.respuesta) Color.Green
                        else if (respuestaSeleccionada && !pregunta.respuesta) Color.Red
                        else Purple40
                        ))
            ) {
                Text(text = "VERDA")
            }

            Button(
                onClick = {
                    alSeleccionarRespuesta(false)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                , colors = ButtonDefaults.buttonColors(containerColor = (
                        if (respuestaSeleccionada && !pregunta.respuesta) Color.Green
                        else if (respuestaSeleccionada && pregunta.respuesta) Color.Red
                        else Purple40
                        ))
            ) {
                Text(text = "BULSHI")
            }
        }
    }
}

@Composable
fun Mensaje(texto: String, color: Color) {
    Text(
        text = texto,
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(8.dp)
    )
}
