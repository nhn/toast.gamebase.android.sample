package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.ui.theme.Grey300
import com.toast.android.gamebase.sample.ui.theme.Purple700
import com.toast.android.gamebase.sample.ui.theme.Toast
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun ToggleButton(
    state: Boolean,
    text1: String,
    text2: String,
    textColor: Color = Toast,
    bgColor: Color = White,
    shape: Shape = RoundedCornerShape(50),
    onToggle: () -> Unit,
) {
    if (!state) {
        OutlinedButton(
            onClick = { onToggle() },
            colors =
            ButtonDefaults.buttonColors(
                backgroundColor = bgColor,
                contentColor = textColor
            ),
            border = BorderStroke(1.dp, textColor),
            shape = shape
        ) {
            Text(text = text1)
        }
    } else {
        OutlinedButton(
            onClick = { onToggle() },
            colors =
            ButtonDefaults.buttonColors(
                backgroundColor = textColor,
                contentColor = bgColor
            ),
            shape = shape
        ) {
            Text(text = text2)
        }
    }
}

@Preview
@Composable
fun ButtonPreview() {
    var state by remember {
        mutableStateOf(true)
    }
    ToggleButton(state = state , text1 = "연동하기", text2 = "연동됨", textColor = Purple700, bgColor = Grey300) {
        state = !state
    }
}