package com.toast.android.gamebase.sample.ui.settings.idpmap

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.auth.mapping.data.ForcingMappingTicket
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.auth.AuthProviderCredentialConstants
import com.toast.android.gamebase.sample.data.supportedIdpList
import com.toast.android.gamebase.sample.gamebase_manager.addIdpMapping
import com.toast.android.gamebase.sample.gamebase_manager.forceIdpMapping
import com.toast.android.gamebase.sample.gamebase_manager.getAuthMappingList
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.removeIdpMapping

enum class IdpMappingUiState {
    DEFAULT,
    SHOW_REMOVE_MAPPING_DIALOG,
    SHOW_FORCE_MAPPING_INFO_DIALOG,
    SHOW_FORCE_MAPPING_CONFIRM_DIALOG,
    MAPPING_FAILED,
    REMOVE_MAPPING_FAILED,
    FORCE_MAPPING_FAILED,
    CHANGE_LOGIN_FAILED
}

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class IdpMappingViewModel: ViewModel() {
    var uiState by mutableStateOf(IdpMappingUiState.DEFAULT)
    var currentException by mutableStateOf<GamebaseException?>(null)
    var idpMappedMap: SnapshotStateMap<String, Boolean> = mutableStateMapOf()
    var requiredAdditionalInfo by mutableStateOf(false)
    // line을 통한 idP mapping 시 사용
    var enteredRegion by mutableStateOf("none")

    var forcingMappingTicket: ForcingMappingTicket? = null

    init {
        fetchAuthMappingList()
    }

    fun fetchAuthMappingList() {
        val supportedIdpListWithoutGuest = supportedIdpList.drop(1)
        val mappedIdpList = getAuthMappingList()
        val authMappingMap: SnapshotStateMap<String, Boolean> = mutableStateMapOf()

        supportedIdpListWithoutGuest.map {
            authMappingMap.put(it, mappedIdpList.contains(it))
        }
        idpMappedMap = authMappingMap
    }

    fun showRemoveMappingDialog() {
        uiState = IdpMappingUiState.SHOW_REMOVE_MAPPING_DIALOG
    }

    fun addMapping(activity: Activity, idp: String) {
        val additionalInfo: MutableMap<String, Any> = mutableMapOf()

        if (idp == AuthProvider.LINE) {
            additionalInfo[AuthProviderCredentialConstants.LINE_CHANNEL_REGION] = enteredRegion
        }
        addIdpMapping(activity, idp, additionalInfo) { exception: GamebaseException? ->
            val isSuccess = isSuccess(exception)
            if (isSuccess) {
                idpMappedMap[idp] = true
            } else if (exception?.code == GamebaseError.AUTH_ADD_MAPPING_ALREADY_MAPPED_TO_OTHER_MEMBER) {
                forcingMappingTicket = ForcingMappingTicket.from(exception);
                uiState = IdpMappingUiState.SHOW_FORCE_MAPPING_INFO_DIALOG
            } else {
                uiState = IdpMappingUiState.MAPPING_FAILED
            }
            currentException = exception
        }
    }

    fun removeMapping(activity: Activity, idp: String) {
        removeIdpMapping(activity, idp) { exception: GamebaseException? ->
            val isSuccess = isSuccess(exception)
            if (isSuccess) {
                idpMappedMap[idp] = false
            } else {
                uiState = IdpMappingUiState.REMOVE_MAPPING_FAILED
            }
            currentException = exception
        }
    }

    fun forceMapping(activity: Activity, idp: String) {
        forcingMappingTicket?.let {
            forceIdpMapping(activity, it) { forceMappingException ->
                val isSuccess = isSuccess(forceMappingException)
                if (isSuccess) {
                    idpMappedMap[idp] = true
                } else {
                    uiState = IdpMappingUiState.FORCE_MAPPING_FAILED
                }
                currentException = forceMappingException
            }
        }
    }

    fun changeLogin(activity: Activity) {
        forcingMappingTicket?.let {
            com.toast.android.gamebase.sample.gamebase_manager.changeLogin(activity, it) {
                    authToken, gamebaseException ->
                if (isSuccess(gamebaseException)) {
                    fetchAuthMappingList()
                } else {
                    uiState = IdpMappingUiState.CHANGE_LOGIN_FAILED
                }
                currentException = gamebaseException
            }
        }
    }
}