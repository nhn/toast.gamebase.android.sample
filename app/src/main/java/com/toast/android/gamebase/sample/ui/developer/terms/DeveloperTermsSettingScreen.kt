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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
fun DeveloperTermsSettingScreen() {
    val scrollState = rememberScrollState()
    val activity: Activity = LocalContext.current as Activity
    var termsPopupShowing by remember {
        mutableStateOf<Boolean>(false)
    }
    var forceShow by remember {
        mutableStateOf<Boolean>(false)
    }
    var fixedFontSize by remember {
        mutableStateOf<Boolean>(false)
    }

    BackHandler(termsPopupShowing) {
        // do nothing. just disable back key when terms popup is showing
    }

    Column(modifier = Modifier
        .padding(20.dp)
        .verticalScroll(scrollState)) {

        SwitchWithLabel(
            stringId = R.string.developer_terms_configuration_force_show,
            state = forceShow,
            enableSwitch = true,
            event = { newState ->
                forceShow = newState
            }
        )
        SwitchWithLabel(
            stringId = R.string.developer_terms_configuration_fixed_font_size,
            state = fixedFontSize,
            enableSwitch = true,
            event = { newState ->
                fixedFontSize = newState
            }
        )
        RoundButton(stringResource(id = R.string.developer_terms_show_terms_view)) {
            showTermsViewAndCallback(
                activity,
                fixedFontSize = fixedFontSize,
                forceShow = forceShow
            ) { status ->
                termsPopupShowing = status
            }
        }
    }
}

private fun showTermsViewAndCallback(
    activity: Activity,
    forceShow: Boolean,
    fixedFontSize: Boolean,
    updateTermsPopupStatus: (Boolean) -> Unit
) {
    val termsConfiguration = GamebaseTermsConfiguration.newBuilder()
        .setForceShow(forceShow)
        .enableFixedFontSize(fixedFontSize)
        .build()

    val failedTitle: String = GamebaseApplication.instance.applicationContext.getString(R.string.failed)
    val successTitle: String = GamebaseApplication.instance.applicationContext.getString(R.string.success)

    updateTermsPopupStatus(true)
    showTermsView(
        activity = activity,
        configuration = termsConfiguration) { dataContainer, exception ->

        if (isSuccess(exception)) {
            if (dataContainer != null) {
                showAlert(activity, successTitle, dataContainer.printWithIndent())
            } else {
                showAlert(activity, successTitle, "Empty Gamebase data container")
            }
        } else {
            showAlert(activity, failedTitle, exception.printWithIndent())
        }
        updateTermsPopupStatus(false)
    }
}
