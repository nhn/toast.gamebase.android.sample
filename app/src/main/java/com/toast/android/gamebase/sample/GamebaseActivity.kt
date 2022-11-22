/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample

import android.content.Intent
import androidx.activity.ComponentActivity
import com.toast.android.gamebase.Gamebase

open class GamebaseActivity : ComponentActivity() {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Gamebase.onActivityResult(requestCode, resultCode, data)
    }
}