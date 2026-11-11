package ph.edu.comteq.recollect

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// deck means a container for cards or like a group
@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // 0 means "not saved yet", Room fills in the real id
    val name: String
)

// flashcard: title = the answer, description = the clue.
@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = Deck::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE // deleting a deck deletes its cards
        )
    ],
    indices = [Index("deckId")]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deckId: Int, // which deck this card belongs to
    val title: String,
    val description: String,
    val timesCorrect: Int = 0, // defaults, so new cards start at 0
    val timesIncorrect: Int = 0,
    val lastReviewedDate: Long? = null // null = never reviewed yet
)

// the Results screen needs.
data class SessionResult(
    val correct: Int,
    val answered: Int,
    val missed: List<Flashcard>,
    val survival: Boolean = false, // toggle for the survival or lives
    val livesLeft: Int = 0 // lives remaining at the end
)