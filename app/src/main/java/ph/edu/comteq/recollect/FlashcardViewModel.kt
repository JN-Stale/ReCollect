package ph.edu.comteq.recollect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Holds all deck/card logic.
class FlashcardViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao = AppDatabase
        .getInstance(application)
        .recollectDao()

    // Live lists. Room sends a new list after every insert, rename, or delete.
    val decks: StateFlow<List<Deck>> = dao.observeDecks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val cards: StateFlow<List<Flashcard>> = dao.observeCards()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun cardsOf(deckId: Int): List<Flashcard> {
        return cards.value.filter { it.deckId == deckId }
    }

    fun addDeck(name: String) {
        viewModelScope.launch {
            dao.insertDeck(Deck(name = name.trim())) // id stays 0, Room picks the real one
        }
    }

    fun renameDeck(deckId: Int, newName: String) {
        viewModelScope.launch {
            dao.renameDeck(deckId, newName.trim())
        }
    }

    fun deleteDeck(deckId: Int) {
        viewModelScope.launch {
            dao.deleteDeck(deckId) // its cards are deleted too.
        }
    }

    fun addCard(deckId: Int, title: String, description: String) {
        viewModelScope.launch {
            dao.insertCard(
                Flashcard(
                    deckId = deckId,
                    title = title.trim(),
                    description = description.trim()
                )
            )
        }
    }

    fun updateCard(cardId: Int, title: String, description: String) {
        viewModelScope.launch {
            dao.updateCardText(cardId, title.trim(), description.trim())
        }
    }

    fun deleteCard(cardId: Int) {
        viewModelScope.launch {
            dao.deleteCard(cardId)
        }
    }

    // Saves one answer, bumps the right counter and stamps the time
    fun recordAnswer(card: Flashcard, wasCorrect: Boolean) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (wasCorrect) {
                dao.recordCorrect(card.id, now)
            } else {
                dao.recordIncorrect(card.id, now)
            }
        }
    }
}