package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.SocialPlatform
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.InstagramGradientBrush
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.PureWhiteCard
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMediumText
import com.example.ui.theme.SlateMuted

@Composable
fun AddAccountDialog(
    onDismiss: () -> Unit,
    onAccountAdded: (username: String, platform: SocialPlatform) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPlatform by remember { mutableStateOf(SocialPlatform.INSTAGRAM) }
    var handleInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("add_account_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = PureWhiteCard,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Connect Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateDarkText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Sync active friends from your profile",
                            fontSize = 12.sp,
                            color = SlateMuted
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close dialog",
                            tint = SlateMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Platform Selector (Instagram vs Facebook)
                Text(
                    text = "Select Platform",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SlateDarkText
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Instagram option
                    PlatformOptionCard(
                        title = "Instagram",
                        code = "IG",
                        isSelected = selectedPlatform == SocialPlatform.INSTAGRAM,
                        onClick = { selectedPlatform = SocialPlatform.INSTAGRAM },
                        gradientBrush = InstagramGradientBrush,
                        solidColor = null,
                        modifier = Modifier.weight(1f)
                    )

                    // Facebook option
                    PlatformOptionCard(
                        title = "Facebook",
                        code = "FB",
                        isSelected = selectedPlatform == SocialPlatform.FACEBOOK,
                        onClick = { selectedPlatform = SocialPlatform.FACEBOOK },
                        gradientBrush = null,
                        solidColor = FacebookBlue,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Handle / Username Input Field
                Text(
                    text = if (selectedPlatform == SocialPlatform.INSTAGRAM) "Instagram Username / Handle" else "Facebook Account Name",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SlateDarkText
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = handleInput,
                    onValueChange = {
                        handleInput = it
                        if (errorMessage != null) errorMessage = null
                    },
                    placeholder = {
                        Text(
                            text = if (selectedPlatform == SocialPlatform.INSTAGRAM) "@your_handle" else "e.g., Palia Gaming",
                            fontSize = 14.sp,
                            color = SlateMuted
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("account_handle_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintPrimary,
                        unfocusedBorderColor = SlateBorder,
                        cursorColor = MintPrimary
                    ),
                    singleLine = true,
                    isError = errorMessage != null
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = errorMessage ?: "",
                        fontSize = 11.sp,
                        color = Color(0xFFEF4444)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = SlateMuted)
                    ) {
                        Text(text = "Cancel", fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val trimmed = handleInput.trim()
                            if (trimmed.isEmpty()) {
                                errorMessage = "Please enter an account username"
                            } else {
                                onAccountAdded(trimmed, selectedPlatform)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("submit_add_account_button")
                    ) {
                        Text(
                            text = "Connect Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlatformOptionCard(
    title: String,
    code: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    gradientBrush: androidx.compose.ui.graphics.Brush?,
    solidColor: Color?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("select_platform_$code"),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MintPrimary else SlateBorder
        ),
        color = if (isSelected) MintPrimary.copy(alpha = 0.05f) else Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .then(
                        if (gradientBrush != null) {
                            Modifier.background(gradientBrush)
                        } else {
                            Modifier.background(solidColor ?: Color.Gray)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = code,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) SlateDarkText else SlateMediumText
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(MintPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
