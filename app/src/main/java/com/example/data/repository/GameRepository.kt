package com.example.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.example.data.db.AppDatabase
import com.example.data.initial.SeedData
import com.example.data.model.Achievement
import com.example.data.model.CardDefinition
import com.example.data.model.CardMastery
import com.example.data.model.CardRarity
import com.example.data.model.GameNotification
import com.example.data.model.LedgerEntry
import com.example.data.model.LedgerEntryType
import com.example.data.model.ListingStatus
import com.example.data.model.MarketListing
import com.example.data.model.Mission
import com.example.data.model.NotificationType
import com.example.data.model.PlayerInventoryCard
import com.example.data.model.PlayerTier
import com.example.data.model.Recipe
import com.example.data.model.RecipeDiscovery
import com.example.data.model.TradeOffer
import com.example.data.model.TradeStatus
import com.example.data.model.UserAccount
import com.example.data.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class GameRepository(
    private val context: Context,
    database: AppDatabase? = null
) {
    private val db = database ?: AppDatabase.getInstance(context)
    private val dao = db.dao()

    val currentUserFlow: Flow<UserAccount?> = dao.getCurrentUserFlow()
    val allCardsFlow: Flow<List<CardDefinition>> = dao.getAllCardsFlow()
    val allRecipesFlow: Flow<List<Recipe>> = dao.getAllRecipesFlow()
    val allMissionsFlow: Flow<List<Mission>> = dao.getAllMissionsFlow()
    val allAchievementsFlow: Flow<List<Achievement>> = dao.getAllAchievementsFlow()
    val activeListingsFlow: Flow<List<MarketListing>> = dao.getActiveListingsFlow()
    val allListingsFlow: Flow<List<MarketListing>> = dao.getAllListingsFlow()
    val allTradesFlow: Flow<List<TradeOffer>> = dao.getAllTradesFlow()

    fun getWalletFlow(userId: String): Flow<Wallet?> = dao.getWalletFlow(userId)
    fun getInventoryFlow(userId: String): Flow<List<PlayerInventoryCard>> = dao.getInventoryFlow(userId)
    fun getMasteriesFlow(userId: String): Flow<List<CardMastery>> = dao.getMasteriesFlow(userId)
    fun getDiscoveriesFlow(userId: String): Flow<List<RecipeDiscovery>> = dao.getDiscoveriesFlow(userId)
    fun getLedgerEntriesFlow(userId: String): Flow<List<LedgerEntry>> = dao.getLedgerEntriesFlow(userId)
    fun getNotificationsFlow(userId: String): Flow<List<GameNotification>> = dao.getNotificationsFlow(userId)

    suspend fun initializeIfNeeded() {
        val existingUser = dao.getCurrentUser()
        if (existingUser != null) {
            // Already initialized; ensure card definitions and recipes are up to date
            if (dao.getAllCards().isEmpty()) {
                dao.insertCards(SeedData.INITIAL_CARDS)
            }
            if (dao.getAllRecipes().isEmpty()) {
                dao.insertRecipes(SeedData.INITIAL_RECIPES)
            }
            return
        }

        // Fresh installation: initialize authoritative account, wallet with 500 starting coins
        db.withTransaction {
            val userId = "player_main"
            val now = System.currentTimeMillis()

            val initialUser = UserAccount(
                id = userId,
                username = "investor_one",
                displayName = "Alex Vance",
                tier = PlayerTier.STARTER,
                avatarId = "avatar_gold_shield",
                bio = "Student of compounding wealth and strategic systems.",
                createdAt = now
            )
            dao.insertUser(initialUser)

            // Starting wallet: 500 virtual coins
            val startingBalance = 500L
            val initialWallet = Wallet(
                userId = userId,
                balance = startingBalance,
                totalEarned = startingBalance,
                totalSpent = 0L,
                lastUpdated = now
            )
            dao.insertWallet(initialWallet)

            // Authoritative Ledger Entry for onboarding grant
            val onboardingLedger = LedgerEntry(
                id = UUID.randomUUID().toString(),
                userId = userId,
                type = LedgerEntryType.ONBOARDING,
                amount = startingBalance,
                balanceAfter = startingBalance,
                description = "Onboarding initial capital allocation grant (500 virtual coins)",
                idempotencyKey = "onboarding_grant_$userId",
                timestamp = now
            )
            dao.insertLedgerEntry(onboardingLedger)

            // Insert Catalogue & Recipes
            dao.insertCards(SeedData.INITIAL_CARDS)
            dao.insertRecipes(SeedData.INITIAL_RECIPES)
            dao.insertMissions(SeedData.INITIAL_MISSIONS)
            dao.insertAchievements(SeedData.INITIAL_ACHIEVEMENTS)
            dao.insertListings(SeedData.INITIAL_MARKET_LISTINGS)
            dao.insertTrades(SeedData.INITIAL_TRADE_OFFERS)

            // Grant 5 Starter Cards to inventory
            val starterCardIds = listOf(
                "card_saver",
                "card_trader",
                "card_product_idea",
                "card_digital_distribution",
                "card_content_creation"
            )
            val starterInventory = starterCardIds.map { cardDefId ->
                PlayerInventoryCard(
                    instanceId = UUID.randomUUID().toString(),
                    userId = userId,
                    cardDefId = cardDefId,
                    acquiredAt = now,
                    source = "STARTER_PACK",
                    isTradable = true,
                    isLocked = false
                )
            }
            dao.insertInventoryCards(starterInventory)

            // Initial Welcome Notification
            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Welcome to Rules of Wealth!",
                    message = "You received 500 virtual coins and 5 starter cards. Master principles, discover combinations, and trade strategically.",
                    type = NotificationType.SYSTEM,
                    timestamp = now,
                    isRead = false
                )
            )
        }
    }

    suspend fun executeCombination(recipeId: String): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val recipe = dao.getRecipeById(recipeId) ?: return Result.failure(Exception("Recipe not found"))

        val inputCardIds = recipe.inputCardDefIdsJson.split(",").map { it.trim() }
        val inventory = dao.getInventory(user.id)

        // Check coin balance
        if (wallet.balance < recipe.coinCost) {
            return Result.failure(Exception("Insufficient coins! Required: ${recipe.coinCost}, Available: ${wallet.balance}"))
        }

        // Check that user owns all required input cards and they are not locked
        val availableCards = inventory.filter { !it.isLocked }.toMutableList()
        val cardsToConsume = mutableListOf<PlayerInventoryCard>()

        for (reqCardDefId in inputCardIds) {
            val matching = availableCards.find { it.cardDefId == reqCardDefId }
                ?: return Result.failure(Exception("Missing required card: ${dao.getCardById(reqCardDefId)?.name ?: reqCardDefId}"))
            cardsToConsume.add(matching)
            availableCards.remove(matching)
        }

        val outputCard = dao.getCardById(recipe.outputCardDefId)
            ?: return Result.failure(Exception("Crafted card definition missing"))

        val now = System.currentTimeMillis()

        db.withTransaction {
            // Deduct recipe coin cost
            val newBalance = wallet.balance - recipe.coinCost
            val updatedWallet = wallet.copy(
                balance = newBalance,
                totalSpent = wallet.totalSpent + recipe.coinCost,
                lastUpdated = now
            )
            dao.updateWallet(updatedWallet)

            // Ledger entry
            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.RECIPE_CRAFT,
                    amount = -recipe.coinCost,
                    balanceAfter = newBalance,
                    description = "Synthesized recipe: ${recipe.name} -> ${outputCard.name}",
                    idempotencyKey = "recipe_craft_${user.id}_${recipe.id}_$now",
                    timestamp = now
                )
            )

            // Consume cards if recipe is consuming
            if (recipe.isConsuming) {
                cardsToConsume.forEach { dao.deleteInventoryCard(it) }
            }

            // Add newly crafted card to inventory
            val newInventoryCard = PlayerInventoryCard(
                instanceId = UUID.randomUUID().toString(),
                userId = user.id,
                cardDefId = outputCard.id,
                acquiredAt = now,
                source = "COMBINATION:${recipe.id}",
                isTradable = true,
                isLocked = false
            )
            dao.insertInventoryCard(newInventoryCard)

            // Record discovery
            dao.insertDiscovery(
                RecipeDiscovery(
                    id = "${user.id}_${recipe.id}",
                    userId = user.id,
                    recipeId = recipe.id,
                    discoveredAt = now
                )
            )

            // Update missions & achievements
            val missions = dao.getAllMissions()
            missions.find { it.id == "m_tut_3" }?.let { m ->
                if (!m.isCompleted) dao.updateMissionProgress(m.id, 1, true)
            }
            missions.find { it.id == "m_weekly_1" }?.let { m ->
                val newProg = (m.currentProgress + 1).coerceAtMost(m.targetCount)
                dao.updateMissionProgress(m.id, newProg, newProg >= m.targetCount)
            }

            dao.unlockAchievement("ach_first_combo", now)
            if (outputCard.rarity == CardRarity.RARE || outputCard.rarity == CardRarity.EPIC || outputCard.rarity == CardRarity.LEGENDARY) {
                dao.unlockAchievement("ach_rare_discovery", now)
            }

            // Notification
            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    title = "New Discovery: ${outputCard.name}!",
                    message = "Successfully synthesized ${recipe.name}. Added '${outputCard.name}' to your inventory.",
                    type = NotificationType.ACHIEVEMENT,
                    timestamp = now,
                    isRead = false
                )
            )

            checkAndAdvanceTier(user.id)
        }

        return Result.success("Discovered ${outputCard.name}!")
    }

    suspend fun submitCardQuiz(cardDefId: String, selectedIndex: Int): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val card = dao.getCardById(cardDefId) ?: return Result.failure(Exception("Card not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))

        if (selectedIndex != card.quizCorrectIndex) {
            return Result.failure(Exception("Incorrect answer. Read the concept explanation carefully and try again!"))
        }

        val existingMastery = dao.getMastery(user.id, cardDefId)
        val now = System.currentTimeMillis()

        if (existingMastery != null && existingMastery.isMastered) {
            return Result.success("Principle already mastered! Knowledge mastery is permanent.")
        }

        val bonusCoins = when (card.rarity) {
            CardRarity.COMMON -> 50L
            CardRarity.UNCOMMON -> 80L
            CardRarity.RARE -> 120L
            CardRarity.EPIC -> 200L
            CardRarity.LEGENDARY -> 350L
        }

        db.withTransaction {
            // Save permanent mastery
            dao.insertMastery(
                CardMastery(
                    id = "${user.id}_$cardDefId",
                    userId = user.id,
                    cardDefId = cardDefId,
                    isMastered = true,
                    quizScore = 100,
                    masteredAt = now
                )
            )

            // Award bonus coins
            val newBalance = wallet.balance + bonusCoins
            val updatedWallet = wallet.copy(
                balance = newBalance,
                totalEarned = wallet.totalEarned + bonusCoins,
                lastUpdated = now
            )
            dao.updateWallet(updatedWallet)

            // Ledger entry
            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.MASTERY_BONUS,
                    amount = bonusCoins,
                    balanceAfter = newBalance,
                    description = "Mastery Knowledge Bonus for: ${card.name}",
                    idempotencyKey = "mastery_bonus_${user.id}_${card.id}",
                    timestamp = now
                )
            )

            // Update missions & achievements
            dao.unlockAchievement("ach_first_mastery", now)
            val allMasteries = dao.getMasteries(user.id)
            if (allMasteries.size >= 5) {
                dao.unlockAchievement("ach_five_masteries", now)
            }

            val missions = dao.getAllMissions()
            missions.find { it.id == "m_tut_2" }?.let { m ->
                if (!m.isCompleted) dao.updateMissionProgress(m.id, 1, true)
            }
            missions.find { it.id == "m_daily_1" }?.let { m ->
                val newProg = (m.currentProgress + 1).coerceAtMost(m.targetCount)
                dao.updateMissionProgress(m.id, newProg, newProg >= m.targetCount)
            }

            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    title = "Concept Mastered: ${card.name}!",
                    message = "Earned $bonusCoins coins and permanent knowledge mastery.",
                    type = NotificationType.MASTERY,
                    timestamp = now,
                    isRead = false
                )
            )

            checkAndAdvanceTier(user.id)
        }

        return Result.success("Correct! Mastered '${card.name}' and earned +$bonusCoins coins!")
    }

    suspend fun buyMarketListing(listingId: String): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val listing = dao.getListingById(listingId) ?: return Result.failure(Exception("Listing not found"))

        if (listing.status != ListingStatus.ACTIVE) {
            return Result.failure(Exception("Listing is no longer active."))
        }

        if (listing.sellerUserId == user.id) {
            return Result.failure(Exception("You cannot purchase your own listing."))
        }

        if (wallet.balance < listing.price) {
            return Result.failure(Exception("Insufficient funds! Price: ${listing.price}, Balance: ${wallet.balance}"))
        }

        val card = dao.getCardById(listing.cardDefId)
            ?: return Result.failure(Exception("Card definition not found"))

        val now = System.currentTimeMillis()

        db.withTransaction {
            // Deduct coins from buyer
            val newBuyerBalance = wallet.balance - listing.price
            dao.updateWallet(
                wallet.copy(
                    balance = newBuyerBalance,
                    totalSpent = wallet.totalSpent + listing.price,
                    lastUpdated = now
                )
            )

            // Buyer ledger
            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.MARKET_BUY,
                    amount = -listing.price,
                    balanceAfter = newBuyerBalance,
                    description = "Marketplace purchase of '${card.name}' from ${listing.sellerDisplayName}",
                    idempotencyKey = "market_buy_${user.id}_${listing.id}",
                    timestamp = now
                )
            )

            // Add card to buyer inventory
            dao.insertInventoryCard(
                PlayerInventoryCard(
                    instanceId = UUID.randomUUID().toString(),
                    userId = user.id,
                    cardDefId = card.id,
                    acquiredAt = now,
                    source = "MARKETPLACE",
                    isTradable = true,
                    isLocked = false
                )
            )

            // Mark listing as SOLD
            dao.updateListing(listing.copy(status = ListingStatus.SOLD))

            // Missions & Achievements
            dao.unlockAchievement("ach_market_mogul", now)
            val missions = dao.getAllMissions()
            missions.find { it.id == "m_tut_4" }?.let { m ->
                if (!m.isCompleted) dao.updateMissionProgress(m.id, 1, true)
            }
            missions.find { it.id == "m_daily_2" }?.let { m ->
                if (!m.isCompleted) dao.updateMissionProgress(m.id, 1, true)
            }
            missions.find { it.id == "m_weekly_2" }?.let { m ->
                val newProg = (m.currentProgress + 1).coerceAtMost(m.targetCount)
                dao.updateMissionProgress(m.id, newProg, newProg >= m.targetCount)
            }

            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    title = "Marketplace Purchase Confirmed",
                    message = "Bought '${card.name}' for ${listing.price} coins.",
                    type = NotificationType.MARKETPLACE,
                    timestamp = now,
                    isRead = false
                )
            )
        }

        return Result.success("Successfully purchased ${card.name} for ${listing.price} coins!")
    }

    suspend fun createMarketListing(instanceId: String, price: Long): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val inventoryCard = dao.getInventoryCard(instanceId) ?: return Result.failure(Exception("Card not in inventory"))

        if (inventoryCard.isLocked) {
            return Result.failure(Exception("Card is currently locked in another listing or trade offer."))
        }

        if (price <= 0) {
            return Result.failure(Exception("Listing price must be at least 1 coin."))
        }

        val card = dao.getCardById(inventoryCard.cardDefId) ?: return Result.failure(Exception("Card not found"))
        val now = System.currentTimeMillis()

        db.withTransaction {
            // Lock inventory card
            dao.setCardLocked(instanceId, true)

            // Create listing
            val listing = MarketListing(
                id = UUID.randomUUID().toString(),
                sellerUserId = user.id,
                sellerDisplayName = user.displayName,
                cardDefId = card.id,
                inventoryInstanceId = instanceId,
                price = price,
                listedAt = now,
                status = ListingStatus.ACTIVE,
                isAiTrader = false
            )
            dao.insertListing(listing)

            val missions = dao.getAllMissions()
            missions.find { it.id == "m_tut_4" }?.let { m ->
                if (!m.isCompleted) dao.updateMissionProgress(m.id, 1, true)
            }
        }

        return Result.success("Listed '${card.name}' for $price coins!")
    }

    suspend fun cancelMarketListing(listingId: String): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val listing = dao.getListingById(listingId) ?: return Result.failure(Exception("Listing not found"))

        if (listing.sellerUserId != user.id) {
            return Result.failure(Exception("You do not own this listing."))
        }

        if (listing.status != ListingStatus.ACTIVE) {
            return Result.failure(Exception("Listing is not active."))
        }

        db.withTransaction {
            dao.updateListing(listing.copy(status = ListingStatus.CANCELLED))
            listing.inventoryInstanceId?.let { dao.setCardLocked(it, false) }
        }

        return Result.success("Listing cancelled and card unlocked.")
    }

    suspend fun acceptTrade(tradeId: String): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val trade = dao.getTradeById(tradeId) ?: return Result.failure(Exception("Trade not found"))

        if (trade.status != TradeStatus.PENDING) {
            return Result.failure(Exception("Trade offer is no longer pending."))
        }

        val requestedCardIds = trade.requestedCardDefIdsJson.split(",").filter { it.isNotBlank() }
        val offeredCardIds = trade.offeredCardDefIdsJson.split(",").filter { it.isNotBlank() }

        // Check if user has requested coins
        if (trade.requestedCoins > 0 && wallet.balance < trade.requestedCoins) {
            return Result.failure(Exception("Insufficient coins for trade! Requested: ${trade.requestedCoins}, Balance: ${wallet.balance}"))
        }

        // Check if user owns requested cards
        val inventory = dao.getInventory(user.id).filter { !it.isLocked }.toMutableList()
        val cardsToGive = mutableListOf<PlayerInventoryCard>()

        for (reqCardDefId in requestedCardIds) {
            val matching = inventory.find { it.cardDefId == reqCardDefId }
                ?: return Result.failure(Exception("You do not own the required card: ${dao.getCardById(reqCardDefId)?.name ?: reqCardDefId}"))
            cardsToGive.add(matching)
            inventory.remove(matching)
        }

        val now = System.currentTimeMillis()

        db.withTransaction {
            // Settle coins
            val netCoinChange = trade.offeredCoins - trade.requestedCoins
            val newBalance = wallet.balance + netCoinChange
            dao.updateWallet(
                wallet.copy(
                    balance = newBalance,
                    totalEarned = if (netCoinChange > 0) wallet.totalEarned + netCoinChange else wallet.totalEarned,
                    totalSpent = if (netCoinChange < 0) wallet.totalSpent - netCoinChange else wallet.totalSpent,
                    lastUpdated = now
                )
            )

            // Ledger entry
            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.TRADE_SETTLEMENT,
                    amount = netCoinChange,
                    balanceAfter = newBalance,
                    description = "Trade completed with ${trade.senderDisplayName}",
                    idempotencyKey = "trade_accept_${trade.id}_$now",
                    timestamp = now
                )
            )

            // Remove given cards
            cardsToGive.forEach { dao.deleteInventoryCard(it) }

            // Add received cards
            offeredCardIds.forEach { cardDefId ->
                dao.insertInventoryCard(
                    PlayerInventoryCard(
                        instanceId = UUID.randomUUID().toString(),
                        userId = user.id,
                        cardDefId = cardDefId,
                        acquiredAt = now,
                        source = "TRADE:${trade.senderDisplayName}",
                        isTradable = true,
                        isLocked = false
                    )
                )
            }

            // Mark trade as ACCEPTED
            dao.updateTrade(trade.copy(status = TradeStatus.ACCEPTED))

            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    title = "Trade Settled",
                    message = "Successfully accepted trade terms with ${trade.senderDisplayName}.",
                    type = NotificationType.TRADE,
                    timestamp = now,
                    isRead = false
                )
            )

            dao.unlockAchievement("ach_market_mogul", now)
        }

        return Result.success("Trade accepted and settled successfully!")
    }

    suspend fun rejectTrade(tradeId: String): Result<String> {
        val trade = dao.getTradeById(tradeId) ?: return Result.failure(Exception("Trade not found"))
        if (trade.status != TradeStatus.PENDING) {
            return Result.failure(Exception("Trade is not pending."))
        }

        dao.updateTrade(trade.copy(status = TradeStatus.REJECTED))
        return Result.success("Trade offer declined.")
    }

    suspend fun proposeTrade(
        receiverDisplayName: String,
        offeredInstanceIds: List<String>,
        offeredCoins: Long,
        requestedCardDefId: String,
        requestedCoins: Long,
        note: String
    ): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))

        if (offeredCoins > 0 && wallet.balance < offeredCoins) {
            return Result.failure(Exception("Insufficient coins for offer."))
        }

        val inventoryCards = dao.getInventory(user.id)
        val offeredCards = offeredInstanceIds.mapNotNull { id -> inventoryCards.find { it.instanceId == id } }

        if (offeredCards.size != offeredInstanceIds.size) {
            return Result.failure(Exception("One or more offered cards unavailable."))
        }

        val offeredCardDefIds = offeredCards.map { it.cardDefId }
        val now = System.currentTimeMillis()

        db.withTransaction {
            offeredInstanceIds.forEach { dao.setCardLocked(it, true) }

            val newTrade = TradeOffer(
                id = UUID.randomUUID().toString(),
                senderUserId = user.id,
                senderDisplayName = user.displayName,
                receiverUserId = "peer_recipient",
                receiverDisplayName = receiverDisplayName,
                offeredCardDefIdsJson = offeredCardDefIds.joinToString(","),
                offeredCoins = offeredCoins,
                requestedCardDefIdsJson = requestedCardDefId,
                requestedCoins = requestedCoins,
                status = TradeStatus.PENDING,
                createdAt = now,
                note = note.ifBlank { "Direct peer trade proposition." }
            )
            dao.insertTrade(newTrade)
        }

        return Result.success("Trade proposal sent to $receiverDisplayName!")
    }

    suspend fun claimMission(missionId: String): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val missions = dao.getAllMissions()
        val mission = missions.find { it.id == missionId } ?: return Result.failure(Exception("Mission not found"))

        if (!mission.isCompleted) {
            return Result.failure(Exception("Mission requirements not yet completed."))
        }

        if (mission.isClaimed) {
            return Result.failure(Exception("Mission reward already claimed."))
        }

        val now = System.currentTimeMillis()

        db.withTransaction {
            dao.updateMission(mission.copy(isClaimed = true))

            val newBalance = wallet.balance + mission.rewardCoins
            dao.updateWallet(
                wallet.copy(
                    balance = newBalance,
                    totalEarned = wallet.totalEarned + mission.rewardCoins,
                    lastUpdated = now
                )
            )

            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.MISSION_REWARD,
                    amount = mission.rewardCoins,
                    balanceAfter = newBalance,
                    description = "Claimed Mission: ${mission.title}",
                    idempotencyKey = "mission_claim_${user.id}_${mission.id}",
                    timestamp = now
                )
            )

            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    title = "Mission Reward Claimed!",
                    message = "Earned ${mission.rewardCoins} coins for '${mission.title}'.",
                    type = NotificationType.MISSION,
                    timestamp = now,
                    isRead = false
                )
            )
        }

        return Result.success("Claimed +${mission.rewardCoins} coins for ${mission.title}!")
    }

    suspend fun claimAchievement(achievementId: String): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val achievements = dao.getAllAchievements()
        val achievement = achievements.find { it.id == achievementId } ?: return Result.failure(Exception("Achievement not found"))

        if (!achievement.isUnlocked) {
            return Result.failure(Exception("Achievement is not yet unlocked."))
        }

        return Result.success("Achievement acknowledged!")
    }

    private suspend fun checkAndAdvanceTier(userId: String) {
        val user = dao.getCurrentUser() ?: return
        val masteries = dao.getMasteries(userId)
        val discoveries = dao.getDiscoveries(userId)

        val nextTier = user.tier.nextTier() ?: return

        if (masteries.size >= nextTier.requiredMasteries && discoveries.size >= nextTier.requiredRecipes) {
            dao.updateUserTier(userId, nextTier)
            val now = System.currentTimeMillis()

            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Tier Advanced: ${nextTier.title}!",
                    message = "Congratulations! You advanced to ${nextTier.title} Tier.",
                    type = NotificationType.ACHIEVEMENT,
                    timestamp = now,
                    isRead = false
                )
            )

            if (nextTier == PlayerTier.EMERGING) {
                dao.unlockAchievement("ach_tier_emerging", now)
            } else if (nextTier == PlayerTier.ADVANCED) {
                dao.unlockAchievement("ach_tier_advanced", now)
            }
        }
    }

    suspend fun addAdminCoins(amount: Long): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val now = System.currentTimeMillis()

        db.withTransaction {
            val newBalance = wallet.balance + amount
            dao.updateWallet(
                wallet.copy(
                    balance = newBalance,
                    totalEarned = wallet.totalEarned + amount,
                    lastUpdated = now
                )
            )

            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.ADMIN_BONUS,
                    amount = amount,
                    balanceAfter = newBalance,
                    description = "Admin Sandbox Grant (+$amount coins)",
                    idempotencyKey = "admin_grant_${user.id}_$now",
                    timestamp = now
                )
            )
        }

        return Result.success("Added $amount coins to wallet.")
    }

    suspend fun unlockCardBooster(): Result<String> {
        val user = dao.getCurrentUser() ?: return Result.failure(Exception("User not found"))
        val wallet = dao.getWallet(user.id) ?: return Result.failure(Exception("Wallet not found"))
        val boosterCost = 120L

        if (wallet.balance < boosterCost) {
            return Result.failure(Exception("Insufficient coins! Booster pack costs $boosterCost coins."))
        }

        val allCards = dao.getAllCards()
        if (allCards.isEmpty()) {
            return Result.failure(Exception("No cards available in catalogue."))
        }

        // Pick a card from catalogue (shuffled)
        val selected = allCards.shuffled().first()
        val now = System.currentTimeMillis()

        db.withTransaction {
            val newBalance = wallet.balance - boosterCost
            dao.updateWallet(
                wallet.copy(
                    balance = newBalance,
                    totalSpent = wallet.totalSpent + boosterCost,
                    lastUpdated = now
                )
            )

            dao.insertLedgerEntry(
                LedgerEntry(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    type = LedgerEntryType.MARKET_BUY,
                    amount = -boosterCost,
                    balanceAfter = newBalance,
                    description = "Unlocked Opportunity Booster Pack (${selected.name})",
                    idempotencyKey = "booster_pack_${user.id}_$now",
                    timestamp = now
                )
            )

            dao.insertInventoryCard(
                PlayerInventoryCard(
                    instanceId = UUID.randomUUID().toString(),
                    userId = user.id,
                    cardDefId = selected.id,
                    acquiredAt = now,
                    source = "BOOSTER_PACK",
                    isTradable = true,
                    isLocked = false
                )
            )

            dao.insertNotification(
                GameNotification(
                    id = UUID.randomUUID().toString(),
                    userId = user.id,
                    title = "Booster Pack Opened!",
                    message = "Acquired '${selected.name}' (${selected.rarity.label}) from the Opportunity Booster.",
                    type = NotificationType.MARKETPLACE,
                    timestamp = now,
                    isRead = false
                )
            )
        }

        return Result.success("Acquired '${selected.name}' from booster pack!")
    }

    suspend fun markAllNotificationsRead() {
        val user = dao.getCurrentUser() ?: return
        dao.markAllNotificationsRead(user.id)
    }

    suspend fun updateUserProfile(displayName: String, bio: String) {
        val user = dao.getCurrentUser() ?: return
        dao.updateUser(user.copy(displayName = displayName, bio = bio))
    }
}
