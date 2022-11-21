package com.toast.android.gamebase.sample.gamebase_manager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseConfiguration
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.base.purchase.PurchaseProvider
import com.toast.android.gamebase.error.data.UpdateInfo
import com.toast.android.gamebase.launching.data.LaunchingStatus

private enum class GamePlayStatus {
    PLAYABLE, STOP_PLAYING, INITIALIZE_AGAIN
}

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

// Debug mode
// See https://docs.toast.com/en/Game/Gamebase/en/aos-initialization/#debug-mode
// TODO: [Fix me] Change to 'false' when release mode.
private const val DEBUG_MODE = true

// Popup settings
// See https://docs.toast.com/en/Game/Gamebase/en/aos-initialization/#configuration-settings
// TODO: [Fix me] CThis value determines whether to use pop-ups provided by Gamebase SDK.
private const val ENABLE_POPUP = true
private const val ENABLE_LAUNCHING_STATUS_POPUP = true
private const val ENABLE_BAN_POPUP = true

fun initializeGamebase(
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

    val configuration = GamebaseConfiguration
        .newBuilder(
            PROJECT_ID,
            APP_CLIENT_VERSION,
            STORE_CODE
        )
        .enablePopup(ENABLE_POPUP)
        .enableLaunchingStatusPopup(ENABLE_LAUNCHING_STATUS_POPUP)
        .enableBanPopup(ENABLE_BAN_POPUP)
        .build()

    Gamebase.initialize(
        activity, configuration,
        GamebaseDataCallback { launchingInfo, exception ->
            if (Gamebase.isSuccess(exception)) {
                // 게임 진입을 허용할지 여부를 론칭 코드에 따라 판단하십시오.
                val launchingStatusCode = launchingInfo?.status?.code
                val (canPlay, errorLog) = checkIfGameCanPlay(launchingStatusCode)

                if (canPlay == GamePlayStatus.PLAYABLE) {
                    // 게임 플레이를 시작합니다.
                    Log.v(TAG, "Launching Succeeded")
                    onLaunchingSuccess()
                } else {
                    // 게임 불가 사유를 밝히고 게임을 중지합니다.
                    Log.w(TAG, "Launching Failed($launchingStatusCode) : $errorLog")
                    if (canPlay == GamePlayStatus.INITIALIZE_AGAIN) {
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

private fun checkIfGameCanPlay(launchingStatusCode: Int?): Pair<GamePlayStatus, String> {
    var canPlay: GamePlayStatus = GamePlayStatus.PLAYABLE
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
            canPlay = GamePlayStatus.STOP_PLAYING
            errorLog = "You have to update this application."
        }
        LaunchingStatus.BLOCKED_USER -> {
            canPlay = GamePlayStatus.INITIALIZE_AGAIN
            errorLog = "You are blocked user!"
        }
        LaunchingStatus.TERMINATED_SERVICE -> {
            canPlay = GamePlayStatus.INITIALIZE_AGAIN
            errorLog = "Game is closed!"
        }
        // enablePopup과 enableLaunchingStatusPopup 값이 모두 true인 경우, 게임이 점검 상태라면 자동으로 점검 팝업 창이 표시됩니다.
        LaunchingStatus.INSPECTING_SERVICE,
        LaunchingStatus.INSPECTING_ALL_SERVICES -> {
            canPlay = GamePlayStatus.INITIALIZE_AGAIN
            errorLog = "Under maintenance."
        }
        LaunchingStatus.INTERNAL_SERVER_ERROR -> {
            canPlay = GamePlayStatus.INITIALIZE_AGAIN
            errorLog = "Unknown internal error."
        }
        else -> {
            canPlay = GamePlayStatus.INITIALIZE_AGAIN
            errorLog = "Unknown internal error."
        }
    }
    return (canPlay to errorLog)
}
