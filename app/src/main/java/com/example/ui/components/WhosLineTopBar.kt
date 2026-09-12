package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMuted

@Composable
fun WhosLineTopBar(
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    onAddAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "refresh_rotation"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Title & Subtitle Branding
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "WhosLine",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MintPrimary,
                            letterSpacing = (-0.5).sp,
                            modifier = Modifier.testTag("app_title_text")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Active pulse beacon dot next to title
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MintPrimary)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "By PaliaAPK HUB",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SlateMuted,
                        letterSpacing = 0.2.sp
                    )
                }

                // Action: Refresh status button
                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("refresh_button"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = SlateBorderLight,
                        contentColor = SlateDarkText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sync friends status",
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(rotation)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Action: Add Social Account button
                FilledTonalIconButton(
                    onClick = onAddAccountClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("add_account_button"),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MintContainer,
                        contentColor = OnMintContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "+ Add Social Account",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
