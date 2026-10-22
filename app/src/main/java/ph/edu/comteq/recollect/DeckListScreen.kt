package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DeckListScreen(
    store: FlashcardStore,
    onDeckClick: (Deck) -> Unit,
    onPlayDeck: (Deck) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    // Which deck a popup is about. null means that popup is hidden.
    var deckToRename by remember { mutableStateOf<Deck?>(null) }
    var deckToDelete by remember { mutableStateOf<Deck?>(null) }

    val visibleDecks = store.decks.filter { it.name.contains(searchText, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)   // top = space under the top bar
    ) {
        // Search bar + new-deck button
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search decks") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "New deck")
            }
        }

        // Empty state, or the list
        if (visibleDecks.isEmpty()) {
            Text(
                text = if (store.decks.isEmpty()) "No decks yet. Tap the pencil to create your first deck."
                else "No deck matches \"$searchText\".",
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                items(visibleDecks) { deck ->
                    DeckCard(
                        deck = deck,
                        cardCount = store.cards.count { it.deckId == deck.id },
                        onClick = { onDeckClick(deck) },
                        onPlay = { onPlayDeck(deck) },
                        onRename = { deckToRename = deck },   // remember WHICH deck, which opens the popup
                        onDelete = { deckToDelete = deck }
                    )
                }
            }
        }
    }

    // New deck popup
    if (showAddDialog) {
        var newDeckName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("New deck") },
            text = {
                OutlinedTextField(
                    value = newDeckName,
                    onValueChange = { newDeckName = it },
                    label = { Text("Deck name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newDeckName.isNotBlank()) {
                        store.addDeck(newDeckName)
                        showAddDialog = false
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }

    val renamingDeck = deckToRename
    if (renamingDeck != null) {
        var newName by remember { mutableStateOf(renamingDeck.name) }   // starts with the current name
        AlertDialog(
            onDismissRequest = { deckToRename = null },
            title = { Text("Rename deck") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Deck name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName.isNotBlank()) {
                        store.renameDeck(renamingDeck.id, newName)
                        deckToRename = null
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { deckToRename = null }) { Text("Cancel") }
            }
        )
    }

    // Delete confirmation. Deleting a deck also deletes its cards, so we ask first.
    val deletingDeck = deckToDelete
    if (deletingDeck != null) {
        val cardCount = store.cards.count { it.deckId == deletingDeck.id }
        AlertDialog(
            onDismissRequest = { deckToDelete = null },
            title = { Text("Delete deck?") },
            text = { Text("\"${deletingDeck.name}\" and its $cardCount cards will be deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    store.deleteDeck(deletingDeck.id)
                    deckToDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { deckToDelete = null }) { Text("Cancel") }
            }
        )
    }
}

// One deck card: name, card count, pen (options menu), play button
@Composable
fun DeckCard(
    deck: Deck,
    cardCount: Int,
    onClick: () -> Unit,
    onPlay: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }   // each card has its own menu state

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(deck.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$cardCount cards", style = MaterialTheme.typography.bodySmall)
            }

            // The Box makes the menu appear right under the pen
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Deck options")
                }
                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        onClick = {
                            menuOpen = false
                            onRename()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            menuOpen = false
                            onDelete()
                        }
                    )
                }
            }

            IconButton(onClick = onPlay) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Review ${deck.name}",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}