/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.settings.idpmap

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toast.android.gamebase.auth.mapping.data.ForcingMappingTicket
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.sample.data.supportedIdpList
import com.toast.android.gamebase.sample.gamebase_manager.addIdpMapping
import com.toast.android.gamebase.sample.gamebase_manager.forceIdpMapping
import com.toast.android.gamebase.sample.gamebase_manager.getAuthMappingList
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.removeIdpMapping
import kotlinx.coroutines.launch

private const val TAG = "IdpMappingViewModel"

enum class IDP_MAPPING_UI_STATE {
    DEFAULT,
    SHOW_REMOVE_MAPPING_DIALOG,
    SHOW_FORCE_MAPPING_DIALOG,
    MAPPING_FAILED,
    REMOVE_MAPPING_FAILED,
    FORCE_MAPPING_FAILED
}

class IdpMappingViewModel: ViewModel() {
    var uiState by mutableStateOf(IDP_MAPPING_UI_STATE.DEFAULT)
    var currentException by mutableStateOf<GamebaseException?>(null)
    var idpMappedMap: SnapshotStateMap<String, Boolean> = mutableStateMapOf()

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
        uiState = IDP_MAPPING_UI_STATE.SHOW_REMOVE_MAPPING_DIALOG
    }

    fun addMapping(activity: Activity, idp: String) {
        addIdpMapping(activity, idp) { exception: GamebaseException? ->
            val isSuccess = isSuccess(exception)
            if (isSuccess) {
                idpMappedMap[idp] = true
            } else if (exception?.code == GamebaseError.AUTH_ADD_MAPPING_ALREADY_MAPPED_TO_OTHER_MEMBER){
                uiState = IDP_MAPPING_UI_STATE.SHOW_FORCE_MAPPING_DIALOG
            } else {
                uiState = IDP_MAPPING_UI_STATE.MAPPING_FAILED
            }
            currentException = exception
        }
    }

    fun removeMapping(activity: Activity, idp: String) {
        viewModelScope.launch {
            removeIdpMapping(activity, idp) { exception: GamebaseException? ->
                val isSuccess = isSuccess(exception)
                if (isSuccess) {
                    idpMappedMap[idp] = false
                } else {
                    uiState = IDP_MAPPING_UI_STATE.REMOVE_MAPPING_FAILED
                }
                currentException = exception
            }
        }
    }

    fun forceMapping(activity: Activity, idp: String, exception: GamebaseException?) {
        if (exception == null) {
            return
        }
        val forcingMappingTicket = ForcingMappingTicket.from(exception);
        forceIdpMapping(activity, forcingMappingTicket) { exception ->
            val isSuccess = isSuccess(exception)
            if (isSuccess) {
                idpMappedMap[idp] = true
            } else {
                uiState = IDP_MAPPING_UI_STATE.FORCE_MAPPING_FAILED
            }
            currentException = exception
        }
    }
}