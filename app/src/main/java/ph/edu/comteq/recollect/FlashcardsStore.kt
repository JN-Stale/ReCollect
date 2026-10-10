package ph.edu.comteq.recollect

import androidx.compose.runtime.mutableStateListOf

// Holds all decks and cards in memory. All changes go through its functions.
class FlashcardStore {
    val decks = mutableStateListOf<Deck>()
    val cards = mutableStateListOf<Flashcard>()

    init {
        // sample data
        decks.addAll(sampleDecks)
        cards.addAll(sampleCards)
    }

    fun addDeck(name: String) {
        // New id = highest existing id + 1 (or 1 if there are no decks yet)
        val newId = (decks.maxOfOrNull { it.id } ?: 0) + 1
        decks.add(Deck(id = newId, name = name.trim()))
    }

    fun deleteDeck(deckId: Int) {
        decks.removeAll { it.id == deckId }
        cards.removeAll { it.deckId == deckId }   // deleting a deck also deletes its cards
    }

    fun renameDeck(deckId: Int, newName: String) {
        val index = decks.indexOfFirst { it.id == deckId }   // position in the list, or -1 if not found
        if (index != -1) {
            // copy() makes a new Deck with only the name changed
            decks[index] = decks[index].copy(name = newName.trim())
        }
    }

    fun deleteCard(cardId: Int) {
        cards.removeAll { it.id == cardId }
    }
    fun addCard(deckId: Int, title: String, description: String) {
        val newId = (cards.maxOfOrNull { it.id } ?: 0) + 1
        // timesCorrect, timesIncorrect and lastReviewedDate use their defaults (0, 0, null)
        cards.add(Flashcard(id = newId, deckId = deckId, title = title.trim(), description = description.trim()))
    }

    fun updateCard(cardId: Int, title: String, description: String) {
        val index = cards.indexOfFirst { it.id == cardId }
        if (index != -1) {
            // copy() changes only title and description, so the card's review stats are kept
            cards[index] = cards[index].copy(title = title.trim(), description = description.trim())
        }
    }
}