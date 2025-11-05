package com.jason.alp_vp.ui.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TestView(){
    Text("Hello World")
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TestViewPreview() {
    TestView()
}