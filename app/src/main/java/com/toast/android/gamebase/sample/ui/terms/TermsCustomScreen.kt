package com.toast.android.gamebase.sample.ui.terms

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.EmptyListScreen
import com.toast.android.gamebase.sample.ui.components.ErrorScreen
import com.toast.android.gamebase.sample.ui.components.LoadingScreen
import com.toast.android.gamebase.sample.ui.components.RoundButton
import com.toast.android.gamebase.sample.ui.components.SwitchWithLabel
import com.toast.android.gamebase.sample.util.printWithIndent

@Composable
fun TermsCustomScreen(
    activity: Activity = LocalContext.current as Activity,
    viewModel: TermsCustomViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        viewModel.fetchTermsAndUpdateUiState(activity)
    }

    Column(modifier = Modifier.padding(
        dimensionResource(id = R.dimen.setting_screen_column_padding_horizontal))) {

        when(viewModel.uiState.value) {
            TermsUIState.LOADING -> LoadingScreen()
            TermsUIState.NO_TERMS -> EmptyListScreen()
            TermsUIState.ERROR_TERMS -> {
                ErrorScreen(errorString =
                    viewModel.currentException.value?.printWithIndent() ?:
                        stringResource(id = R.string.unknown_error))
            }
            TermsUIState.DEFAULT_STATE -> {
                viewModel.queryTermsResultState.value?.contents?.let {
                    TermsContentLazyColumn(viewModel) } ?: EmptyListScreen()
            }
        }
    }
}

@Composable
fun TermsContentLazyColumn(viewModel: TermsCustomViewModel) {
    val activity = LocalContext.current as Activity
    val queryTermResultValue = viewModel.queryTermsResultState.value ?: return

    LazyColumn(contentPadding = PaddingValues(
        horizontal = dimensionResource(id = R.dimen.setting_screen_terms_lazy_column_content_padding),
        vertical = dimensionResource(id = R.dimen.setting_screen_terms_lazy_column_content_padding))) {
        items(items = queryTermResultValue.contents) { item ->
            val state = viewModel.agreedMap[item.termsContentSeq]?.value ?: false
            val label = item.name ?: ""

            SwitchWithLabel(
                label = label,
                state = state,
                enableSwitch = viewModel.enabledMap[item.termsContentSeq]?.value ?: true,
                event = { newState ->
                    viewModel.onSwitchChanged(activity, item.termsContentSeq, newState)
                }
            )
        }
        item {
            RoundButton(buttonText = stringResource(R.string.button_ok)) {
                viewModel.sendTerms(activity)
            }
        }
    }
}
