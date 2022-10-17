package com.toast.android.gamebase.sample.ui

import android.R
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : GamebaseActivity() {
    private val requestCode = 123
    private val mActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchingScreen()
        }
        if (Gamebase.isInitialized() && GamebaseManager.isLoggedIn()) {
            // Application relaunched by clicking of notification.
            loadMainActivity()
        } else {
            initialize()
        }
    }

    private fun loadMainActivity() {
        val thread: Thread = object : Thread() {
            override fun run() {
                mActivity.runOnUiThread {
                    val intent = Intent(mActivity, MainActivity::class.java)
                    intent.putExtra(MainActivity.INTENT_APPLICATION_RELAUNCHED, true)
                    mActivity.startActivity(intent)
                    mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    mActivity.finish()
                }
            }
        }
        thread.start()
    }

    private fun initialize() {
        GamebaseManager.initialize(
            activity = mActivity,
            onSuccess = {
                moveToMainActivity()
            },
            showErrorAndReturnToSplash = { title, message ->
                showErrorAndReturnToSplash(title, message)
            },
            showUnregisteredVersionAndMoveToStore = { updateUrl, message ->
                showUnregisteredVersionAndMoveToStore(updateUrl, message)
            }
        )
    }

    private fun showErrorAndReturnToSplash(title: String?, message: String?) {
        if(title.isNullOrEmpty() || message.isNullOrEmpty()) {
            returnToSplash()
        } else {
            val returnToTitle =
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    returnToSplash()
                }
            Gamebase.Util.showAlert(mActivity, title, message, returnToTitle)
        }
    }

    private fun returnToSplash() {
        val userId = Gamebase.getUserID()
        if (!userId.isNullOrEmpty()) {
            GamebaseManager.logout(mActivity) { isSuccess, errorMessage ->
                if (!isSuccess) {
                    val msg = errorMessage ?: ""
                    GamebaseManager.showError(mActivity, "Logout Failed", msg)
                }
            }
        } else {
            val intent = Intent(mActivity, SplashActivity::class.java)
            mActivity.apply {
                startActivity(intent)
                finish()
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(mActivity, MainActivity::class.java)
        mActivity.apply {
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    private fun showUnregisteredVersionAndMoveToStore(updateUrl: String, message: String) {
        val moveToStore =
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                try {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse(updateUrl)
                    mActivity.startActivityForResult(marketIntent, requestCode)
                } catch (ignored: ActivityNotFoundException) {
                }
            }
        Gamebase.Util.showAlert(
            mActivity,
            "Unregistered Game Version",
            message,
            moveToStore
        )
    }
}