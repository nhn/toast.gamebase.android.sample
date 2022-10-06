package com.nhn.android.gamebase.sample.ui

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.nhn.android.gamebase.sample.GamebaseActivity
import com.nhn.android.gamebase.sample.GamebaseManager
import com.toast.android.gamebase.Gamebase

@SuppressLint("CustomSplashScreen")
class SplashActivity : GamebaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchingScreen()
        }

        if (Gamebase.isInitialized() && GamebaseManager.isLoggedIn()) {
            // Application relaunched by clicking of notification.
            LoadMainActivity(this, true)
        } else {
            GamebaseManager.initialize(this)
        }
    }

    companion object {
        fun LoadMainActivity(activity: Activity) {
            LoadMainActivity(activity, false)
        }

        fun LoadMainActivity(
            activity: Activity,
            isRelaunched: Boolean
        ) {
            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        sleep(3000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    activity.runOnUiThread { //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra(MainActivity.INTENT_APPLICATION_RELAUNCHED, isRelaunched)
                        activity.startActivity(intent)
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        activity.finish()
                    }
                }
            }
            thread.start()
        }
    }

}