package com.aragang.chipiolo.SignInChipiolo

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CreateNameDialog(onNameEntered: (String) -> Unit, onDialogDismissed: () -> Unit) {
    val name = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDialogDismissed,
        title = { Text(text = "Chipi Name") },
        text = {
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(text = "Name") }
            )
        },
        confirmButton = {
            Button(onClick = {
                onNameEntered(name.value)
                onDialogDismissed()
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDialogDismissed) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview
@Composable
fun CreateNameDialogPreview() {
    CreateNameDialog(onNameEntered = {}, onDialogDismissed = {})
}
