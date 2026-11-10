package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ResultsScreen(result: SessionResult, onDone: () -> Unit) {
    // Percentage rounded to a whole number. Guard against dividing by zero.
    val accuracy = if (result.answered == 0) 0
    else (result.correct * 100.0 / result.answered).roundToInt()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Session complete",
                style = MaterialTheme.typography.headlineSmall)
        }
        item {
            Text("Score: ${result.correct} of ${result.answered}")
        }
        item {
            Text("Accuracy: $accuracy%")
        }

        if (result.missed.isEmpty()) {
            item {
                Text("You didn't miss a single card!")
            }
        } else {
            item {
                Text("Missed cards", style = MaterialTheme.typography.titleMedium)
            }
            items(result.missed) { card ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(card.description) // full clue
                        Text("Answer: ${card.title}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Button(onClick = onDone) {
                Text("Back to decks")
            }
        }
    }
}