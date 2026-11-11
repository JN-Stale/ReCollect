package ph.edu.comteq.recollect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ph.edu.comteq.recollect.ui.theme.ReCollectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReCollectApp() {
    val store: FlashcardViewModel = viewModel()
    val decks by store.decks.collectAsStateWithLifecycle()
    val cards by store.cards.collectAsStateWithLifecycle()

    // Theme state: light mode by default (false = light, true = dark)
    var isDarkTheme by remember { mutableStateOf(false) }

    // State variable that decides which screen is shown
    var currentScreen by remember { mutableStateOf(Screen.DECK_LIST) }
    // Keep only the id of the tapped deck, then look up the current Deck from the store
    var selectedDeckId by remember { mutableStateOf<Int?>(null) }
    val selectedDeck = decks.find { it.id == selectedDeckId } // null if nothing matches
    var modeDeck by remember { mutableStateOf<Deck?>(null) } // deck waiting for a mode choice
    var survivalMode by remember { mutableStateOf(false) } // the chosen mode
    // The id of the card being edited. null means we're adding a new card.
    var editingCardId by remember { mutableStateOf<Int?>(null) }
    val editingCard = cards.find { it.id == editingCardId } // null when adding
    var sessionResult by remember { mutableStateOf<SessionResult?>(null) }

    // used by the top bar arrow and the phone's Back button
    fun goBack() {
        currentScreen = if (currentScreen == Screen.CARD_FORM) {
            Screen.DECK_DETAIL
        } else {
            Screen.DECK_LIST
        }
    }

    // The phone's Back button or swipe. Off on the Deck List so Back closes the app there.
    BackHandler(enabled = currentScreen != Screen.DECK_LIST) {
        goBack()
    }

    ReCollectTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        when (currentScreen) {
                            Screen.DECK_LIST -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.appicon),
                                        contentDescription = "ReCollect logo",
                                        modifier = Modifier.size(56.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "ReCollect",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Screen.DECK_DETAIL -> Text(selectedDeck?.name ?: "")
                            Screen.CARD_FORM -> Text(
                                if (editingCard == null) "Add card" else "Edit card"
                            )
                            Screen.REVIEW -> Text(selectedDeck?.name ?: "Review")
                            Screen.RESULTS -> Text("Results")
                            Screen.ABOUT -> Text("About")
                        }
                    },
                    navigationIcon = {
                        if (currentScreen != Screen.DECK_LIST) {
                            IconButton(onClick = { goBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = {
                        if (currentScreen == Screen.DECK_LIST) {
                            IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                                Icon(
                                    imageVector = if (isDarkTheme) {
                                        Icons.Default.LightMode
                                    } else {
                                        Icons.Default.DarkMode
                                    },
                                    contentDescription = if (isDarkTheme) {
                                        "Switch to light mode"
                                    } else {
                                        "Switch to dark mode"
                                    }
                                )

                                IconButton(onClick = { currentScreen = Screen.ABOUT }) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "About ReCollect"
                                    )
                                }
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
                    },
                    onPlayDeck = { deck ->
                        modeDeck = deck
                    }
                )
                Screen.DECK_DETAIL -> {
                    if (selectedDeck != null) {
                        DeckDetailScreen(
                            deck = selectedDeck,
                            store = store,
                            onAddCard = {
                                editingCardId = null // null = adding
                                currentScreen = Screen.CARD_FORM
                            },
                            onEditCard = { card ->
                                editingCardId = card.id // remember which card
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
                                currentScreen = Screen.DECK_DETAIL // back to the card list
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                Screen.REVIEW -> {
                    if (selectedDeck != null) {
                        ReviewScreen(
                            cards = store.cardsOf(selectedDeck.id),
                            survival = survivalMode,
                            onAnswered = { card, correct -> store.recordAnswer(card, correct) },
                            onFinished = { result ->
                                sessionResult = result
                                currentScreen = Screen.RESULTS
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                Screen.RESULTS -> {
                    sessionResult?.let { result ->
                        ResultsScreen(
                            result = result,
                            onDone = { currentScreen = Screen.DECK_LIST },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }

                Screen.ABOUT -> AboutScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Mode selection dialog when a deck is chosen to review
        modeDeck?.let { deck ->
            AlertDialog(
                onDismissRequest = { modeDeck = null },          // tap outside to cancel
                title = {
                    Text("Start review")
                },
                text = {
                    Text("Survival gives you 3 lives and ends when they run out.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDeckId = deck.id
                            survivalMode = true
                            modeDeck = null
                            currentScreen = Screen.REVIEW
                        }
                    ) {
                        Text("Survival")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            selectedDeckId = deck.id
                            survivalMode = false
                            modeDeck = null
                            currentScreen = Screen.REVIEW
                        }
                    ) {
                        Text("Normal")
                    }
                }
            )
        }
    }
}
