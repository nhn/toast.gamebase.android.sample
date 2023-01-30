package com.toast.android.gamebase.sample.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun copyClipBoard(context: Context, string: String) {
    val appContext = context.applicationContext
    val clipboardManager = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", string)
    clipboardManager.setPrimaryClip(clipData)
    val data = clipboardManager.primaryClip
    data?.let {
        showToastMessage(context, "Copy Clipboard : " + it.getItemAt(0).text.toString())
    }
}

fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}