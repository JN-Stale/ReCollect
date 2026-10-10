package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DeckDetailScreen(
    deck: Deck,
    store: FlashcardStore,
    onAddCard: () -> Unit,
    onEditCard: (Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {
    // Only the cards that belong to this deck
    val deckCards = store.cards.filter { it.deckId == deck.id }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        Button(
            onClick = onAddCard,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add card")
        }

        // Empty state, or the list of cards
        if (deckCards.isEmpty()) {
            Text(
                "No cards in this deck yet. Tap \"Add card\" to make your first one.",
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                items(deckCards) { card ->
                    CardRow(
                        card = card,
                        onEdit = { onEditCard(card) },
                        onDelete = { store.deleteCard(card.id) }
                    )
                }
            }
        }
    }
}

// One card in the management list: title, clue, edit and delete buttons
@Composable
fun CardRow(card: Flashcard, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(card.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                // No maxLines. the full clue is always shown, wrapping onto as many lines as it needs
                Text(card.description, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit card")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete card")
            }
        }
    }
}