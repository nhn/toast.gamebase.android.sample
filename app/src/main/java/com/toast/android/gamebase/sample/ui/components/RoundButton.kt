package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.toast.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme

@Composable
fun RoundButton(buttonText: String, onClickListener: () -> Unit) {
    Button(
        onClick = onClickListener,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.surface)
    ) {
        Text(text = buttonText, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun PreviewToastButton() {
    GamebaseSampleProjectTheme {
        RoundButton("이미지 공지") {}
    }
}