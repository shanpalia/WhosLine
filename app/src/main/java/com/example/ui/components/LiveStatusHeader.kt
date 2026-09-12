package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMediumText
import com.example.ui.theme.SlateMuted

@Composable
fun LiveStatusHeader(
    onlineCount: Int,
    showOnlineOnly: Boolean,
    onToggleOnlineOnly: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Title + Total Count Pill Badge
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Active Friends",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateDarkText,
                    letterSpacing = (-0.3).sp,
                    modifier = Modifier.testTag("live_active_friends_title")
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Total count pill badge (e.g. "8 Online")
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MintContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MintPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.testTag("online_count_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Live pulse beacon dot
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(MintPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$onlineCount Online",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnMintContainer
                        )
                    }
                }
            }

            // Quick actions: Search & Filter toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Search toggle icon
                IconButton(
                    onClick = {
                        isSearchExpanded = !isSearchExpanded
                        if (!isSearchExpanded) onSearchQueryChange("")
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("toggle_search_button")
                ) {
                    Icon(
                        imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Search friends",
                        tint = if (isSearchExpanded) MintPrimary else SlateMediumText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Filter Online Only Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (showOnlineOnly) MintPrimary.copy(alpha = 0.12f) else SlateBorderLight)
                        .clickable(onClick = onToggleOnlineOnly)
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("online_filter_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = null,
                            tint = if (showOnlineOnly) MintPrimary else SlateMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (showOnlineOnly) "Active" else "All",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (showOnlineOnly) MintPrimary else SlateMuted
                        )
                    }
                }
            }
        }

        // Expandable search field
        AnimatedVisibility(
            visible = isSearchExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = SlateMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = SlateDarkText,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("friend_search_input"),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search friends by name or account...",
                                    fontSize = 13.sp,
                                    color = SlateMuted
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = SlateMuted,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onSearchQueryChange("") }
                        )
                    }
                }
            }
        }
    }
}
