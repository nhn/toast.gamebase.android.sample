package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.auth.data.BanInfo
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.auth.AuthProviderCredentialConstants
import com.toast.android.gamebase.sample.util.printBanInfo
import com.toast.android.gamebase.sample.util.printLoginError
import com.toast.android.gamebase.sample.util.printLoginSuccess
import com.toast.android.gamebase.sample.util.printLoginWithIdpSuccess

fun lastProviderLogin(activity: Activity, onLoginFinished: () -> Unit) {
    val lastLoggedInProvider = Gamebase.getLastLoggedInProvider()
    Log.d(TAG, "Last Logged in Provider : $lastLoggedInProvider")

    Gamebase.loginForLastLoggedInProvider(
        activity
    ) { result, exception ->
        if (Gamebase.isSuccess(exception)) {
            Log.d(TAG, "Login with Last Logged In Provider Success")
            Log.i(TAG, "LastLoggedInProvider : " + Gamebase.getLastLoggedInProvider())
            handleLoginSuccess(activity, result, onLoginFinished)
        } else {
            handleLastProviderLoginFailed(activity, exception, onLoginFinished)
        }
    }
}

private fun handleLastProviderLoginFailed(
    activity: Activity,
    exception: GamebaseException,
    onLoginFinished: () -> Unit
) {
    val hasGamebaseAccessToken = Gamebase.getAccessToken() != null
    val lastLoggedInProvider = Gamebase.getLastLoggedInProvider()

    if (isNetworkError(exception)) {
        Gamebase.Util.showAlert(activity, "Network Error", "Check your network.")
        retryWithInterval(Runnable { lastProviderLogin(activity, onLoginFinished) }, 2000L)
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
                // TODO: Line login with dialog
                additionalInfo[AuthProviderCredentialConstants.LINE_CHANNEL_REGION] =
                    "japan"
            }
            loginWithIdP(activity, lastLoggedInProvider, additionalInfo, onLoginFinished)
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
            handleLoginSuccess(activity, result, onLoginSuccess)
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
        Gamebase.Util.showAlert(activity, "Login Failed!", exception.toJsonString())
    }
}

private fun handleLoginSuccess(
    activity: Activity,
    authToken: AuthToken,
    onLoginSuccess: () -> Unit
) {
    printLoginSuccess(TAG, authToken)

    //TODO: Initialize Gamebase Analytics

    onLoginSuccess.invoke()
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

// Get Profile Data

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
