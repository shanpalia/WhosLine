package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.FriendStatusModel
import com.example.model.SocialPlatform
import com.example.ui.theme.ActiveMintDot
import com.example.ui.theme.FacebookBg
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.InstagramBg
import com.example.ui.theme.InstagramGradientBrush
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.OfflineGray
import com.example.ui.theme.PureWhiteCard
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMediumText
import com.example.ui.theme.SlateMuted

@Composable
fun FriendStatusCard(
    friend: FriendStatusModel,
    onFriendClick: (FriendStatusModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onFriendClick(friend) }
            .testTag("friend_card_${friend.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, SlateBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Friend Profile Picture Avatar with Active Mint Green Dot at bottom-right
            FriendAvatarWithBadge(
                name = friend.name,
                profilePicUrl = friend.profilePicUrl,
                isOnline = friend.isOnline
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Friend Details (Name, Platform tag, Status)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name
                Text(
                    text = friend.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateDarkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("friend_name_${friend.id}")
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Platform Tag (e.g. "Instagram • via @palia_dev")
                PlatformTagPill(
                    platform = friend.platform,
                    connectedVia = friend.connectedViaAccount
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Status Text: "Active Now" (Mint Green) or "Offline" (Gray)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (friend.isOnline) ActiveMintDot else OfflineGray)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (friend.isOnline) "Active Now" else "Offline",
                        fontSize = 12.sp,
                        fontWeight = if (friend.isOnline) FontWeight.Bold else FontWeight.Medium,
                        color = if (friend.isOnline) MintPrimary else OfflineGray,
                        modifier = Modifier.testTag("friend_status_${friend.id}")
                    )
                }
            }

            // Quick interaction button (Chat/Message)
            IconButton(
                onClick = { onFriendClick(friend) },
                modifier = Modifier
                    .size(38.dp)
                    .testTag("friend_action_${friend.id}"),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (friend.isOnline) MintContainer.copy(alpha = 0.6f) else SlateBorderLight,
                    contentColor = if (friend.isOnline) MintPrimary else SlateMuted
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Message ${friend.name}",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun FriendAvatarWithBadge(
    name: String,
    profilePicUrl: String,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    var imageLoadFailed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.size(52.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Main Avatar Circle
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))),
            contentAlignment = Alignment.Center
        ) {
            if (profilePicUrl.isNotBlank() && !imageLoadFailed) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profilePicUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    onError = { imageLoadFailed = true }
                )
            } else {
                // Fallback Initials
                val initials = name.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                    .uppercase()

                Text(
                    text = initials,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateDarkText
                )
            }
        }

        // Active indicator badge at bottom-right corner
        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(2.dp)
                .clip(CircleShape)
                .background(if (isOnline) ActiveMintDot else OfflineGray)
                .border(1.dp, Color.White, CircleShape)
                .testTag(if (isOnline) "active_dot_online" else "active_dot_offline")
        )
    }
}

@Composable
fun PlatformTagPill(
    platform: SocialPlatform,
    connectedVia: String,
    modifier: Modifier = Modifier
) {
    val pillBg = when (platform) {
        SocialPlatform.INSTAGRAM -> InstagramBg
        SocialPlatform.FACEBOOK -> FacebookBg
    }
    val pillBorder = when (platform) {
        SocialPlatform.INSTAGRAM -> Color(0xFFF9A8D4).copy(alpha = 0.5f)
        SocialPlatform.FACEBOOK -> Color(0xFFBFDBFE).copy(alpha = 0.5f)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp), // 20dp chip style
        color = pillBg,
        border = BorderStroke(0.8.dp, pillBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Platform mini badge dot
            when (platform) {
                SocialPlatform.INSTAGRAM -> {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(InstagramGradientBrush)
                    )
                }
                SocialPlatform.FACEBOOK -> {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(FacebookBlue)
                    )
                }
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "${platform.displayName} • via $connectedVia",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = SlateMediumText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
