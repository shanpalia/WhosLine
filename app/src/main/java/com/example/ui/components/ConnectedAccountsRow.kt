package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.model.SocialAccount
import com.example.model.SocialPlatform
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.InstagramGradientBrush
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMediumText
import com.example.ui.theme.SlateMuted

@Composable
fun ConnectedAccountsRow(
    accounts: List<SocialAccount>,
    selectedAccountId: String?,
    onSelectAccount: (String?) -> Unit,
    onAddAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Connected Accounts",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SlateMuted,
                letterSpacing = 0.5.sp
            )

            // Optional "Show All" toggle when filtered
            if (selectedAccountId != null) {
                Text(
                    text = "Show All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MintPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onSelectAccount(null) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("connected_accounts_row"),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "All" chip
            item(key = "all_accounts") {
                val isAllSelected = selectedAccountId == null
                AccountFilterChip(
                    label = "All Feeds",
                    badge = "${accounts.size}",
                    isSelected = isAllSelected,
                    onClick = { onSelectAccount(null) },
                    platform = null
                )
            }

            // Account chips
            items(accounts, key = { it.id }) { account ->
                val isSelected = selectedAccountId == account.id
                AccountFilterChip(
                    label = "${account.username} [${account.platform.shortCode}]",
                    badge = account.platform.shortCode,
                    isSelected = isSelected,
                    onClick = { onSelectAccount(account.id) },
                    platform = account.platform
                )
            }

            // Outlined "+ Add Account" button at end of row
            item(key = "add_account_btn") {
                OutlinedButton(
                    onClick = onAddAccountClick,
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("add_account_outlined_button"),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, MintPrimary.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MintPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Account",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun AccountFilterChip(
    label: String,
    badge: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    platform: SocialPlatform?,
    modifier: Modifier = Modifier
) {
    val chipBorder = if (isSelected) {
        BorderStroke(1.5.dp, MintPrimary)
    } else {
        BorderStroke(1.dp, SlateBorder)
    }

    val chipBg = if (isSelected) {
        Color.White
    } else {
        Color.White.copy(alpha = 0.85f)
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("account_chip_${label.replace(" ", "_")}"),
        shape = RoundedCornerShape(20.dp),
        color = chipBg,
        border = chipBorder,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Platform badge icon / color dot
            when (platform) {
                SocialPlatform.INSTAGRAM -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(brush = InstagramGradientBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "IG",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                SocialPlatform.FACEBOOK -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(FacebookBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "f",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                null -> {
                    // All feeds indicator
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MintPrimary else SlateMuted.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✦",
                            color = if (isSelected) Color.White else SlateMediumText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) SlateDarkText else SlateMediumText
            )

            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MintPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
