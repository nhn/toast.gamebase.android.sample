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

private const val INITIALIZE_RETRY_MAX_COUNT = 2
private const val MARKET_INTENT_REQUEST_CODE = 123

@SuppressLint("CustomSplashScreen")
class SplashActivity : GamebaseActivity() {
    private val mActivity = this
    private var reInitializeCount = 0

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
        val intent = Intent(mActivity, MainActivity::class.java)
        intent.putExtra(MainActivity.INTENT_APPLICATION_RELAUNCHED, true)
        mActivity.let {
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    private fun initialize() {
        GamebaseManager.initialize(
            activity = mActivity,
            onSuccess = {
                moveToMainActivity()
            },
            showErrorAndRetryInitialize = { title, message ->
                showErrorAndRetryInitialize(title, message)
            },
            showUnregisteredVersionAndMoveToStore = { updateUrl, message ->
                showUnregisteredVersionAndMoveToStore(updateUrl, message)
            }
        )
    }

    private fun showErrorAndRetryInitialize(title: String?, message: String?) {
        if (title.isNullOrEmpty() || message.isNullOrEmpty()) {
            logoutOrRetryInitialize()
        } else {
            val returnToTitle =
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    logoutOrRetryInitialize()
                }
            Gamebase.Util.showAlert(mActivity, title, message, returnToTitle)
        }
    }

    private fun logoutOrRetryInitialize() {
        val userId = Gamebase.getUserID()
        if (!userId.isNullOrEmpty()) {
            GamebaseManager.logout(mActivity) { isSuccess, errorMessage ->
                if (!isSuccess) {
                    val msg = errorMessage ?: ""
                    GamebaseManager.showError(mActivity, "Logout Failed", msg)
                }
            }
        } else {
            retryInitialize()
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(mActivity, MainActivity::class.java)
        mActivity.let {
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
                    mActivity.startActivityForResult(marketIntent, MARKET_INTENT_REQUEST_CODE)
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

    private fun retryInitialize() {
        if (reInitializeCount < INITIALIZE_RETRY_MAX_COUNT) {
            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            ++reInitializeCount
            initialize()
        }
    }
}