package com.toast.android.gamebase.sample

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.GamebaseConfiguration
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.auth.data.BanInfo
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.auth.AuthProviderCredentialConstants
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.base.purchase.PurchaseProvider
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebaseNotificationOptions
import com.toast.android.gamebase.base.push.data.GamebasePushTokenInfo
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.error.data.UpdateInfo
import com.toast.android.gamebase.event.GamebaseEventCategory
import com.toast.android.gamebase.event.GamebaseEventHandler
import com.toast.android.gamebase.event.data.GamebaseEventLoggedOutData
import com.toast.android.gamebase.event.data.GamebaseEventMessage
import com.toast.android.gamebase.event.data.PushAction
import com.toast.android.gamebase.event.data.PushMessage
import com.toast.android.gamebase.launching.data.LaunchingStatus
import com.toast.android.gamebase.sample.util.*
import org.json.JSONObject

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

        // GamebaseEventHandler
        private var mGamebaseEventHandler: GamebaseEventHandler? = null

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Common
        //
        ////////////////////////////////////////////////////////////////////////////////
        fun isSuccess(exception: GamebaseException?): Boolean =
            Gamebase.isSuccess(exception)


        fun showAlert(activity: Activity, title: String, message: String) =
            Gamebase.Util.showAlert(activity, title, message)


        fun showToast(activity: Activity, message: String, duration: Int) =
            Gamebase.Util.showToast(activity, message, duration)

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Initialization
        //
        ////////////////////////////////////////////////////////////////////////////////
        fun initialize(
            activity: Activity,
            onLaunchingSuccess: () -> Unit,
            showErrorAndRetryInitialize: (String?, String?) -> Unit,
            showUnregisteredVersionAndMoveToStore: (String, String) -> Unit
        ) {
            /**
             * Show gamebase debug message.
             * Set 'false' when build RELEASE.
             */
            Gamebase.setDebugMode(DEBUG_MODE)

            val configuration =
                GamebaseConfiguration.newBuilder(PROJECT_ID, APP_CLIENT_VERSION, STORE_CODE)
                    .enablePopup(ENABLE_POPUP)
                    .enableLaunchingStatusPopup(ENABLE_LAUNCHING_STATUS_POPUP)
                    .enableBanPopup(ENABLE_BAN_POPUP)
                    .build()

            Gamebase.initialize(
                activity, configuration,
                GamebaseDataCallback { launchingInfo, exception ->
                    val launchingStatusCode = launchingInfo.status.code
                    if (Gamebase.isSuccess(exception)) {
                        // 게임 진입을 허용할지 여부를 론칭 코드에 따라 판단하십시오.
                        val (canPlay, errorLog) = checkIfGameCanPlay(launchingStatusCode)

                        if (canPlay == GAME_PLAY_STATUS.PLAYABLE) {
                            // 게임 플레이를 시작합니다.
                            Log.v(TAG, "Launching Succeeded")
                            onLaunchingSuccess()
                        } else {
                            // 게임 불가 사유를 밝히고 게임을 중지합니다.
                            Log.w(TAG, "Launching Failed($launchingStatusCode) : $errorLog")
                            if (canPlay == GAME_PLAY_STATUS.INITIALIZE_AGAIN) {
                                if (!ENABLE_POPUP || !ENABLE_LAUNCHING_STATUS_POPUP) {
                                    showErrorAndRetryInitialize("Launching Failed", errorLog)
                                } else {
                                    showErrorAndRetryInitialize(null, null)
                                }
                            }
                        }
                    } else {
                        // 초기화에 실패하면 Gamebase SDK를 이용할 수 없습니다.
                        // 오류를 표시하고 게임을 재시작 또는 종료해야 합니다.
                        Log.w(TAG, "Launching Exception : " + exception.toJsonString())

                        val updateInfo = UpdateInfo.from(exception)
                        if (updateInfo != null) {
                            if (!ENABLE_POPUP || !ENABLE_LAUNCHING_STATUS_POPUP) {
                                // 등록되지 않은 game client version 입니다.
                                // 사용자가 마켓으로 이동할 수 있도록 게임에서 직접 UI를 구현할 수 있습니다
                                showUnregisteredVersionAndMoveToStore(
                                    updateInfo.installUrl,  // Market URL
                                    updateInfo.message      // Message from launching server
                                )
                            }
                            return@GamebaseDataCallback
                        }
                        showErrorAndRetryInitialize("Launching Exception", exception.toJsonString())
                    }
                })
        }

        private fun checkIfGameCanPlay(launchingStatusCode: Int): Pair<GAME_PLAY_STATUS, String> {
            var canPlay: GAME_PLAY_STATUS = GAME_PLAY_STATUS.PLAYABLE
            var errorLog = ""

            // 론칭 상태를 확인합니다.
            when (launchingStatusCode) {
                LaunchingStatus.IN_SERVICE -> {}
                LaunchingStatus.RECOMMEND_UPDATE ->
                    Log.d(TAG, "There is a new version of this application.")
                LaunchingStatus.IN_SERVICE_BY_QA_WHITE_LIST,
                LaunchingStatus.IN_TEST,
                LaunchingStatus.IN_REVIEW,
                LaunchingStatus.IN_BETA ->
                    Log.d(TAG, "You logged in because you are developer.")
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
                // enablePopup과 enableLaunchingStatusPopup 값이 모두 true인 경우, 게임이 점검 상태라면 자동으로 점검 팝업 창이 표시됩니다.
                LaunchingStatus.INSPECTING_SERVICE,
                LaunchingStatus.INSPECTING_ALL_SERVICES -> {
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
            return (canPlay to errorLog)
        }

        // TODO: 동작 테스트 필요
        ////////////////////////////////////////////////////////////////////////////////
        //
        // GamebaseEventHandler
        //
        ////////////////////////////////////////////////////////////////////////////////
        fun addGamebaseEventHandler(activity: Activity) {
            if (mGamebaseEventHandler != null) {
                Gamebase.removeEventHandler(mGamebaseEventHandler)
            }
            mGamebaseEventHandler =
                GamebaseEventHandler { message: GamebaseEventMessage ->
                    when (message.category) {
                        GamebaseEventCategory.LOGGED_OUT -> {
                            onLoggedOut(activity, message)
                        }
                        GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT_MESSAGE_RECEIVED,
                        GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT,
                        GamebaseEventCategory.SERVER_PUSH_TRANSFER_KICKOUT -> {
                            //TODO: processServerPush
                        }
                        GamebaseEventCategory.OBSERVER_LAUNCHING,
                        GamebaseEventCategory.OBSERVER_HEARTBEAT,
                        GamebaseEventCategory.OBSERVER_NETWORK -> {
                            //TODO: processObserver
                        }
                        GamebaseEventCategory.PURCHASE_UPDATED -> {
                            PurchasableReceipt.from(message.data)?.let {
                                printWhat(TAG, message.category, it)
                            }
                        }
                        GamebaseEventCategory.PUSH_RECEIVED_MESSAGE -> {
                            onPushReceiveMessage(message)
                        }
                        GamebaseEventCategory.PUSH_CLICK_MESSAGE -> {
                            PushMessage.from(message.data)?.let {
                                printPushClickMessage(TAG, message.category, it)
                            }
                        }
                        GamebaseEventCategory.PUSH_CLICK_ACTION -> {
                            PushAction.from(message.data)?.let {
                                printPushAction(TAG, message.category, it)
                            }
                        }
                        else -> {}
                    }
                }
            Gamebase.addEventHandler(mGamebaseEventHandler)
        }

        private fun onLoggedOut(activity: Activity, message: GamebaseEventMessage) {
            val loggedOutData = GamebaseEventLoggedOutData.from(message.data)
            if (loggedOutData != null) {
                Log.i(TAG, "[GamebaseEventHandler] category : " + message.category)
                Log.i(TAG, "[GamebaseEventHandler] loggedOutData : $loggedOutData")
                Log.i(TAG, "[GamebaseEventHandler] Process Login again.")
                Log.d(TAG, "--------------------------------------")
                // There was a problem with the access token.
                // Call login again.
                Gamebase.login(
                    activity, Gamebase.getLastLoggedInProvider()
                ) { _: AuthToken?, _: GamebaseException? -> }
            }
        }

        private fun onPushReceiveMessage(message: GamebaseEventMessage) {
            val pushMessage = PushMessage.from(message.data)
            if (pushMessage != null) {
                // When you received push message.
                Log.i(TAG, "[GamebaseEventHandler] category : " + message.category)
                Log.i(TAG, "[GamebaseEventHandler] PushMessage : $pushMessage")
                Log.d(TAG, "--------------------------------------")
                try {
                    val json = JSONObject(pushMessage.extras)
                    // There is 'isForeground' information.
                    val isForeground = json.getBoolean("isForeground")
                    // You can get your custom key.
                    if (json.has("YourCustomKey")) {
                        val customValue = json["YourCustomKey"]
                    }
                } catch (ignored: Exception) {
                }
            }
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

        private fun isNetworkError(exception: GamebaseException): Boolean {
            val errorCode = exception.code
            return errorCode == GamebaseError.SOCKET_ERROR ||
                    errorCode == GamebaseError.SOCKET_RESPONSE_TIMEOUT
        }

        private fun isBannedUser(exception: GamebaseException): Boolean {
            return exception.code == GamebaseError.BANNED_MEMBER
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

        fun showError(activity: Activity, title: String, message: String) {
            Gamebase.Util.showAlert(activity, title, message)
        }

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Get Profile Data
        //
        ////////////////////////////////////////////////////////////////////////////////
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
        ////////////////////////////////////////////////////////////////////////////////
        //
        // Purchase
        //
        ////////////////////////////////////////////////////////////////////////////////

        fun requestNotConsumedItems(
            activity: Activity,
            callback: GamebaseDataCallback<List<PurchasableReceipt>>?
        ) {
            Gamebase.Purchase.requestItemListOfNotConsumed(
                activity
            ) { purchasableReceipts, exception ->
                if (Gamebase.isSuccess(exception)) {
                    // Succeeded.
                } else {
                    // Failed.
                    Log.w(TAG, "Request purchases failed : $exception")
                }
                callback?.onCallback(purchasableReceipts, exception)
            }
        }

        fun requestItemList(
            activity: Activity,
            callback: GamebaseDataCallback<List<PurchasableItem>>?
        ) {
            Gamebase.Purchase.requestItemListPurchasable(
                activity
            ) { purchasableItems, exception ->
                if (Gamebase.isSuccess(exception)) {
                    // Succeeded.
                } else {
                    // Failed.
                    Log.w(TAG, "Request item list failed : $exception")
                }
                callback?.onCallback(purchasableItems, exception)
            }
        }

        fun requestPurchase(
            activity: Activity,
            gamebaseProductId: String,
            callback: GamebaseDataCallback<PurchasableReceipt>?
        ) {
            Gamebase.Purchase.requestPurchase(
                activity, gamebaseProductId
            ) { data, exception ->
                if (Gamebase.isSuccess(exception)) {
                    // Succeeded.
                } else if (exception.code == GamebaseError.PURCHASE_USER_CANCELED) {
                    // User canceled.
                } else {
                    // To Purchase Item Failed cause of the error
                }
                callback?.onCallback(data, exception)
            }
        }

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Push
        //
        ////////////////////////////////////////////////////////////////////////////////
        fun registerPush(
            activity: Activity,
            configuration: PushConfiguration,
            callback: GamebaseCallback?
        ) {
            Gamebase.Push.registerPush(
                activity,
                configuration
            ) { exception: GamebaseException? ->
                if (Gamebase.isSuccess(exception)) {
                    Log.v(TAG, "Register push succeeded")
                } else {
                    Log.e(TAG, "Register push failed : $exception")
                }
                callback?.onCallback(exception)
            }
        }

        fun registerPush(
            activity: Activity,
            configuration: PushConfiguration,
            notificationOptions: GamebaseNotificationOptions,
            callback: GamebaseCallback?
        ) {
            Gamebase.Push.registerPush(activity, configuration, notificationOptions) { exception ->
                if (Gamebase.isSuccess(exception)) {
                    Log.v(TAG, "Register push succeeded")
                } else {
                    Log.e(TAG, "Register push failed : $exception")
                }
                callback?.onCallback(exception)
            }
        }

        fun getNotificationOptions(activity: Activity): GamebaseNotificationOptions =
            Gamebase.Push.getNotificationOptions(activity)

        fun queryTokenInfo(
            activity: Activity,
            callback: GamebaseDataCallback<GamebasePushTokenInfo>?
        ) {
            Gamebase.Push.queryTokenInfo(
                activity
            ) { tokenInfo, exception ->
                if (Gamebase.isSuccess(exception)) {
                    printQueryTokenInfo(TAG, tokenInfo)
                } else {
                    Log.e(TAG, "QueryTokenInfo failed : $exception")
                }
                callback?.onCallback(tokenInfo, exception)
            }
        }

        ////////////////////////////////////////////////////////////////////////////////
        //
        // Contact
        //
        ////////////////////////////////////////////////////////////////////////////////
        fun openContact(
            activity: Activity,
            userName: String?,
            callback: (() -> Unit)?
        ) {
            val builder = ContactConfiguration.newBuilder()
            if (userName != null) {
                builder.setUserName(userName)
            }
            Gamebase.Contact.openContact(
                activity, builder.build()
            ) {
                if (callback != null) {
                    callback()
                }
            }
        }
    }
}