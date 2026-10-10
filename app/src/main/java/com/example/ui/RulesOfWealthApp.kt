package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import com.example.ui.components.CardDetailDialog
import com.example.ui.components.CoinBalancePill
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.RulebookDialog
import com.example.ui.components.TierBadge
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.CombinationScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LedgerScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.MasteryScreen
import com.example.ui.screens.MissionsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TradesScreen
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldPrimary
import kotlinx.coroutines.launch

data class NavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesOfWealthApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val wallet by viewModel.wallet.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val cards by viewModel.allCards.collectAsState()
    val masteries by viewModel.masteries.collectAsState()
    val recipes by viewModel.allRecipes.collectAsState()
    val discoveries by viewModel.discoveries.collectAsState()
    val listings by viewModel.activeListings.collectAsState()
    val trades by viewModel.allTrades.collectAsState()
    val missions by viewModel.allMissions.collectAsState()
    val achievements by viewModel.allAchievements.collectAsState()
    val ledgerEntries by viewModel.ledgerEntries.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val selectedCard by viewModel.selectedCard.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showRulebookDialog by remember { mutableStateOf(false) }
    val unreadNotifs = remember(notifications) { notifications.count { !it.isRead } }

    // Handle Back Button to return to Dashboard
    if (currentScreen != Screen.DASHBOARD) {
        BackHandler {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val bottomNavItems = listOf(
        NavItem(Screen.DASHBOARD, Icons.Default.Dashboard, "Command"),
        NavItem(Screen.COLLECTION, Icons.Default.AccountBalance, "Vault"),
        NavItem(Screen.COMBINATION, Icons.Default.AutoAwesome, "Forge"),
        NavItem(Screen.MARKETPLACE, Icons.Default.Storefront, "Exchange"),
        NavItem(Screen.MASTERY, Icons.Default.School, "Academy")
    )

    Scaffold(
        containerColor = DarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldPrimary.copy(alpha = 0.2f))
                                .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentScreen.title,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    user?.tier?.let {
                        TierBadge(tier = it)
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    wallet?.let {
                        CoinBalancePill(
                            balance = it.balance,
                            onClick = { viewModel.navigateTo(Screen.LEDGER) }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // In-app Notifications with Badge
                    IconButton(
                        onClick = { showNotificationsDialog = true },
                        modifier = Modifier.testTag("notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifs > 0) {
                                    Badge(containerColor = EmeraldTertiary) {
                                        Text("$unreadNotifs", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                        }
                    }

                    // More Menu for secondary screens
                    Box {
                        IconButton(onClick = { showMenu = !showMenu }, modifier = Modifier.testTag("top_menu_button")) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(DarkSurfaceVariant)
                        ) {
                            DropdownMenuItem(
                                text = { Text("P2P Deals & Trades", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.Handshake, contentDescription = null, tint = GoldPrimary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.navigateTo(Screen.TRADES)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Missions & Milestones", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.TaskAlt, contentDescription = null, tint = EmeraldTertiary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.navigateTo(Screen.MISSIONS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Audited Financial Ledger", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.History, contentDescription = null, tint = GoldPrimary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.navigateTo(Screen.LEDGER)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Executive Profile", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF00B4D8)) },
                                onClick = {
                                    showMenu = false
                                    viewModel.navigateTo(Screen.PROFILE)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Game Rules & Manual", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.Help, contentDescription = null, tint = CyanSecondary) },
                                onClick = {
                                    showMenu = false
                                    showRulebookDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Operations & Sandbox Console", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFFA855F7)) },
                                onClick = {
                                    showMenu = false
                                    viewModel.navigateTo(Screen.ADMIN)
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                modifier = Modifier.navigationBarsPadding()
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = (currentScreen == item.screen)
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateTo(item.screen) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPrimary,
                            selectedTextColor = GoldPrimary,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B),
                            indicatorColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("nav_${item.screen.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.DASHBOARD -> DashboardScreen(
                    user = user,
                    wallet = wallet,
                    inventory = inventory,
                    cards = cards,
                    masteries = masteries,
                    discoveries = discoveries,
                    missions = missions,
                    recentLedger = ledgerEntries,
                    onNavigate = { viewModel.navigateTo(it) },
                    onClaimMission = { viewModel.claimMission(it) }
                )
                Screen.COLLECTION -> CollectionScreen(
                    cards = cards,
                    inventory = inventory,
                    masteries = masteries,
                    onSelectCard = { viewModel.selectCard(it) },
                    onUnlockBooster = { viewModel.unlockBoosterPack() }
                )
                Screen.COMBINATION -> CombinationScreen(
                    inventory = inventory,
                    cards = cards,
                    recipes = recipes,
                    discoveries = discoveries,
                    wallet = wallet,
                    onCombine = { viewModel.combine(it) }
                )
                Screen.MARKETPLACE -> MarketplaceScreen(
                    user = user,
                    wallet = wallet,
                    listings = listings,
                    inventory = inventory,
                    cards = cards,
                    onBuyListing = { viewModel.buyListing(it) },
                    onCreateListing = { id, price -> viewModel.createListing(id, price) },
                    onCancelListing = { viewModel.cancelListing(it) },
                    onSelectCard = { viewModel.selectCard(it) }
                )
                Screen.TRADES -> TradesScreen(
                    user = user,
                    wallet = wallet,
                    trades = trades,
                    inventory = inventory,
                    cards = cards,
                    onAcceptTrade = { viewModel.acceptTrade(it) },
                    onRejectTrade = { viewModel.rejectTrade(it) },
                    onProposeTrade = { peer, offeredIds, offeredCoins, reqCard, reqCoins, note ->
                        viewModel.proposeTrade(peer, offeredIds, offeredCoins, reqCard, reqCoins, note)
                    }
                )
                Screen.MASTERY -> MasteryScreen(
                    cards = cards,
                    masteries = masteries,
                    onSelectCard = { viewModel.selectCard(it) }
                )
                Screen.MISSIONS -> MissionsScreen(
                    missions = missions,
                    onClaimMission = { viewModel.claimMission(it) }
                )
                Screen.LEDGER -> LedgerScreen(
                    wallet = wallet,
                    ledgerEntries = ledgerEntries
                )
                Screen.PROFILE -> ProfileScreen(
                    user = user,
                    wallet = wallet,
                    inventory = inventory,
                    masteries = masteries,
                    discoveries = discoveries,
                    achievements = achievements,
                    onUpdateProfile = { name, bio -> viewModel.updateProfile(name, bio) }
                )
                Screen.ADMIN -> AdminScreen(
                    wallet = wallet,
                    cards = cards,
                    recipes = recipes,
                    listings = listings,
                    trades = trades,
                    ledgerEntries = ledgerEntries,
                    onAddCoins = { viewModel.addAdminCoins(it) }
                )
            }

            // Card Detail Dialog
            selectedCard?.let { card ->
                val ownedCount = inventory.count { it.cardDefId == card.id }
                val isMastered = masteries.any { it.cardDefId == card.id && it.isMastered }

                CardDetailDialog(
                    card = card,
                    ownedCount = ownedCount,
                    isMastered = isMastered,
                    onDismiss = { viewModel.selectCard(null) },
                    onSubmitQuiz = { answerIdx ->
                        viewModel.submitQuiz(card.id, answerIdx)
                    }
                )
            }

            if (showNotificationsDialog) {
                NotificationsDialog(
                    notifications = notifications,
                    onDismiss = { showNotificationsDialog = false },
                    onMarkAllRead = { viewModel.markNotificationsRead() }
                )
            }

            if (showRulebookDialog) {
                RulebookDialog(
                    onDismiss = { showRulebookDialog = false }
                )
            }
        }
    }
}
