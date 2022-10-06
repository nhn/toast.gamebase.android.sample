package com.nhn.android.gamebase.sample

import android.app.Activity
import android.content.*
import android.net.Uri
import com.nhn.android.gamebase.sample.ui.SplashActivity
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.push.PushConfiguration

class AppFlowHelper {
    companion object {
        private const val PREFERENCE_NAME = "gamebase.sample.application"
        private const val PREF_KEY_PUSH_CONFIGURATION = "gamebase.sample.pref.push.configuration"

        fun savePushConfiguration(
            context: Context,
            pushConfiguration: PushConfiguration?
        ) {
            if (pushConfiguration == null) {
                return
            }
            val pref =
                context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(
                PREF_KEY_PUSH_CONFIGURATION,
                pushConfiguration.toJsonString()
            )
            editor.apply()
        }

        fun showErrorAndReturnToTitle(activity: Activity, title: String, message: String) {
            val returnToTitle =
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    returnToTitle(
                        activity
                    )
                }
            Gamebase.Util.showAlert(activity, title, message, returnToTitle)
        }


        fun returnToTitle(activity: Activity) {
            val userId = Gamebase.getUserID()
            if (userId != null && !userId.isEmpty()) {
                //로그아웃 기능 구현 후 진행
                //GamebaseManager.logout(activity)
            } else {
                GamebaseManager.initialize(activity)
            }
        }

        fun moveToLoginScene(activity: Activity) {
            SplashActivity.LoadMainActivity(activity)
        }

        fun showUnregisteredVersionAndMoveToStore(
            activity: Activity,
            updateUrl: String?,
            message: String?
        ) {
            val requestCode = 123
            val moveToStore =
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    try {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.data = Uri.parse(updateUrl)
                        activity.startActivityForResult(marketIntent, requestCode)
                    } catch (ignored: ActivityNotFoundException) {
                    }
                }
            Gamebase.Util.showAlert(activity, "Unregistered Game Version", message!!, moveToStore)
        }

    }
}