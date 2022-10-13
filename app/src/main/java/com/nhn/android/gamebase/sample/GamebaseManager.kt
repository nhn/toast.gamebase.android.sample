package com.nhn.android.gamebase.sample

import android.app.Activity
import android.util.Log
import com.nhn.android.gamebase.sample.util.printBanInfo
import com.nhn.android.gamebase.sample.util.printLoginError
import com.nhn.android.gamebase.sample.util.printLoginSuccess
import com.nhn.android.gamebase.sample.util.printLoginWithIdpSuccess
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseConfiguration
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.auth.data.BanInfo
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.purchase.PurchaseProvider
import com.toast.android.gamebase.error.data.UpdateInfo
import com.toast.android.gamebase.launching.data.LaunchingStatus

class GamebaseManager {

    companion object {
        private const val TAG = "GamebaseManager"

        // Initialization settings
        // See https://docs.toast.com/en/Game/Gamebase/en/aos-initialization/#initialize
        // NHN Cloud Project ID
        // See https://docs.toast.com/en/TOAST/en/console-guide/#create-projects
        // TODO: [Fix me] Project ID issued from NHN Cloud Project.
        private const val PROJECT_ID = "6ypq5kwa"

        // See https://docs.toast.com/en/Game/Gamebase/en/oper-app/#client-list
        // TODO: [Fix me] See app client version from Gamebase Console.
        private const val APP_CLIENT_VERSION = "1.0.0"

        // TODO: [Fix me] Change your store code for initialization.
        private const val STORE_CODE = PurchaseProvider.StoreCode.GOOGLE

        // Popup settings
        // See https://docs.toast.com/en/Game/Gamebase/en/aos-initialization/#configuration-settings
        // TODO: [Fix me] CThis value determines whether to use pop-ups provided by Gamebase SDK.
        private const val ENABLE_POPUP = true
        private const val ENABLE_LAUNCHING_STATUS_POPUP = true
        private const val ENABLE_BAN_POPUP = true

        // Debug mode
        // See https://docs.toast.com/en/Game/Gamebase/en/aos-initialization/#debug-mode
        // TODO: [Fix me] Change to 'false' when release mode.
        private const val DEBUG_MODE = true

        private enum class GAME_PLAY_STATUS {
            PLAYABLE, STOP_PLAYING, INITIALIZE_AGAIN
        }

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Initialization
        //
        ////////////////////////////////////////////////////////////////////////////////
        fun initialize(activity: Activity) {
            Gamebase.setDebugMode(DEBUG_MODE)
            val configuration =
                GamebaseConfiguration.newBuilder(PROJECT_ID, APP_CLIENT_VERSION, STORE_CODE)
                    .enablePopup(ENABLE_POPUP)
                    .enableLaunchingStatusPopup(ENABLE_LAUNCHING_STATUS_POPUP)
                    .enableBanPopup(ENABLE_BAN_POPUP)
                    .build()

            Gamebase.initialize(activity, configuration,
                GamebaseDataCallback { launchingInfo, exception ->
                    if (Gamebase.isSuccess(exception)) {
                        var canPlay: GAME_PLAY_STATUS = GAME_PLAY_STATUS.PLAYABLE
                        var errorLog = ""
                        val launchingStatusCode = launchingInfo.status.code
                        when (launchingStatusCode) {
                            LaunchingStatus.IN_SERVICE -> {}
                            LaunchingStatus.RECOMMEND_UPDATE -> Log.d(
                                TAG,
                                "There is a new version of this application."
                            )
                            LaunchingStatus.IN_SERVICE_BY_QA_WHITE_LIST, LaunchingStatus.IN_TEST, LaunchingStatus.IN_REVIEW, LaunchingStatus.IN_BETA -> Log.d(
                                TAG,
                                "You logged in because you are developer."
                            )
                            LaunchingStatus.REQUIRE_UPDATE -> {
                                canPlay = GAME_PLAY_STATUS.STOP_PLAYING
                                errorLog = "You have to update this application."
                            }
                            LaunchingStatus.BLOCKED_USER -> {
                                canPlay = GAME_PLAY_STATUS.INITIALIZE_AGAIN
                                errorLog = "You are blocked user!"
                            }
                            LaunchingStatus.TERMINATED_SERVICE -> {
                                canPlay = GAME_PLAY_STATUS.INITIALIZE_AGAIN
                                errorLog = "Game is closed!"
                            }
                            LaunchingStatus.INSPECTING_SERVICE, LaunchingStatus.INSPECTING_ALL_SERVICES -> {
                                canPlay = GAME_PLAY_STATUS.INITIALIZE_AGAIN
                                errorLog = "Under maintenance."
                            }
                            LaunchingStatus.INTERNAL_SERVER_ERROR -> {
                                canPlay = GAME_PLAY_STATUS.INITIALIZE_AGAIN
                                errorLog = "Unknown internal error."
                            }
                            else -> {
                                canPlay = GAME_PLAY_STATUS.INITIALIZE_AGAIN
                                errorLog = "Unknown internal error."
                            }
                        }
                        if (canPlay == GAME_PLAY_STATUS.PLAYABLE) {
                            Log.v(TAG, "Launching Succeeded")
                            AppFlowHelper.moveToMainActivity(
                                activity
                            )
                        } else {
                            Log.w(
                                TAG,
                                "Launching Failed($launchingStatusCode) : $errorLog"
                            )
                            if (canPlay == GAME_PLAY_STATUS.INITIALIZE_AGAIN) {
                                if (!ENABLE_POPUP || !ENABLE_LAUNCHING_STATUS_POPUP) {
                                    AppFlowHelper.showErrorAndReturnToTitle(
                                        activity,
                                        "Launching Failed",
                                        errorLog
                                    )
                                } else {
                                    AppFlowHelper.returnToTitle(activity)
                                }
                            }
                        }
                    } else {
                        val updateInfo = UpdateInfo.from(exception)
                        if (updateInfo != null) {
                            Log.w(
                                TAG,
                                "Launching Exception : " + exception.toJsonString()
                            )
                            if (!ENABLE_POPUP || !ENABLE_LAUNCHING_STATUS_POPUP) {
                                // Initialized with not registered game client version.
                                // Show update popup manually.
                                AppFlowHelper.showUnregisteredVersionAndMoveToStore(
                                    activity,
                                    updateInfo.installUrl,
                                    updateInfo.message
                                )
                            }
                            return@GamebaseDataCallback
                        }
                        Log.w(
                            TAG,
                            "Launching Exception : " + exception.toJsonString()
                        )
                        AppFlowHelper.showErrorAndReturnToTitle(
                            activity,
                            "Launching Exception",
                            exception.toJsonString()
                        )
                    }
                })
        }

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Authentication
        //
        ////////////////////////////////////////////////////////////////////////////////
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
                    val additionalInfo: Map<String, Any?> = HashMap()
                    if (lastLoggedInProvider == AuthProvider.LINE) {
                        // TODO: Line login with dialog
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

        fun loginWithIdP(activity: Activity, provider: String, additionalInfo: Map<String, Any?>?, onLoginSuccess: () -> Unit) {
            Log.d(TAG, "Logger with provider : $provider")
            Gamebase.login(activity, provider, additionalInfo) { result, exception ->
                if (Gamebase.isSuccess(exception)) {
                    printLoginWithIdpSuccess(TAG, provider)
                    handleLoginSuccess(activity, result, onLoginSuccess)
                } else {
                    handleIdpLoginFailed(activity, exception, provider, additionalInfo, onLoginSuccess)
                }
            }
        }

        private fun handleIdpLoginFailed(
            activity: Activity,
            exception: GamebaseException,
            provider: String,
            additionalInfo: Map<String, Any?>?,
            onLoginSuccess:() -> Unit
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

        private fun handleLoginSuccess(activity: Activity, authToken: AuthToken, onLoginSuccess: () -> Unit) {
            printLoginSuccess(TAG, authToken)

            //TODO: addGamebaseEventHandler(activity)
            //TODO: Initialize Gamebase Analytics

            onLoginSuccess.invoke()
        }

        private fun isNetworkError(exception: GamebaseException): Boolean {
            val errorCode = exception.code
            return errorCode == GamebaseError.SOCKET_ERROR ||
                    errorCode == GamebaseError.SOCKET_RESPONSE_TIMEOUT
        }

        private fun isBannedUser(exception: GamebaseException): Boolean {
            return exception.code == GamebaseError.BANNED_MEMBER
        }
    }
}