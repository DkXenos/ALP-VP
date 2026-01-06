package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkBG3 = Color(0xFF0E0E0E)
private val PureWhite3 = Color(0xFFFFFFFF)
private val PrimaryGreen3 = Color(0xFF00C155)
private val SoftGray3 = Color(0xFF9E9E9E)

@Suppress("unused")
@Composable
fun SubmissionScreen(
    bountyId: String,
    onDone: () -> Unit
) {
    SubmissionContent(onDone = onDone)
}

@Composable
fun SubmissionContent(
    onDone: () -> Unit
) {
    var link by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBG3)
            .padding(20.dp)
    ) {
        Text(text = "Submit Your Work", color = PureWhite3, fontSize = 24.sp)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = link,
            onValueChange = { link = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Project link", color = SoftGray3) },
            textStyle = TextStyle(color = PureWhite3)
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen3)
        ) {
            Text("Mark as Done", color = PureWhite3)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSubmission() {
    SubmissionContent(onDone = {})
}
