package ph.edu.comteq.recollect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardFormScreen(
    card: Flashcard?,                                    // null = adding a new card, otherwise editing this one
    onSave: (title: String, description: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Start with the card's current text when editing, or empty when adding
    var title by remember { mutableStateOf(card?.title ?: "") }
    var description by remember { mutableStateOf(card?.description ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (the answer)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (the clue)") },
            minLines = 4,
            maxLines = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSave(title, description) },
            enabled = title.isNotBlank() && description.isNotBlank(),   // no blank cards
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (card == null) "Add card" else "Save changes")
        }
    }
}