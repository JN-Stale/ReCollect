# AI Usage Disclosure

**This submission was co-created with AI.**

Project: ReCollect, an Android flashcard app (Kotlin, Jetpack Compose, Android Studio), Track A (utility / data-driven)

AI tool used: Claude, Built in Chatbot

## Summary

I started building ReCollect on my own. After that, I asked Claude for help, and it provided me bug fixes, alternate solutions, and code suggestions which I used as a reference while learning. I am disclosing this openly because AI-written code is part of the current project.

## What I did myself

- Chose the project direction: an identification-style flashcard app where the user sees a card's description (the clue) and types its title (the answer)
- Chose the track (A) and the app title, ReCollect
- Decided the data model: Deck (id, name) and Flashcard (id, deckId, title, description, timesCorrect, timesIncorrect, lastReviewedDate)
- Decided the storage approach: in-memory first, then Room, keeping the database limited to decks and their cards
- Decided to use a TopAppBar and a single current-screen state variable for navigation, not intents
- Made the interface decisions: the home screen wireframe, the pen-icon dropdown (Rename, Delete) on deck cards, the extra space under the top bar, and the rule that clues are never cut off
- Designed and chose the app logo and decided to use it as the launcher icon
- Decided the product direction: no features that restrict when cards can be reviewed (I rejected spaced-repetition scheduling and a mastery-level feature), and chose Survival mode and sharing a deck as text as the extra features
- Debug the application, trying alternatives. and syntax learned from the lessons.
- Building the first skeleton of the project where the buttons worked.
- Set up the Android Studio environment, ran the app on my device, and made the commits

## What AI did

- Provided code suggestions, alternatives, and bug fixes:
  1. The sample data
  2. Suggested in-memory store
  3. Navigation fixes
  4. Deck Detail screen initial layout

- Provided code alternatives and explanations to restyle the home screen to my wireframe
- Explained concepts and syntax when I asked, and suggested alternative features when I was looking for ideas

## How AI assistance influenced my solution

The ai helped in the debugging and initial look of the app so far, including how the screens are split, how the in-memory store is organized, and how navigation is handled. I used the AI's code as a reference to learn from, and I am working through it so that I can explain every part of it.

## Verification steps

- Ran the app on my own device after each build step and checked that the expected screens and behavior appeared as intended and not broken.
- Tested adding, renaming, and deleting decks, adding and deleting cards, the empty states, and navigation between screens.
- Made commits after completed steps, using the course's commit message format.

## AI interactions

The prompts I used and the responses in summary are:

- Helping to fic the navigation bugs.
- Planning out the step by step of the development i should take to reduce the encounter of errors.
- Vode alternative suggestions and explainations why theyre good.
- Help matching the home screen to my wireframe
