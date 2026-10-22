package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Case-insensitive, ignores spaces at the start and end
fun isCorrectAnswer(typed: String, title: String): Boolean =
    typed.trim().equals(title.trim(), ignoreCase = true)

@Composable
fun ReviewScreen(
    cards: List<Flashcard>,
    onAnswered: (Flashcard, Boolean) -> Unit,   // tells the store the result
    onFinished: () -> Unit,                      // leaves the screen
    modifier: Modifier = Modifier
) {
    val sessionCards = remember { cards }       // fixed list for this session
    var index by remember { mutableStateOf(0) }             // which card we are on
    var typed by remember { mutableStateOf("") }            // what the user typed
    var wasCorrect by remember { mutableStateOf<Boolean?>(null) }  // null = not checked yet
    var score by remember { mutableStateOf(0) }

    // One place decides when the session ends.
    // Survival mode later becomes: index >= sessionCards.size || lives == 0
    val sessionOver = index >= sessionCards.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (sessionCards.isEmpty()) {
            item { Text("This deck has no cards to review yet.") }
            item { Button(onClick = onFinished) { Text("Back to decks") } }
        } else if (sessionOver) {
            item { Text("Session finished", style = MaterialTheme.typography.headlineSmall) }
            item { Text("Score: $score of ${sessionCards.size}") }
            item { Button(onClick = onFinished) { Text("Back to decks") } }
        } else {
            val card = sessionCards[index]
            item { Text("Card ${index + 1} of ${sessionCards.size}") }
            // No maxLines here, so the clue is never cut off
            item { Text(card.description, style = MaterialTheme.typography.bodyLarge) }
            item {
                OutlinedTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    label = { Text("Your answer") },
                    singleLine = true,
                    enabled = wasCorrect == null,    // locked after checking
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
                        // First tap: check the answer
                        val correct = isCorrectAnswer(typed, card.title)
                        wasCorrect = correct
                        if (correct) score++
                        onAnswered(card, correct)
                    } else {
                        // Second tap: go to the next card
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
