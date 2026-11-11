package ph.edu.comteq.recollect

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecollectDao {

    //Decks

    @Insert
    suspend fun insertDeck(deck: Deck)

    @Query("SELECT * FROM decks ORDER BY id ASC")
    fun observeDecks(): Flow<List<Deck>>

    @Query("UPDATE decks SET name = :newName WHERE id = :deckId")
    suspend fun renameDeck(deckId: Int, newName: String)

    // Cards of this deck are deleted automatically (CASCADE)
    @Query("DELETE FROM decks WHERE id = :deckId")
    suspend fun deleteDeck(deckId: Int)

    // Cards

    @Insert
    suspend fun insertCard(card: Flashcard)

    @Query("SELECT * FROM flashcards ORDER BY id ASC")
    fun observeCards(): Flow<List<Flashcard>>

    // Changes only title and description, so the review stats are kept
    @Query(
        """
        UPDATE flashcards
        SET title = :title, description = :description
        WHERE id = :cardId
        """
    )
    suspend fun updateCardText(cardId: Int, title: String, description: String)

    @Query("DELETE FROM flashcards WHERE id = :cardId")
    suspend fun deleteCard(cardId: Int)

    //Review stats

    @Query(
        """
        UPDATE flashcards
        SET timesCorrect = timesCorrect + 1, lastReviewedDate = :reviewedAt
        WHERE id = :cardId
        """
    )
    suspend fun recordCorrect(cardId: Int, reviewedAt: Long)

    @Query(
        """
        UPDATE flashcards
        SET timesIncorrect = timesIncorrect + 1, lastReviewedDate = :reviewedAt
        WHERE id = :cardId
        """
    )
    suspend fun recordIncorrect(cardId: Int, reviewedAt: Long)
}