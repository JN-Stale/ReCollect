package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DeckDetailScreen(
    deck: Deck,
    store: FlashcardViewModel,
    onAddCard: () -> Unit,
    onEditCard: (Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {
    var cardToDelete by remember { mutableStateOf<Flashcard?>(null) }
    val cards by store.cards.collectAsStateWithLifecycle()

    // Only the cards that belong to this deck
    val deckCards = cards.filter { it.deckId == deck.id }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
    ) {
        Button(
            onClick = onAddCard,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add card")
        }

        // Empty state, or the list of cards
        if (deckCards.isEmpty()) {
            Text(
                text = "No cards in this deck yet. Tap \"Add card\" to make your first one.",
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                items(deckCards) { card ->
                    CardRow(
                        card = card,
                        onEdit = { onEditCard(card) },
                        onDelete = { cardToDelete = card }
                    )
                }
            }
        }
    }

    // Delete confirmation for a single card
    val deletingCard = cardToDelete
    if (deletingCard != null) {
        AlertDialog(
            onDismissRequest = { cardToDelete = null },
            title = {
                Text("Delete card?")
            },
            text = {
                Text("\"${deletingCard.title}\" will be deleted.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        store.deleteCard(deletingCard.id)
                        cardToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { cardToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// One card in the management list: title, clue, edit and delete buttons
@Composable
fun CardRow(
    card: Flashcard,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 8.dp,
                end = 4.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                // No maxLines. the full clue is always shown, wrapping onto as many lines as it
                // needs
                Text(
                    text = card.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit card"
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete card"
                )
            }
        }
    }
}
