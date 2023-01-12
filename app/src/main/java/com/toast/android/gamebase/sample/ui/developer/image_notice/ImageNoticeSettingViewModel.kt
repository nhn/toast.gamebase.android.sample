package com.toast.android.gamebase.sample.ui.developer.image_notice

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.imagenotice.ImageNoticeConfiguration
import com.toast.android.gamebase.sample.gamebase_manager.showImageNotices

class ImageNoticeSettingViewModel : ViewModel() {
    var imageNoticeBackgroundDialogStatus = mutableStateOf(false)
    var imageNoticeTimeOutDialogStatus = mutableStateOf(false)
    var imageNoticeBackgroundColor = mutableStateOf("#80000000")
    var imageNoticeTimeOut: MutableState<Long> = mutableStateOf(5000)
    var autoCloseCustomSchemeSwitchStatus = mutableStateOf(false)

    fun showUserSettingImageNotice(activity: Activity) {
        val configuration = ImageNoticeConfiguration.newBuilder()
            .setBackgroundColor(imageNoticeBackgroundColor.value)
            .setTimeout(imageNoticeTimeOut.value)
            .enableAutoCloseByCustomScheme(autoCloseCustomSchemeSwitchStatus.value)
            .build()

        showImageNotices(activity = activity, configuration = configuration)
    }
}