package com.toast.android.gamebase.sample.ui.developer.contact

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.sample.gamebase_manager.openContact

private const val TAG = "ContactDetailViewModel"
class ContactDetailViewModel: ViewModel() {
    val userName = mutableStateOf("")
    val additionalUrl = mutableStateOf("")

    private val _additionalParameters = mutableStateMapOf<String, String>()
    val additionalParameters: Map<String, String>
        get() = _additionalParameters

    private val _extraData = mutableStateMapOf<String, String>()
    val extraData: Map<String, String>
        get() = _extraData

    // input dialog open status
    val isAdditionalParametersInputDialogOpened = mutableStateOf(false)
    val isExtraDataInputDialogOpened = mutableStateOf(false)

    fun saveAdditionalParameter(key: String, value: String) {
        _additionalParameters[key] = value
    }
    fun removeAdditionalParameter(key: String) {
        _additionalParameters.remove(key)
    }

    fun saveExtraData(key: String, value: String) {
        _extraData[key] = value
    }
    fun removeExtraData(key: String) {
        _extraData.remove(key)
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