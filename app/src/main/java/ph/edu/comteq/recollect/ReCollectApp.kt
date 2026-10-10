package ph.edu.comteq.recollect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReCollectApp() {
    val store = remember { FlashcardStore() }

    // state variable that decides which screen is shown
    var currentScreen by remember { mutableStateOf(Screen.DECK_LIST) }
    // Keep only the id of the tapped deck, then look up the current Deck from the store
    var selectedDeckId by remember { mutableStateOf<Int?>(null) }
    val selectedDeck = store.decks.find { it.id == selectedDeckId }   // null if nothing matches
    // The id of the card being edited. null means we're adding a new card.
    var editingCardId by remember { mutableStateOf<Int?>(null) }
    val editingCard = store.cards.find { it.id == editingCardId }   // null when adding

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (currentScreen) {
                        Screen.DECK_LIST -> Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.appicon),
                                contentDescription = "ReCollect logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("ReCollect", fontWeight = FontWeight.Bold)
                        }
                        Screen.DECK_DETAIL -> Text(selectedDeck?.name ?: "")
                        Screen.CARD_FORM -> Text(if (editingCard == null) "Add card" else "Edit card")
                    }
                },
                navigationIcon = {
                    if (currentScreen != Screen.DECK_LIST) {
                        IconButton(onClick = {
                            currentScreen = if (currentScreen == Screen.CARD_FORM) Screen.DECK_DETAIL else Screen.DECK_LIST
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    // Settings gear (visual only for now), home screen only
                    if (currentScreen == Screen.DECK_LIST) {
                        IconButton(onClick = { /* no settings screen yet */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // Show a different screen depending on currentScreen
        when (currentScreen) {
            Screen.DECK_LIST -> DeckListScreen(
                store = store,
                modifier = Modifier.padding(innerPadding),
                onDeckClick = { deck ->
                    selectedDeckId = deck.id
                    currentScreen = Screen.DECK_DETAIL
                }
            )
            Screen.DECK_DETAIL -> {
                if (selectedDeck != null) {
                    DeckDetailScreen(
                        deck = selectedDeck,
                        store = store,
                        onAddCard = {
                            editingCardId = null                  // null = adding
                            currentScreen = Screen.CARD_FORM
                        },
                        onEditCard = { card ->
                            editingCardId = card.id               // remember which card
                            currentScreen = Screen.CARD_FORM
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            Screen.CARD_FORM -> {
                if (selectedDeck != null) {
                    CardFormScreen(
                        card = editingCard,
                        onSave = { title, description ->
                            if (editingCard == null) {
                                store.addCard(selectedDeck.id, title, description)
                            } else {
                                store.updateCard(editingCard.id, title, description)
                            }
                            currentScreen = Screen.DECK_DETAIL    // back to the card list
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
