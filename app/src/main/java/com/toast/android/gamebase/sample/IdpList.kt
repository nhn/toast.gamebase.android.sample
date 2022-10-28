package com.toast.android.gamebase.sample

import com.toast.android.gamebase.base.auth.AuthProvider

// Add supported Idp List
val supportedIdpList = listOf(
    AuthProvider.GUEST,
    AuthProvider.GOOGLE,
    AuthProvider.NAVER,
    AuthProvider.APPLEID,
    AuthProvider.FACEBOOK,
    AuthProvider.KAKAOGAME,
    AuthProvider.LINE,
    AuthProvider.TWITTER,
    AuthProvider.WEIBO,
    "payco",
)

fun getIconResourceById(idp: String): Int {
    when(idp){
        AuthProvider.GUEST -> return R.drawable.guest_logo
        AuthProvider.GOOGLE -> return R.drawable.google_logo
        AuthProvider.NAVER -> return R.drawable.naver_logo
        AuthProvider.APPLEID -> return R.drawable.appleid_logo
        AuthProvider.FACEBOOK -> return R.drawable.facebook_logo
        AuthProvider.KAKAOGAME -> return R.drawable.kakaogames_logo
        AuthProvider.LINE -> return R.drawable.line_logo
        AuthProvider.TWITTER -> return R.drawable.twitter_logo
        AuthProvider.WEIBO -> return R.drawable.weibo_logo
        "payco" -> return R.drawable.payco_logo
    }
    return R.drawable.guest_logo
}