package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Case-insensitive, ignores spaces at the start and end
fun isCorrectAnswer(typed: String, title: String): Boolean =
    typed.trim().equals(title.trim(), ignoreCase = true)

// For lives
const val STARTING_LIVES = 3

@Composable
fun ReviewScreen(
    cards: List<Flashcard>,
    survival: Boolean, // true = Survival mode
    onAnswered: (Flashcard, Boolean) -> Unit,
    onFinished: (SessionResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val sessionCards = remember { cards.shuffled() }   // new random order each session
    var index by remember { mutableStateOf(0) }
    var typed by remember { mutableStateOf("") }
    var wasCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableStateOf(0) }
    var lives by remember { mutableStateOf(STARTING_LIVES) }
    val missed = remember { mutableStateListOf<Flashcard>() }

    // No card means the session is over. In Survival mode it also ends
    // when lives hit 0, but only after the feedback is done (wasCorrect == null),
    // so the player still sees the right answer for the last mistake.
    val outOfLives = survival && lives == 0 && wasCorrect == null
    val card = if (outOfLives) null else sessionCards.getOrNull(index)
    val position = index + 1

    val sessionOver = sessionCards.isNotEmpty() && card == null
    LaunchedEffect(sessionOver) {
        if (sessionOver) {
            onFinished(SessionResult(score, index, missed.toList(), survival, lives))
        }
    }

    LaunchedEffect(wasCorrect) {
        val result = wasCorrect
        if (result != null) {
            delay(if (result) 1000L else 2500L)
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
            item {
                Text("This deck has no cards to review yet.")
            }
        } else if (card != null) {
            if (survival) {
                item {
                    Text("Lives: " + "❤\uFE0F".repeat(lives))
                }
            }
            item {
                Text("Card $position of ${sessionCards.size}")
            }
            item {
                Text(
                    text = card.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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
                item {
                    Text(
                        text = "Correct!",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (wasCorrect == false) {
                item {
                    Text(
                        text = "Incorrect. The answer is: ${card.title}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        if (wasCorrect == null) {
                            val correct = isCorrectAnswer(typed, card.title)
                            wasCorrect = correct
                            if (correct) {
                                score++
                            } else {
                                missed.add(card)
                                if (survival) lives--
                            }
                            onAnswered(card, correct)
                        } else {
                            index++
                            typed = ""
                            wasCorrect = null
                        }
                    }
                ) {
                    Text(if (wasCorrect == null) "Check" else "Next")
                }
            }
        }
    }
}
