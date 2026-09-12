package com.example.model

enum class SocialPlatform(val displayName: String, val shortCode: String) {
    INSTAGRAM("Instagram", "IG"),
    FACEBOOK("Facebook", "FB")
}

data class SocialAccount(
    val id: String,
    val username: String,
    val platform: SocialPlatform,
    val displayName: String = username,
    val isSelected: Boolean = true
)

data class FriendStatusModel(
    val id: String,
    val name: String,
    val profilePicUrl: String,
    val isOnline: Boolean,
    val platform: SocialPlatform,
    val connectedViaAccount: String,
    val lastActive: String = if (isOnline) "Active Now" else "Offline"
)
