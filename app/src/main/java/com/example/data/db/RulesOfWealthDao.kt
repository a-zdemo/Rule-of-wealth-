package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.Achievement
import com.example.data.model.CardDefinition
import com.example.data.model.CardMastery
import com.example.data.model.GameNotification
import com.example.data.model.LedgerEntry
import com.example.data.model.MarketListing
import com.example.data.model.Mission
import com.example.data.model.PlayerInventoryCard
import com.example.data.model.PlayerTier
import com.example.data.model.Recipe
import com.example.data.model.RecipeDiscovery
import com.example.data.model.TradeOffer
import com.example.data.model.UserAccount
import com.example.data.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface RulesOfWealthDao {

    // User Account
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserAccount?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserAccount)

    @Update
    suspend fun updateUser(user: UserAccount)

    @Query("UPDATE users SET tier = :tier WHERE id = :userId")
    suspend fun updateUserTier(userId: String, tier: PlayerTier)

    // Wallet
    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    fun getWalletFlow(userId: String): Flow<Wallet?>

    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    suspend fun getWallet(userId: String): Wallet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)

    @Update
    suspend fun updateWallet(wallet: Wallet)

    // Ledger
    @Query("SELECT * FROM ledger_entries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getLedgerEntriesFlow(userId: String): Flow<List<LedgerEntry>>

    @Query("SELECT * FROM ledger_entries WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getLedgerEntries(userId: String): List<LedgerEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLedgerEntry(entry: LedgerEntry)

    // Cards
    @Query("SELECT * FROM cards")
    fun getAllCardsFlow(): Flow<List<CardDefinition>>

    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<CardDefinition>

    @Query("SELECT * FROM cards WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: String): CardDefinition?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardDefinition>)

    // Inventory
    @Query("SELECT * FROM inventory WHERE userId = :userId")
    fun getInventoryFlow(userId: String): Flow<List<PlayerInventoryCard>>

    @Query("SELECT * FROM inventory WHERE userId = :userId")
    suspend fun getInventory(userId: String): List<PlayerInventoryCard>

    @Query("SELECT * FROM inventory WHERE instanceId = :instanceId LIMIT 1")
    suspend fun getInventoryCard(instanceId: String): PlayerInventoryCard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryCard(card: PlayerInventoryCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryCards(cards: List<PlayerInventoryCard>)

    @Delete
    suspend fun deleteInventoryCard(card: PlayerInventoryCard)

    @Query("DELETE FROM inventory WHERE instanceId = :instanceId")
    suspend fun deleteInventoryCardById(instanceId: String)

    @Query("UPDATE inventory SET isLocked = :isLocked WHERE instanceId = :instanceId")
    suspend fun setCardLocked(instanceId: String, isLocked: Boolean)

    // Mastery
    @Query("SELECT * FROM card_mastery WHERE userId = :userId")
    fun getMasteriesFlow(userId: String): Flow<List<CardMastery>>

    @Query("SELECT * FROM card_mastery WHERE userId = :userId")
    suspend fun getMasteries(userId: String): List<CardMastery>

    @Query("SELECT * FROM card_mastery WHERE userId = :userId AND cardDefId = :cardDefId LIMIT 1")
    suspend fun getMastery(userId: String, cardDefId: String): CardMastery?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastery(mastery: CardMastery)

    // Recipes
    @Query("SELECT * FROM recipes")
    fun getAllRecipesFlow(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id = :recipeId LIMIT 1")
    suspend fun getRecipeById(recipeId: String): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    // Discoveries
    @Query("SELECT * FROM recipe_discoveries WHERE userId = :userId")
    fun getDiscoveriesFlow(userId: String): Flow<List<RecipeDiscovery>>

    @Query("SELECT * FROM recipe_discoveries WHERE userId = :userId")
    suspend fun getDiscoveries(userId: String): List<RecipeDiscovery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscovery(discovery: RecipeDiscovery)

    // Marketplace
    @Query("SELECT * FROM market_listings WHERE status = 'ACTIVE' ORDER BY listedAt DESC")
    fun getActiveListingsFlow(): Flow<List<MarketListing>>

    @Query("SELECT * FROM market_listings ORDER BY listedAt DESC")
    fun getAllListingsFlow(): Flow<List<MarketListing>>

    @Query("SELECT * FROM market_listings WHERE id = :listingId LIMIT 1")
    suspend fun getListingById(listingId: String): MarketListing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: MarketListing)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<MarketListing>)

    @Update
    suspend fun updateListing(listing: MarketListing)

    // Trades
    @Query("SELECT * FROM trade_offers ORDER BY createdAt DESC")
    fun getAllTradesFlow(): Flow<List<TradeOffer>>

    @Query("SELECT * FROM trade_offers WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingTradesFlow(): Flow<List<TradeOffer>>

    @Query("SELECT * FROM trade_offers WHERE id = :tradeId LIMIT 1")
    suspend fun getTradeById(tradeId: String): TradeOffer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeOffer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrades(trades: List<TradeOffer>)

    @Update
    suspend fun updateTrade(trade: TradeOffer)

    // Missions
    @Query("SELECT * FROM missions ORDER BY category ASC")
    fun getAllMissionsFlow(): Flow<List<Mission>>

    @Query("SELECT * FROM missions")
    suspend fun getAllMissions(): List<Mission>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<Mission>)

    @Update
    suspend fun updateMission(mission: Mission)

    @Query("UPDATE missions SET currentProgress = :progress, isCompleted = :isCompleted WHERE id = :missionId")
    suspend fun updateMissionProgress(missionId: String, progress: Int, isCompleted: Boolean)

    // Achievements
    @Query("SELECT * FROM achievements")
    fun getAllAchievementsFlow(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements")
    suspend fun getAllAchievements(): List<Achievement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<Achievement>)

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedAt = :unlockedAt WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: String, unlockedAt: Long)

    // Notifications
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC LIMIT 50")
    fun getNotificationsFlow(userId: String): Flow<List<GameNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: GameNotification)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllNotificationsRead(userId: String)
}
