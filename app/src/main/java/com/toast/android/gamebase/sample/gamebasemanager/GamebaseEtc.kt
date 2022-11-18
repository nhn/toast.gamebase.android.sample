package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.content.DialogInterface
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.analytics.data.GameUserData
import com.toast.android.gamebase.analytics.data.LevelUpData
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.event.GamebaseEventHandler

const val TAG = "GamebaseManager"
private var mGamebaseEventHandler: GamebaseEventHandler? = null


// https://docs.toast.com/ko/Game/Gamebase/ko/aos-etc/
// Additional features

// ENABLE/DISABLE TOAST ANALYTICS
// TODO: [FIX ME] Toast analytics 기능을 사용하려면, 해당 value를 true로 수정하면 login 이후에 setGameUserData 를 호출합니다.
const val useAnalyticsTransmissionFeature = false

fun getDeviceLanguage(): String {
    return Gamebase.getDeviceLanguageCode()
}

fun getDisplayLanguage(): String {
    return Gamebase.getDisplayLanguageCode()
}

fun getCountryCodeOfUSIM(): String {
    return Gamebase.getCountryCodeOfUSIM()
}

fun getCountryCodeOfDevice(): String {
    return Gamebase.getCountryCodeOfDevice()
}

/*
* USIM, 단말기 언어 설정의 순서로 국가 코드를 확인하여 리턴합니다.
* getCountryCode API는 다음 순서로 동작합니다.
* USIM에 기록된 국가 코드를 확인해 보고 값이 존재한다면 추가적인 체크 없이 그대로 리턴합니다.
* USIM 국가 코드가 빈 값이라면 단말기 국가 코드를 확인해 보고 값이 존재한다면 추가적인 체크 없이 그대로 리턴합니다.
* USIM, 단말기 국가 코드가 모두 빈 값이라면 'ZZ'를 리턴합니다.
* */
fun getIntegratedCountryCode(): String {
    return Gamebase.getCountryCode()
}

// Analytics
fun setGameUserData(userLevel: Int, channelId: String?, characterId: String?, classId: String?) {
    val gameUserData: GameUserData = GameUserData(userLevel).apply {
        this.channelId = channelId
        this.characterId = characterId
        this.classId = classId
    }

    Gamebase.Analytics.setGameUserData(gameUserData)
}

fun onLevelUp(userLevel: Int, levelUpTime: Long) {
    val levelUpData: LevelUpData = LevelUpData(userLevel, levelUpTime)

    Gamebase.Analytics.traceLevelUp(levelUpData)
}

// Contact
fun getContactUrl(
    configuration: ContactConfiguration? = null,
    onClosedCallback: ((String, GamebaseException?) -> Unit)
) {
    if (configuration == null) {
        Gamebase.Contact.requestContactURL(onClosedCallback)
    } else {
        Gamebase.Contact.requestContactURL(configuration, onClosedCallback)
    }
}

fun openContact(
    activity: Activity,
    configuration: ContactConfiguration?,
    onClosedCallback: ((GamebaseException?) -> Unit)
) {
    if (configuration != null) {
        Gamebase.Contact.openContact(activity, configuration, onClosedCallback)
    } else {
        Gamebase.Contact.openContact(activity, onClosedCallback)
    }
}

// Common
fun isSuccess(exception: GamebaseException?): Boolean =
    Gamebase.isSuccess(exception)

internal fun isNetworkError(exception: GamebaseException): Boolean {
    val errorCode = exception.code
    return errorCode == GamebaseError.SOCKET_ERROR ||
            errorCode == GamebaseError.SOCKET_RESPONSE_TIMEOUT
}

internal fun isBannedUser(exception: GamebaseException): Boolean {
    return exception.code == GamebaseError.BANNED_MEMBER
}
