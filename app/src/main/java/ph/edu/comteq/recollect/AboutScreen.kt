package ph.edu.comteq.recollect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

const val DEVELOPER_NAME = "Neoh Del Prado"

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = "ReCollect logo",
                modifier = Modifier.size(96.dp)
            )
        }
        item {
            Text(
                text = "ReCollect",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Developer",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Text(DEVELOPER_NAME)
        }

        item {
            Text(
                text = "About this project",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Text(
                "ReCollect was created for educational purposes as part of the " +
                        "requirements for Mobile Application Development at COMTEQ " +
                        "Computer and Business College, Academic Year 2026 - 2027."
            )
        }

        item {
            Text(
                text = "AI assistance",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Text(
                "This project was created with the help of AI, which was used to debug " +
                        "and learning reference while building the app."
            )
        }
    }
}