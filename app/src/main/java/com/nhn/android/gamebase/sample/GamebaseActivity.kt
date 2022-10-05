package com.nhn.android.gamebase.sample

import android.content.Intent
import androidx.activity.ComponentActivity

open class GamebaseActivity : ComponentActivity() {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}