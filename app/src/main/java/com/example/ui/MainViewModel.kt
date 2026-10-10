package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Achievement
import com.example.data.model.CardDefinition
import com.example.data.model.CardMastery
import com.example.data.model.GameNotification
import com.example.data.model.LedgerEntry
import com.example.data.model.MarketListing
import com.example.data.model.Mission
import com.example.data.model.PlayerInventoryCard
import com.example.data.model.Recipe
import com.example.data.model.RecipeDiscovery
import com.example.data.model.TradeOffer
import com.example.data.model.UserAccount
import com.example.data.model.Wallet
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen(val title: String) {
    DASHBOARD("Command"),
    COLLECTION("Vault"),
    COMBINATION("Forge"),
    MARKETPLACE("Exchange"),
    TRADES("P2P Deals"),
    MASTERY("Academy"),
    MISSIONS("Missions"),
    LEDGER("Ledger"),
    PROFILE("Executive"),
    ADMIN("Console")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepository(application)

    val currentUser: StateFlow<UserAccount?> = repository.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allCards: StateFlow<List<CardDefinition>> = repository.allCardsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecipes: StateFlow<List<Recipe>> = repository.allRecipesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMissions: StateFlow<List<Mission>> = repository.allMissionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAchievements: StateFlow<List<Achievement>> = repository.allAchievementsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeListings: StateFlow<List<MarketListing>> = repository.activeListingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTrades: StateFlow<List<TradeOffer>> = repository.allTradesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _wallet = MutableStateFlow<Wallet?>(null)
    val wallet: StateFlow<Wallet?> = _wallet.asStateFlow()

    private val _inventory = MutableStateFlow<List<PlayerInventoryCard>>(emptyList())
    val inventory: StateFlow<List<PlayerInventoryCard>> = _inventory.asStateFlow()

    private val _masteries = MutableStateFlow<List<CardMastery>>(emptyList())
    val masteries: StateFlow<List<CardMastery>> = _masteries.asStateFlow()

    private val _discoveries = MutableStateFlow<List<RecipeDiscovery>>(emptyList())
    val discoveries: StateFlow<List<RecipeDiscovery>> = _discoveries.asStateFlow()

    private val _ledgerEntries = MutableStateFlow<List<LedgerEntry>>(emptyList())
    val ledgerEntries: StateFlow<List<LedgerEntry>> = _ledgerEntries.asStateFlow()

    private val _notifications = MutableStateFlow<List<GameNotification>>(emptyList())
    val notifications: StateFlow<List<GameNotification>> = _notifications.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedCard = MutableStateFlow<CardDefinition?>(null)
    val selectedCard: StateFlow<CardDefinition?> = _selectedCard.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()

            currentUser.collect { user ->
                if (user != null) {
                    launch { repository.getWalletFlow(user.id).collect { _wallet.value = it } }
                    launch { repository.getInventoryFlow(user.id).collect { _inventory.value = it } }
                    launch { repository.getMasteriesFlow(user.id).collect { _masteries.value = it } }
                    launch { repository.getDiscoveriesFlow(user.id).collect { _discoveries.value = it } }
                    launch { repository.getLedgerEntriesFlow(user.id).collect { _ledgerEntries.value = it } }
                    launch { repository.getNotificationsFlow(user.id).collect { _notifications.value = it } }
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectCard(card: CardDefinition?) {
        _selectedCard.value = card
    }

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun combine(recipeId: String) {
        viewModelScope.launch {
            val result = repository.executeCombination(recipeId)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Combination failed") }
        }
    }

    fun submitQuiz(cardDefId: String, selectedIndex: Int) {
        viewModelScope.launch {
            val result = repository.submitCardQuiz(cardDefId, selectedIndex)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Quiz failed") }
        }
    }

    fun buyListing(listingId: String) {
        viewModelScope.launch {
            val result = repository.buyMarketListing(listingId)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Purchase failed") }
        }
    }

    fun createListing(instanceId: String, price: Long) {
        viewModelScope.launch {
            val result = repository.createMarketListing(instanceId, price)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Listing failed") }
        }
    }

    fun cancelListing(listingId: String) {
        viewModelScope.launch {
            val result = repository.cancelMarketListing(listingId)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Cancellation failed") }
        }
    }

    fun acceptTrade(tradeId: String) {
        viewModelScope.launch {
            val result = repository.acceptTrade(tradeId)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Trade acceptance failed") }
        }
    }

    fun rejectTrade(tradeId: String) {
        viewModelScope.launch {
            val result = repository.rejectTrade(tradeId)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Trade rejection failed") }
        }
    }

    fun proposeTrade(
        peerName: String,
        offeredInstanceIds: List<String>,
        offeredCoins: Long,
        requestedCardDefId: String,
        requestedCoins: Long,
        note: String
    ) {
        viewModelScope.launch {
            val result = repository.proposeTrade(
                peerName,
                offeredInstanceIds,
                offeredCoins,
                requestedCardDefId,
                requestedCoins,
                note
            )
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Proposal failed") }
        }
    }

    fun claimMission(missionId: String) {
        viewModelScope.launch {
            val result = repository.claimMission(missionId)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Claim failed") }
        }
    }

    fun unlockBoosterPack() {
        viewModelScope.launch {
            val result = repository.unlockCardBooster()
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Failed to open booster") }
        }
    }

    fun addAdminCoins(amount: Long) {
        viewModelScope.launch {
            val result = repository.addAdminCoins(amount)
            result.onSuccess { showSnackbar(it) }
                .onFailure { showSnackbar(it.message ?: "Admin grant failed") }
        }
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun updateProfile(displayName: String, bio: String) {
        viewModelScope.launch {
            repository.updateUserProfile(displayName, bio)
            showSnackbar("Profile updated successfully!")
        }
    }
}
