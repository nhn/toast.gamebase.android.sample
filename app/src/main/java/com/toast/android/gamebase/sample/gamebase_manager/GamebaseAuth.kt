package com.toast.android.gamebase.sample.gamebase_manager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.auth.data.BanInfo
import com.toast.android.gamebase.auth.data.TemporaryWithdrawalInfo
import com.toast.android.gamebase.auth.mapping.data.ForcingMappingTicket
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.sample.data.UserData
import com.toast.android.gamebase.sample.data.dummyUserData
import com.toast.android.gamebase.sample.util.printBanInfo
import com.toast.android.gamebase.sample.util.printLoginError
import com.toast.android.gamebase.sample.util.printLoginSuccess
import com.toast.android.gamebase.sample.util.printLoginWithIdpSuccess
import com.toast.android.gamebase.sample.util.printWithIndent

// Gamebase Auth
// https://docs.toast.com/en/Game/Gamebase/en/aos-authentication/

fun lastProviderLogin(
    activity: Activity,
    onLastProviderIsLine: () -> Unit,
    onLoginFinished: () -> Unit
) {
    val lastLoggedInProvider = Gamebase.getLastLoggedInProvider()
    Log.d(TAG, "Last Logged in Provider : $lastLoggedInProvider")

    Gamebase.loginForLastLoggedInProvider(
        activity
    ) { result, exception ->
        if (Gamebase.isSuccess(exception)) {
            Log.d(TAG, "Login with Last Logged In Provider Success")
            Log.i(TAG, "LastLoggedInProvider : " + Gamebase.getLastLoggedInProvider())
            handleLoginSuccess(result, onLoginFinished)
        } else {
            handleLastProviderLoginFailed(activity, exception, onLastProviderIsLine, onLoginFinished)
        }
    }
}

private fun handleLastProviderLoginFailed(
    activity: Activity,
    exception: GamebaseException,
    onLastProviderIsLine: () -> Unit,
    onLoginFinished: () -> Unit
) {
    val hasGamebaseAccessToken = Gamebase.getAccessToken() != null
    val lastLoggedInProvider = Gamebase.getLastLoggedInProvider()

    if (isNetworkError(exception)) {
        Gamebase.Util.showAlert(activity, "Network Error", "Check your network.")
        retryWithInterval(Runnable { lastProviderLogin(activity, onLoginFinished, onLastProviderIsLine) }, 2000L)
    } else if (isBannedUser(exception)) {
        // Do nothing because you set the 'enableBanPopup' true.
        // Gamebase will show ban-popup automatically.
        // Obtaining Ban Information
        val banInfo = BanInfo.from(exception)
        printBanInfo(TAG, banInfo)
    } else {
        if (hasGamebaseAccessToken && lastLoggedInProvider != null) {
            val additionalInfo: MutableMap<String, Any?> = mutableMapOf()
            if (lastLoggedInProvider == AuthProvider.LINE) {
                onLastProviderIsLine()
            } else {
                loginWithIdP(activity, lastLoggedInProvider, additionalInfo, onLoginFinished)
            }
        } else {
            // Do nothing. User should select IDP from Game UI.
        }
    }
}

private fun retryWithInterval(runnable: Runnable, intervalMillis: Long) {
    Thread {
        try {
            Thread.sleep(intervalMillis)
            runnable.run()
        } catch (e: InterruptedException) {
        }
    }.start()
}

fun isLoggedIn(): Boolean {
    val userId = Gamebase.getUserID()
    return !(userId == null || userId.equals("", ignoreCase = true))
}

// https://docs.toast.com/en/Game/Gamebase/en/aos-authentication/#login-with-idp
fun loginWithIdP(
    activity: Activity,
    provider: String,
    additionalInfo: Map<String, Any?>?,
    onLoginSuccess: () -> Unit
) {
    Log.d(TAG, "Logger with provider : $provider")
    Gamebase.login(activity, provider, additionalInfo) { result, exception ->
        if (Gamebase.isSuccess(exception)) {
            printLoginWithIdpSuccess(TAG, provider)
            handleLoginSuccess(result, onLoginSuccess)
        } else {
            handleIdpLoginFailed(
                activity,
                exception,
                provider,
                additionalInfo,
                onLoginSuccess
            )
        }
    }
}

