package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Case-insensitive, ignores spaces at the start and end
fun isCorrectAnswer(typed: String, title: String): Boolean =
    typed.trim().equals(title.trim(), ignoreCase = true)

@Composable
fun ReviewScreen(
    cards: List<Flashcard>,
    onAnswered: (Flashcard, Boolean) -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sessionCards = remember { cards }
    var index by remember { mutableIntStateOf(0) }
    var typed by remember { mutableStateOf("") }
    var wasCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val card = sessionCards.getOrNull(index)
    val position = index + 1   // calculated here so the LazyColumn never reads index

    // after an answer is checked, wait, then move to the next card
    LaunchedEffect(wasCorrect) {
        val result = wasCorrect
        if (result != null) {
            delay(if (result) 1000.milliseconds else 2500.milliseconds)   // more time to read a wrong answer
            index++
            typed = ""
            wasCorrect = null
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (sessionCards.isEmpty()) {
            item { Text("This deck has no cards to review yet.") }
        } else if (card == null) {
            item { Text("Session finished", style = MaterialTheme.typography.headlineSmall) }
            item { Text("Score: $score of ${sessionCards.size}") }
            item { Button(onClick = onFinished) { Text("Back to decks") } }
        } else {
            item { Text("Card $position of ${sessionCards.size}") }
            item { Text(card.description, style = MaterialTheme.typography.bodyLarge) }
            item {
                OutlinedTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    label = { Text("Your answer") },
                    singleLine = true,
                    enabled = wasCorrect == null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (wasCorrect == true) {
                item { Text("Correct!", color = MaterialTheme.colorScheme.primary) }
            }
            if (wasCorrect == false) {
                item {
                    Text(
                        "Incorrect. The answer is: ${card.title}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            item {
                Button(onClick = {
                    if (wasCorrect == null) {
                        val correct = isCorrectAnswer(typed, card.title)
                        wasCorrect = correct
                        if (correct) score++
                        onAnswered(card, correct)
                    } else {
                        // Skip the wait. Changing wasCorrect cancels the LaunchedEffect.
                        index++
                        typed = ""
                        wasCorrect = null
                    }
                }) {
                    Text(if (wasCorrect == null) "Check" else "Next")
                }
            }
        }
    }
}
