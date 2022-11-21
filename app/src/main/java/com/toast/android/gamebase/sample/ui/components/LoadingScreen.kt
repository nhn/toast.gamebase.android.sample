package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.toast.android.gamebase.sample.R

@Composable
fun LoadingScreen() {
    Surface(color = MaterialTheme.colors.surface) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.loading))
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}