private fun handleIdpLoginFailed(
    activity: Activity,
    exception: GamebaseException,
    provider: String,
    additionalInfo: Map<String, Any?>?,
    onLoginSuccess: () -> Unit
) {
    if (isNetworkError(exception)) {
        Gamebase.Util.showAlert(activity, "Network Error", "Check your network.")
        retryWithInterval(Runnable {
            loginWithIdP(activity, provider, additionalInfo, onLoginSuccess)
        }, 2000)
    } else if (isBannedUser(exception)) {
        // Do nothing because you set the 'enableBanPopup' true.
        // Gamebase will show ban-popup automatically.
        printBanInfo(TAG, BanInfo.from(exception))
    } else {
        printLoginError(TAG, exception)
        Gamebase.Util.showAlert(activity, "Login Failed!", exception.printWithIndent())
    }
}

private fun handleLoginSuccess(
    authToken: AuthToken,
    onLoginSuccess: () -> Unit
) {
    printLoginSuccess(TAG, authToken)

    // Gamebase Analytics에서 지원하는 모든 API는 로그인 후에 호출이 가능합니다.
    if (useAnalyticsTransmissionFeature) {
        initializeGamebaseAnalytics(dummyUserData)
    }

    onLoginSuccess.invoke()
}

private fun initializeGamebaseAnalytics(userData: UserData) {
    setGameUserData(
        userData.level,
        userData.channelId,
        userData.characterId,
        userData.classId
    )
}

fun logout(
    activity: Activity,
    onLogoutFinished: (isSuccess: Boolean, errorMessage: String?) -> Unit
) {
    Gamebase.logout(activity) { exception ->
        onLogoutFinished(Gamebase.isSuccess(exception), exception?.toJsonString())
    }
}

fun withdraw(
    activity: Activity,
    onWithdrawFinished: (isSuccess: Boolean, errorMessage: String?) -> Unit
) {
    Gamebase.withdraw(activity) { exception ->
        onWithdrawFinished(Gamebase.isSuccess(exception), exception?.toJsonString())
    }
}

// https://docs.toast.com/en/Game/Gamebase/en/aos-authentication/#mapping
fun addIdpMapping(
    activity: Activity,
    mappingProvider: String,
    onMappingFinished: ((GamebaseException?) -> Unit)?
) {
    Gamebase.addMapping(activity, mappingProvider) { authToken, exception ->
        // 매핑 추가 성공
        if (Gamebase.isSuccess(exception)) {
            Log.d(TAG, "Add Mapping successful");
            var userId = authToken.userId;
            onMappingFinished?.invoke(exception)
            return@addMapping;
        }

        // 매핑 추가 실패
        if (exception.code == GamebaseError.SOCKET_ERROR ||
            exception.code == GamebaseError.SOCKET_RESPONSE_TIMEOUT ||
            exception.code == GamebaseError.SOCKET_UNKNOWN_ERROR) {
            // Socket error 로 일시적인 네트워크 접속 불가 상태임을 의미합니다.
            // 네트워크 상태를 확인하거나 잠시 대기 후 재시도 하세요.
            Thread {
                try {
                    Thread.sleep(2000);
                    addIdpMapping(activity, mappingProvider, onMappingFinished);
                } catch (e: InterruptedException) {
                    onMappingFinished?.invoke(exception)
                }
            }.start();
        } else if (exception.code == GamebaseError.AUTH_ADD_MAPPING_ALREADY_MAPPED_TO_OTHER_MEMBER) {
            // Mapping을 시도하는 IdP 계정이 이미 다른 계정에 연동되어 있습니다.
            // 강제로 연동을 해제하기 위해서는 해당 계정의 탈퇴나 Mapping 해제를 하거나, 다음과 같이
            // ForcingMappingTicket을 획득 후, addMappingForcibly() 메소드를 이용하여 강제 매핑을 시도합니다.
            Log.e(TAG, "Add Mapping failed- ALREADY_MAPPED_TO_OTHER_MEMBER");
        } else if (exception.getCode() == GamebaseError.AUTH_ADD_MAPPING_ALREADY_HAS_SAME_IDP) {
            // Mapping을 시도하는 IdP의 계정이 이미 추가되어 있습니다.
            // Gamebase Mapping은 한 IdP당 하나의 계정만 연동 가능합니다.
            // IdP 계정을 변경하려면 이미 연동중인 계정은 Mapping 해제를 해야 합니다.
            Log.e(TAG, "Add Mapping failed- ALREADY_HAS_SAME_IDP");
        } else {
            // 매핑 추가 실패
            Log.e(TAG, "Add Mapping failed- ${exception.toJsonString()}");
        }
        onMappingFinished?.invoke(exception)
    }
}

