package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputComponent(state: DatePickerState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DatePicker(title = {
            Text(
                text = "",
            )
        },
            headline = {
                Text(
                    text = "Data",
                    modifier = Modifier.padding(16.dp)
                )
            }, state = state, modifier = Modifier.padding(16.dp)
        )
    }
}