package com.toast.android.gamebase.sample.ui.developer

import android.content.Context

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
    return getString(resources.getIdentifier(name, defType, packageName))
}