fun removeIdpMapping(
    activity: Activity,
    providerName: String,
    onRemovedMapping: ((GamebaseException?) -> Unit)?
) {
    Gamebase.removeMapping(activity, providerName) { exception: GamebaseException? ->
        if (Gamebase.isSuccess(exception)) {
            // 매핑 해제 성공
            Log.d(TAG, "Remove mapping successful")
            onRemovedMapping?.invoke(exception)
            return@removeMapping
        }
        if (exception?.code == GamebaseError.SOCKET_ERROR ||
            exception?.code == GamebaseError.SOCKET_RESPONSE_TIMEOUT
        ) {
            // Socket error 로 일시적인 네트워크 접속 불가 상태임을 의미합니다.
            // 네트워크 상태를 확인하거나 잠시 대기 후 재시도 하세요.
            Thread {
                try {
                    Thread.sleep(2000)
                    removeIdpMapping(activity, providerName, onRemovedMapping)
                } catch (e: InterruptedException) { }
            }.start()
        } else if (exception?.code == GamebaseError.AUTH_REMOVE_MAPPING_LOGGED_IN_IDP) {
            // 로그인중인 계정으로는 Mapping 해제를 할 수 없습니다.
            // 다른 계정으로 로그인 하여 Mapping 해제 하거나 탈퇴하여야 합니다.
            Log.e(TAG, "Remove Mapping failed- LOGGED_IN_IDP")
        } else {
            // 매핑 해제 실패
            Log.e(
                TAG, "Remove mapping failed- " +
                    "errorCode: ${ exception?.code} errorMessage: ${exception?.message}")
        }
        onRemovedMapping?.invoke(exception)
    }
}

fun forceIdpMapping(
    activity: Activity,
    forcingMappingTicket: ForcingMappingTicket,
    onForceMapping: ((GamebaseException?) -> Unit)?
) {
    Gamebase.addMappingForcibly(activity, forcingMappingTicket) {
            authToken, exception ->
        onForceMapping?.invoke(exception)
    }
}

// https://docs.toast.com/en/Game/Gamebase/en/aos-authentication/#temporarywithdrawal
fun requestWithdrawal(
    activity: Activity,
    callback: GamebaseDataCallback<TemporaryWithdrawalInfo>?
) {
    Gamebase.TemporaryWithdrawal.requestWithdrawal(
        activity
    ) { temporaryWithdrawalInfo, exception ->
        callback?.onCallback(
            temporaryWithdrawalInfo,
            exception
        )
    }
}

fun cancelWithdrawal(activity: Activity, callback: GamebaseCallback?) {
    Gamebase.TemporaryWithdrawal.cancelWithdrawal(
        activity
    ) { exception -> callback?.onCallback(exception) }
}

// Get Profile Data
// https://docs.toast.com/en/Game/Gamebase/en/aos-authentication/#gamebase-users-information
fun getUserID(): String {
    return Gamebase.getUserID()
}
fun getAccessToken(): String {
    return Gamebase.getAccessToken()
}
fun getLastLoggedInProvider(): String {
    return Gamebase.getLastLoggedInProvider() ?: ""
}
fun getAuthMappingList(): List<String> {
    return Gamebase.getAuthMappingList()
}
