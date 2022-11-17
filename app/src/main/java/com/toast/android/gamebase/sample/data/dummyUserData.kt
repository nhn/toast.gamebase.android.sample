package com.toast.android.gamebase.sample.data

// TODO: [Fix me] Value for the analytics data of the game.
data class UserData(
    val level: Int,
    val channelId: String,
    val characterId: String,
    val classId: String
)

val dummyUserData = UserData(
    level = 10,
    channelId = "channel sample",
    characterId = "character name",
    classId = "character class"
)