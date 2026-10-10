package ph.edu.comteq.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ph.edu.comteq.recollect.ui.theme.ReCollectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReCollectTheme {
                ReCollectApp()
            }
        }
    }
}
