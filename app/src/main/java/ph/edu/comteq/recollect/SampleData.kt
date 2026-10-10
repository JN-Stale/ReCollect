package ph.edu.comteq.recollect

// Temporary data
val sampleDecks = listOf(
    Deck(id = 1, name = "Philippine Heroes"),
    Deck(id = 2, name = "Computer Parts")
)

val sampleCards = listOf(
    Flashcard(id = 1, deckId = 1, title = "Jose Rizal", description = "National hero who wrote Noli Me Tangere"),
    Flashcard(id = 2, deckId = 1, title = "Andres Bonifacio", description = "Founder of the Katipunan"),
    Flashcard(id = 3, deckId = 2, title = "RAM", description = "Temporary memory the CPU uses while programs are running"),
    Flashcard(id = 4, deckId = 2, title = "Motherboard", description = "Main circuit board that connects all the components")
)