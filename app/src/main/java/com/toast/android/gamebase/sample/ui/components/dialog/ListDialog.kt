package com.toast.android.gamebase.sample.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.toast.android.gamebase.base.ValueObject
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.theme.White
import com.toast.android.gamebase.sample.util.printWithIndent

@Composable
fun ListDialog(
    isDialogOpened: Boolean,
    setDialogState: (Boolean) -> Unit,
    data: List<*>,
    onOkButtonClicked: () -> Unit,
) {
    if (isDialogOpened) {
        Dialog(onDismissRequest = {  }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(80f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                color = White
            ) {
                Column (modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(12.dp))
                    if (data.isEmpty()) {
                        Text("No Item")
                    }
                    LazyColumn(modifier = Modifier.heightIn(max = 500.dp)) {
                        items(items = data) { item ->
                            val str = if (item is ValueObject) {
                                item.printWithIndent()
                            } else {
                                item.toString()
                            }
                            Text(str)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        TextButton(
                            onClick = {
                                onOkButtonClicked()
                                setDialogState(false)
                            }
                        ) {
                            Text(stringResource(id = R.string.button_ok))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ListDialogComposable() {
    ListDialog(
        true,
        { },
        listOf("123", "2345"),
        { })
}

@Preview
@Composable
fun LongListDialogComposable() {
    val names: List<String> = List(1000) { "$it" }
    ListDialog(
        true,
        { },
        names,
        { })
}


@Preview
@Composable
fun EmptyListDialogComposable() {
    val emptyList: List<String> = emptyList()
    ListDialog(
        true,
        { },
        emptyList,
        { })
}