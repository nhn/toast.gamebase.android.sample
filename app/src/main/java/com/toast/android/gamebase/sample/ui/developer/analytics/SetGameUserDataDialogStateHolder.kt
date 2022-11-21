package com.toast.android.gamebase.sample.ui.developer.analytics

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import com.toast.android.gamebase.sample.gamebase_manager.setGameUserData
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import java.lang.NumberFormatException

private const val TAG = "GameUserDataDialog"

class SetGameUserDataDialogStateHolder() {
    val levelInput = mutableStateOf("")
    val channelId = mutableStateOf("")
    val characterId = mutableStateOf("")
    val classId = mutableStateOf("")

    fun setGameUserDataInDialog(
        activity: Activity
    ) {
        try {
            setGameUserData(
                levelInput.value.toInt(),
                channelId.value,
                characterId.value,
                classId.value
            )
        } catch (exception: NumberFormatException) {
            showAlert(activity, TAG, "level needs Int type : $exception")
        }
    }
}