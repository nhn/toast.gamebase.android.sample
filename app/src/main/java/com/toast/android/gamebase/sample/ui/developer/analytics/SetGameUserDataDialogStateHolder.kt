package com.toast.android.gamebase.sample.ui.developer.analytics

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import com.toast.android.gamebase.analytics.data.GameUserData
import com.toast.android.gamebase.sample.gamebase_manager.setGameUserData
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import java.lang.NumberFormatException

private const val TAG = "GameUserDataDialog"

class SetGameUserDataDialogStateHolder {
    val levelInput = mutableStateOf("")
    val channelId = mutableStateOf("")
    val characterId = mutableStateOf("")
    val classId = mutableStateOf("")

    fun setGameUserDataInDialog(
        activity: Activity
    ) {
        try {
            val dummyUserData = GameUserData(levelInput.value.toInt())
            dummyUserData.channelId = channelId.value
            dummyUserData.characterId = characterId.value
            dummyUserData.classId = classId.value

            setGameUserData(dummyUserData)
        } catch (exception: NumberFormatException) {
            showAlert(activity, TAG, "level needs Int type : $exception")
        }
    }
}