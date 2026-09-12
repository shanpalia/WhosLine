package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.FriendStatusModel
import com.example.ui.theme.ActiveMintDot
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.OfflineGray
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.PureWhiteCard
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMediumText
import com.example.ui.theme.SlateMuted

@Composable
fun FriendDetailDialog(
    friend: FriendStatusModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("friend_detail_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = PureWhiteCard,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SlateMuted
                        )
                    }
                }

                // Large Avatar
                FriendAvatarWithBadge(
                    name = friend.name,
                    profilePicUrl = friend.profilePicUrl,
                    isOnline = friend.isOnline,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Friend Name
                Text(
                    text = friend.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateDarkText
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Platform pill
                PlatformTagPill(
                    platform = friend.platform,
                    connectedVia = friend.connectedViaAccount
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Status Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (friend.isOnline) MintContainer else Color(0xFFF1F5F9)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (friend.isOnline) ActiveMintDot else OfflineGray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (friend.isOnline) "Active Now on ${friend.platform.displayName}" else "Last Seen Recently",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (friend.isOnline) OnMintContainer else SlateMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Open Chat in ${friend.platform.displayName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = SlateMediumText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Profile",
                        fontSize = 13.sp,
                        color = SlateMediumText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
