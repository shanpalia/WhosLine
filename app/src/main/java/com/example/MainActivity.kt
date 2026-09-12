package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.WhosLineDashboardContent
import com.example.ui.screens.WhosLineDashboardScreen
import com.example.ui.theme.MintTintBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.WhosLineViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MintTintBackground
                ) {
                    val viewModel: WhosLineViewModel = viewModel()
                    WhosLineDashboardScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainActivityPreview() {
    MyApplicationTheme {
        WhosLineDashboardContent(
            accounts = WhosLineViewModel.initialAccounts,
            selectedAccountId = null,
            friends = WhosLineViewModel.initialFriends,
            onlineCount = 0,
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
