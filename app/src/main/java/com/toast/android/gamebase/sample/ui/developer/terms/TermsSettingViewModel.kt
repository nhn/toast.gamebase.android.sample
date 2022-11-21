package com.toast.android.gamebase.sample.ui.developer.terms

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import com.toast.android.gamebase.sample.gamebase_manager.showTermsView
import com.toast.android.gamebase.sample.util.printWithIndent
import com.toast.android.gamebase.terms.GamebaseTermsConfiguration

private const val TAG = "TermsSettingViewModel"
class TermsSettingViewModel: ViewModel() {
    var termsPopupShowing = mutableStateOf<Boolean>(false)
        private set
    var forceShow = mutableStateOf<Boolean>(false)
        private set
    var fixedFontSize = mutableStateOf<Boolean>(false)
        private set

    fun showTermsViewAndCallback(
        activity: Activity,
        forceShow: Boolean,
        fixedFontSize: Boolean,
        updateTermsPopupStatus: (isShowing: Boolean) -> Unit
    ) {
        val termsConfiguration = GamebaseTermsConfiguration.newBuilder()
            .setForceShow(forceShow)
            .enableFixedFontSize(fixedFontSize)
            .build()

        val failedTitle: String =
            GamebaseApplication.instance.applicationContext.getString(R.string.failed)
        val successTitle: String =
            GamebaseApplication.instance.applicationContext.getString(R.string.success)

        updateTermsPopupStatus(true)
        showTermsView(activity = activity, configuration = termsConfiguration
        ) { dataContainer, exception ->
            if (isSuccess(exception)) {
                if (dataContainer != null) {
                    showAlert(activity, successTitle, dataContainer.printWithIndent())
                } else {
                    Log.e(TAG, "Empty Gamebase data container" )
                }
            } else {
                showAlert(activity, failedTitle, exception.printWithIndent())
            }
            updateTermsPopupStatus(false)
        }
    }
}