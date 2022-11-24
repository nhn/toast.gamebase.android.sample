/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.developer.terms

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebasePushTokenInfo
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.queryTerms
import com.toast.android.gamebase.sample.gamebase_manager.queryTokenInfo
import com.toast.android.gamebase.sample.gamebase_manager.registerPush
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import com.toast.android.gamebase.sample.gamebase_manager.updateTerms
import com.toast.android.gamebase.sample.util.printWithIndent
import com.toast.android.gamebase.terms.data.GamebaseQueryTermsResult
import com.toast.android.gamebase.terms.data.GamebaseTermsContent
import com.toast.android.gamebase.terms.data.GamebaseTermsContentDetail

enum class TermsUIState {
    LOADING,
    NO_TERMS,
    ERROR_TERMS,
    DEFAULT_STATE,
}

class TermsCustomViewModel: ViewModel() {
    val uiState: MutableState<TermsUIState> = mutableStateOf(TermsUIState.LOADING)
    val queryTermsResultState: MutableState<GamebaseQueryTermsResult?> = mutableStateOf(null)
    val currentException: MutableState<GamebaseException?> = mutableStateOf(null)

    var agreedMap: Map<Int, MutableState<Boolean>> = mapOf()
    var enabledMap: Map<Int, MutableState<Boolean>> = mapOf()

    fun fetchTermsAndUpdateUiState(activity: Activity) {
        queryTerms(activity) { queryTermsResult, queryTermsException ->
            currentException.value = queryTermsException
            if (!isSuccess(queryTermsException)) {
                uiState.value = TermsUIState.ERROR_TERMS
                return@queryTerms
            }
            queryTermsResultState.value = queryTermsResult
            if (queryTermsResult.contents.isEmpty()) {
                uiState.value = TermsUIState.NO_TERMS
                return@queryTerms
            }
            // 푸시 수신 동의 여부도 Gamebase 서버에 저장되지 않으므로 agreed 값은 항상 false 로 리턴됩니다.
            // 푸시 수신 동의 여부는 Gamebase.Push.queryTokenInfo API 를 통해 조회하시기 바랍니다.
            queryTokenInfo(activity) { pushTokenInfo, gamebaseException ->
                agreedMap = queryTermsResult.contents.associate { it ->
                    val agreed = determineAgreed(
                        it,
                        gamebaseException,
                        pushTokenInfo
                    )
                    it.termsContentSeq to mutableStateOf(agreed)
                }
                enabledMap = queryTermsResult.contents.associate { it ->
                    // 필수 약관은 OFF 불가능
                    it.termsContentSeq to mutableStateOf(!it.required)
                }
                uiState.value = TermsUIState.DEFAULT_STATE
            }
        }
    }

    private fun determineAgreed(
        termsContentDetail: GamebaseTermsContentDetail,
        gamebaseException: GamebaseException?,
        pushTokenInfo: GamebasePushTokenInfo
    ): Boolean {
        // GamebaseTermsContentDetail.getRequired()가 true 인 필수 항목은 Gamebase 서버에 저장되지 않으므로
        // agreed 값은 항상 false 로 리턴됩니다.
        // 필수 항목은 항상 true 로 저장될 수 밖에 없어서 저장하는 의미가 없기 때문입니다.
        return if (termsContentDetail.required)
            true
        else {
            if (isSuccess(gamebaseException)) {
                when (termsContentDetail.agreePush) {
                    "DAY" -> pushTokenInfo.agreement.adAgreement
                    "NIGHT" -> pushTokenInfo.agreement.adAgreementNight
                    else -> termsContentDetail.agreed
                }
            } else {
                termsContentDetail.agreed
            }
        }
    }

    fun onSwitchChanged(key: Int, newState: Boolean) {
        agreedMap[key]?.value = newState
    }

    fun sendTerms(activity: Activity) {
        val termsContentList = agreedMap.map {
            GamebaseTermsContent(it.key, it.value.value)
        }
        updateTerms(activity = activity,
            queryTermsResultState.value?.termsSeq,
            queryTermsResultState.value?.termsVersion,
            termsContentList
        ) { exception ->
            if (!isSuccess(exception)) {
                val failedTitle = (activity as Context).getString(R.string.failed)
                showAlert(activity, failedTitle, exception.printWithIndent())
            }
        }

        var agreeNight = false;
        var agreeDay = false;
        queryTermsResultState.value?.contents?.forEach {
            when (it.agreePush) {
                "ALL" -> {
                    agreeDay = agreedMap[it.termsContentSeq]?.value ?: false
                    agreeNight = agreedMap[it.termsContentSeq]?.value ?: false
                }
                "DAY" -> {
                    agreeDay = agreedMap[it.termsContentSeq]?.value ?: false
                }
                "NIGHT" -> {
                    agreeNight = agreedMap[it.termsContentSeq]?.value ?: false
                }
            }
        }
        val configuration = PushConfiguration.newBuilder()
            .enablePush(agreeDay)
            .enableAdAgreement(agreeDay)
            .enableAdAgreementNight(agreeNight)
            .build()
        registerPush(
            activity,
            configuration
        ) { exception ->
            if (!isSuccess(exception)) {
                val failedTitle = (activity as Context).getString(R.string.failed)
                showAlert(activity, failedTitle, exception.printWithIndent())
            }
        }
    }
}