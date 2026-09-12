package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.FriendStatusModel
import com.example.model.SocialAccount
import com.example.ui.components.AddAccountDialog
import com.example.ui.components.ConnectedAccountsRow
import com.example.ui.components.FriendDetailDialog
import com.example.ui.components.FriendStatusCard
import com.example.ui.components.LiveStatusHeader
import com.example.ui.components.WhosLineTopBar
import com.example.ui.theme.MintTintBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateDarkText
import com.example.ui.theme.SlateMuted
import com.example.viewmodel.WhosLineViewModel

@Composable
fun WhosLineDashboardScreen(
    viewModel: WhosLineViewModel,
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val selectedAccountId by viewModel.selectedAccountId.collectAsStateWithLifecycle()
    val friends by viewModel.displayedFriends.collectAsStateWithLifecycle()
    val onlineCount by viewModel.onlineCount.collectAsStateWithLifecycle()
    val showOnlineOnly by viewModel.showOnlineOnly.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isAddAccountDialogOpen by viewModel.isAddAccountDialogOpen.collectAsStateWithLifecycle()

    var selectedFriendForDetail by remember { mutableStateOf<FriendStatusModel?>(null) }

    WhosLineDashboardContent(
        accounts = accounts,
        selectedAccountId = selectedAccountId,
        friends = friends,
        onlineCount = onlineCount,
        showOnlineOnly = showOnlineOnly,
        searchQuery = searchQuery,
        isRefreshing = isRefreshing,
        onRefreshClick = { viewModel.refreshStatuses() },
        onSelectAccount = { viewModel.selectAccountFilter(it) },
        onAddAccountClick = { viewModel.openAddAccountDialog() },
        onToggleOnlineOnly = { viewModel.toggleOnlineOnly() },
        onSearchQueryChange = { viewModel.setSearchQuery(it) },
        onFriendClick = { selectedFriendForDetail = it },
        modifier = modifier
    )

    // Add Account Dialog
    if (isAddAccountDialogOpen) {
        AddAccountDialog(
            onDismiss = { viewModel.closeAddAccountDialog() },
            onAccountAdded = { username, platform ->
                viewModel.addSocialAccount(username, platform)
            }
        )
    }

    // Friend Quick Detail Dialog
    selectedFriendForDetail?.let { friend ->
        FriendDetailDialog(
            friend = friend,
            onDismiss = { selectedFriendForDetail = null }
        )
    }
}

@Composable
fun WhosLineDashboardContent(
    accounts: List<SocialAccount>,
    selectedAccountId: String?,
    friends: List<FriendStatusModel>,
    onlineCount: Int,
    showOnlineOnly: Boolean,
    searchQuery: String,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    onSelectAccount: (String?) -> Unit,
    onAddAccountClick: () -> Unit,
    onToggleOnlineOnly: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onFriendClick: (FriendStatusModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("whosline_dashboard_scaffold"),
        containerColor = MintTintBackground,
        topBar = {
            WhosLineTopBar(
                isRefreshing = isRefreshing,
                onRefreshClick = onRefreshClick,
                onAddAccountClick = onAddAccountClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("unified_friends_feed_column"),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // SECTION 1: CONNECTED ACCOUNTS BAR (Horizontal LazyRow)
            item(key = "section_connected_accounts") {
                ConnectedAccountsRow(
                    accounts = accounts,
                    selectedAccountId = selectedAccountId,
                    onSelectAccount = onSelectAccount,
                    onAddAccountClick = onAddAccountClick
                )
            }

            // SECTION 2: LIVE STATUS HEADER
            item(key = "section_live_status_header") {
                LiveStatusHeader(
                    onlineCount = onlineCount,
                    showOnlineOnly = showOnlineOnly,
                    onToggleOnlineOnly = onToggleOnlineOnly,
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange
                )
            }

            // SECTION 3: UNIFIED ACTIVE FRIENDS FEED (LazyColumn Cards)
            if (friends.isEmpty()) {
                item(key = "empty_state") {
                    FeedEmptyState(searchQuery = searchQuery)
                }
            } else {
                items(friends, key = { it.id }) { friend ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        FriendStatusCard(
                            friend = friend,
                            onFriendClick = onFriendClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedEmptyState(
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(SlateBorderLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.People,
                contentDescription = null,
                tint = SlateMuted,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (searchQuery.isNotBlank()) "No friends found matching \"$searchQuery\"" else "No active friends right now",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateDarkText
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Try switching account filters or sync new social accounts above.",
            fontSize = 13.sp,
            color = SlateMuted,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WhosLineDashboardPreview() {
    MyApplicationTheme {
        WhosLineDashboardContent(
            accounts = WhosLineViewModel.initialAccounts,
            selectedAccountId = null,
            friends = WhosLineViewModel.initialFriends,
            onlineCount = 8,
            showOnlineOnly = true,
            searchQuery = "",
            isRefreshing = false,
            onRefreshClick = {},
            onSelectAccount = {},
            onAddAccountClick = {},
            onToggleOnlineOnly = {},
            onSearchQueryChange = {},
            onFriendClick = {}
        )
    }
}
