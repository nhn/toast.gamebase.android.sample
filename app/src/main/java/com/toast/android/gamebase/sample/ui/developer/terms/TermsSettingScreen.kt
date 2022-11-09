package com.toast.android.gamebase.sample.ui.developer.terms

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.showAlert
import com.toast.android.gamebase.sample.gamebasemanager.showTermsView
import com.toast.android.gamebase.sample.ui.common.RoundButton
import com.toast.android.gamebase.sample.ui.common.SwitchWithLabel
import com.toast.android.gamebase.sample.util.printWithIndent
import com.toast.android.gamebase.terms.GamebaseTermsConfiguration

@Composable
fun TermsSettingScreen(
    viewModel: TermsSettingViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val activity: Activity = LocalContext.current as Activity

    BackHandler(viewModel.termsPopupShowing.value) {
        // do nothing. just disable back key when terms popup is showing
    }

    Column(modifier = Modifier
        .padding(dimensionResource(id = R.dimen.setting_screen_column_padding_horizontal))
        .verticalScroll(scrollState)) {

        SwitchWithLabel(
            stringId = R.string.developer_terms_configuration_force_show,
            state = viewModel.forceShow,
            enableSwitch = true,
            event = { newState ->
                viewModel.forceShow.value = newState
            }
        )
        SwitchWithLabel(
            stringId = R.string.developer_terms_configuration_fixed_font_size,
            state = viewModel.fixedFontSize,
            enableSwitch = true,
            event = { newState ->
                viewModel.fixedFontSize.value = newState
            }
        )
        RoundButton(stringResource(id = R.string.developer_terms_show_terms_view)) {
            viewModel.showTermsViewAndCallback(
                activity,
                fixedFontSize = viewModel.fixedFontSize.value,
                forceShow = viewModel.forceShow.value
            ) { status ->
                viewModel.termsPopupShowing.value = status
            }
        }
    }
}
