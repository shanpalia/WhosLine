package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.FriendStatusModel
import com.example.model.SocialAccount
import com.example.model.SocialPlatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class WhosLineViewModel : ViewModel() {

    private val _accounts = MutableStateFlow<List<SocialAccount>>(initialAccounts)
    val accounts: StateFlow<List<SocialAccount>> = _accounts.asStateFlow()

    private val _allFriends = MutableStateFlow<List<FriendStatusModel>>(initialFriends)

    private val _selectedAccountId = MutableStateFlow<String?>(null)
    val selectedAccountId: StateFlow<String?> = _selectedAccountId.asStateFlow()

    private val _showOnlineOnly = MutableStateFlow(true)
    val showOnlineOnly: StateFlow<Boolean> = _showOnlineOnly.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isAddAccountDialogOpen = MutableStateFlow(false)
    val isAddAccountDialogOpen: StateFlow<Boolean> = _isAddAccountDialogOpen.asStateFlow()

    // Filtered friends list
    val displayedFriends: StateFlow<List<FriendStatusModel>> = combine(
        _allFriends,
        _selectedAccountId,
        _showOnlineOnly,
        _searchQuery,
        _accounts
    ) { friends, selectedId, onlineOnly, query, accountsList ->
        val selectedAccount = accountsList.find { it.id == selectedId }
        friends.filter { friend ->
            val matchesAccount = selectedAccount == null ||
                friend.connectedViaAccount.equals(selectedAccount.username, ignoreCase = true) ||
                friend.connectedViaAccount.equals(selectedAccount.displayName, ignoreCase = true)

            val matchesOnline = if (onlineOnly) friend.isOnline else true

            val matchesQuery = query.isBlank() ||
                friend.name.contains(query, ignoreCase = true) ||
                friend.connectedViaAccount.contains(query, ignoreCase = true) ||
                friend.platform.displayName.contains(query, ignoreCase = true)

            matchesAccount && matchesOnline && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Online count for the live status badge
    val onlineCount: StateFlow<Int> = combine(_allFriends, _selectedAccountId, _accounts) { friends, selectedId, accountsList ->
        val selectedAccount = accountsList.find { it.id == selectedId }
        friends.count { friend ->
            val matchesAccount = selectedAccount == null ||
                friend.connectedViaAccount.equals(selectedAccount.username, ignoreCase = true) ||
                friend.connectedViaAccount.equals(selectedAccount.displayName, ignoreCase = true)
            friend.isOnline && matchesAccount
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectAccountFilter(accountId: String?) {
        _selectedAccountId.value = if (_selectedAccountId.value == accountId) null else accountId
    }

    fun toggleOnlineOnly() {
        _showOnlineOnly.value = !_showOnlineOnly.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openAddAccountDialog() {
        _isAddAccountDialogOpen.value = true
    }

    fun closeAddAccountDialog() {
        _isAddAccountDialogOpen.value = false
    }

    fun addSocialAccount(username: String, platform: SocialPlatform) {
        val cleanUsername = if (platform == SocialPlatform.INSTAGRAM && !username.startsWith("@")) {
            "@$username"
        } else username

        val newAccount = SocialAccount(
            id = UUID.randomUUID().toString(),
            username = cleanUsername,
            platform = platform,
            displayName = cleanUsername
        )

        _accounts.value = _accounts.value + newAccount

        // Real friend/status data should come from the connected service; no demo data is inserted here.
        _isAddAccountDialogOpen.value = false
    }

    fun removeAccount(accountId: String) {
        val accountToRemove = _accounts.value.find { it.id == accountId }
        _accounts.value = _accounts.value.filterNot { it.id == accountId }
        if (_selectedAccountId.value == accountId) {
            _selectedAccountId.value = null
        }
        if (accountToRemove != null) {
            _allFriends.value = _allFriends.value.filterNot {
                it.connectedViaAccount.equals(accountToRemove.username, ignoreCase = true)
            }
        }
    }

    fun refreshStatuses() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(800) // Realistic network refresh simulation
            _isRefreshing.value = false
        }
    }

    companion object {
        val initialAccounts = emptyList<SocialAccount>()

        val initialFriends = emptyList<FriendStatusModel>()
    }
}
