package com.toast.android.gamebase.sample.ui.developer

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.Log

private const val TAG = "DeveloperMenuFactory"
class DeveloperMenuFactory() {
    fun createMenu(context: Context,
                   categoryId: Int): List<DeveloperMenu> {
        val menuList = context.resources.getStringArray(categoryId).toList()
        return menuList.map { menu ->
            DeveloperMenu(
                category = categoryId,
                id = menu,
                name = context.getStringResourceByName(menu))
        }
    }
}

fun Context.getStringResourceByName(name: String): String {
    val defType = "string"
    return try {
        getString(resources.getIdentifier(name, defType, packageName))
    } catch (exception: NotFoundException) {
        Log.e(TAG, exception.toString())
        ""
    }
}