package com.toast.android.gamebase.sample.ui

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseManager
import com.toast.android.gamebase.sample.ui.login.LoginState

@SuppressLint("CustomSplashScreen")
class SplashActivity : GamebaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchingScreen()
        }

        if (Gamebase.isInitialized() && GamebaseManager.isLoggedIn()) {
            // Application relaunched by clicking of notification.
            reloadMainActivity(this)
        } else {
            initialize(activity = this)
        }
    }

    private fun reloadMainActivity(
        activity: Activity
    ) {
        val thread: Thread = object : Thread() {
            override fun run() {
                activity.runOnUiThread {
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra(MainActivity.INTENT_APPLICATION_RELAUNCHED, true)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    activity.finish()
                }
            }
        }
        thread.start()
    }

    private fun returnToTitle(activity: Activity) {
        val userId = Gamebase.getUserID()
        if (userId != null && !userId.isEmpty()) {
            GamebaseManager.logout(activity) { isSuccess, errorMessage ->
                if (!isSuccess) {
                    val msg = errorMessage ?: ""
                    GamebaseManager.showError(activity, "Logout Failed", msg)
                }
            }
        } else {
            initialize(activity)
        }
    }

    private fun initialize(activity: Activity) {
        GamebaseManager.initialize(
            activity,
            { activityParam ->
                val intent = Intent(activityParam, MainActivity::class.java)
                activityParam.startActivity(intent)
                activityParam.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                activityParam.finish()
            },
            { activityParam, title, message ->
                val returnToTitle =
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        returnToTitle(activityParam)
                    }
                Gamebase.Util.showAlert(activityParam, title, message, returnToTitle)
            },
            { activityParam ->
                returnToTitle(activityParam)
            }) { activityParam, updateUrl, message ->
            val requestCode = 123
            val moveToStore =
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    try {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.data = Uri.parse(updateUrl)
                        activityParam.startActivityForResult(marketIntent, requestCode)
                    } catch (ignored: ActivityNotFoundException) {
                    }
                }
            Gamebase.Util.showAlert(
                activityParam,
                "Unregistered Game Version",
                message!!,
                moveToStore
            )
        }
    }
}