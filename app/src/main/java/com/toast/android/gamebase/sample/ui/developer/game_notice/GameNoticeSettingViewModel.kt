package com.toast.android.gamebase.sample.ui.developer.game_notice

import android.app.Activity
import android.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.gamenotice.GameNoticeConfiguration
import com.toast.android.gamebase.sample.gamebase_manager.openGameNotices
import androidx.core.graphics.toColorInt

class GameNoticeSettingViewModel : ViewModel() {
    var gameNoticeBackgroundDialogState = mutableStateOf(false)
    var gameNoticeBackgroundColor = mutableStateOf("#80000000")

    fun openUserSettingGameNotice(activity: Activity) {
        val configuration = GameNoticeConfiguration.newBuilder()
            .setBackgroundColor(gameNoticeBackgroundColor.value.toColorInt())
            .build()

        openGameNotices(activity = activity, configuration = configuration)
    }
}