package com.toast.android.gamebase.sample.ui.developer.contact

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.sample.gamebasemanager.openContact

private const val TAG = "ContactDetailViewModel"
class ContactDetailViewModel: ViewModel() {
    val userName = mutableStateOf("")
    val additionalUrl = mutableStateOf("")
    val additionalParameters = mutableMapOf<String, String>()
    val extraData = mutableMapOf<String, String>()

    // input dialog open status
    val isAdditionalParametersInputDialogOpened = mutableStateOf(false)
    val isExtraDataInputDialogOpened = mutableStateOf(false)

    fun saveAdditionalParameter(key: String, value: String) {
        additionalParameters[key] = value
    }

    fun saveExtraData(key: String, value: String) {
        extraData[key] = value
    }

    fun openContactUrl(activity: Activity) {
        val configuration = ContactConfiguration
            .newBuilder()
            .setUserName(userName.value)
            .setAdditionalURL(additionalUrl.value)
            .setExtraData(extraData.toMap())
            .setAdditionalParameters(additionalParameters.toMap())
            .build()
        openContact(activity, configuration) { exception ->
            Log.e(TAG, exception.toString())
        }
    }
}