package ph.edu.comteq.recollect

// deck means a container for cards or like a group
data class Deck(
    val id: Int,
    val name: String
)

// flashcard: title = the answer, description = the clue.
data class Flashcard(
    val id: Int,
    val deckId: Int,                    // which deck this card belongs to
    val title: String,
    val description: String,
    val timesCorrect: Int = 0,          // defaults, so new cards start at 0
    val timesIncorrect: Int = 0,
    val lastReviewedDate: Long? = null  // null = never reviewed yet
)

// the Results screen needs.
data class SessionResult(
    val correct: Int,
    val answered: Int,
    val missed: List<Flashcard>,
    val survival: Boolean = false,   // toggle for the survival or lives
    val livesLeft: Int = 0           // lives remaining at the end
)