package com.example.actv5

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.actv5.ui.theme.Actv5Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actv5Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AgeCalculatorApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AgeCalculatorApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    var currentDateText by remember { mutableStateOf("") }
    var currentTimeText by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance()
            currentDateText = dateFormat.format(now.time)
            currentTimeText = timeFormat.format(now.time)
            delay(1000) // Update every second
        }
    }

    fun calculateAge(birthDate: Calendar) {
        val today = Calendar.getInstance()
        var years = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        var months = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH)
        val days = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH)

        if (months < 0 || (months == 0 && days < 0)) {
            years--
            months += 12
        }
        if (days < 0 && months > 0) { // Check this condition to avoid negative months if already adjusted
            months-- // This might need more refinement for perfect day-based month decrement
        }
         // Adjust months if it became negative after year adjustment
        if (months < 0) {
            months += 12
        }


        ageText = "$years años y $months meses"
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newSelectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            selectedDate = newSelectedDate
            calculateAge(newSelectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Calculadora de Edad Creativa", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { datePickerDialog.show() }) {
            Text(selectedDate?.let { dateFormat.format(it.time) } ?: "Selecciona tu Fecha de Nacimiento")
        }
        Spacer(modifier = Modifier.height(16.dp))

        selectedDate?.let {
            Text("Fecha de Nacimiento: ${dateFormat.format(it.time)}")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text("Fecha Actual: $currentDateText")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Hora Actual: $currentTimeText")
        Spacer(modifier = Modifier.height(16.dp))

        if (ageText.isNotEmpty()) {
            Text("Tu Edad: $ageText", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgeCalculatorAppPreview() {
    Actv5Theme {
        AgeCalculatorApp()
    }
}
