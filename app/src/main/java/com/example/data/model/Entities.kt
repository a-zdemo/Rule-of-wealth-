package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserAccount(
    @PrimaryKey val id: String,
    val username: String,
    val displayName: String,
    val tier: PlayerTier,
    val avatarId: String,
    val bio: String,
    val createdAt: Long
)

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey val userId: String,
    val balance: Long,
    val totalEarned: Long,
    val totalSpent: Long,
    val lastUpdated: Long
)

@Entity(tableName = "ledger_entries")
data class LedgerEntry(
    @PrimaryKey val id: String,
    val userId: String,
    val type: LedgerEntryType,
    val amount: Long,
    val balanceAfter: Long,
    val description: String,
    val idempotencyKey: String,
    val timestamp: Long
)

@Entity(tableName = "cards")
data class CardDefinition(
    @PrimaryKey val id: String,
    val cardKey: String,
    val name: String,
    val category: CardCategory,
    val rarity: CardRarity,
    val tierRequirement: PlayerTier,
    val shortDescription: String,
    val conceptExplanation: String,
    val practicalExample: String,
    val basePrice: Long,
    val quizQuestion: String,
    val quizOptionsJson: String, // Pipe-separated or json string
    val quizCorrectIndex: Int,
    val quizExplanation: String
)

@Entity(tableName = "inventory")
data class PlayerInventoryCard(
    @PrimaryKey val instanceId: String,
    val userId: String,
    val cardDefId: String,
    val acquiredAt: Long,
    val source: String,
    val isTradable: Boolean,
    val isLocked: Boolean
)

@Entity(tableName = "card_mastery")
data class CardMastery(
    @PrimaryKey val id: String, // "$userId-$cardDefId"
    val userId: String,
    val cardDefId: String,
    val isMastered: Boolean,
    val quizScore: Int,
    val masteredAt: Long
)

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val inputCardDefIdsJson: String, // Comma separated or json
    val outputCardDefId: String,
    val coinCost: Long,
    val isConsuming: Boolean,
    val isHidden: Boolean,
    val clue: String
)

@Entity(tableName = "recipe_discoveries")
data class RecipeDiscovery(
    @PrimaryKey val id: String, // "$userId-$recipeId"
    val userId: String,
    val recipeId: String,
    val discoveredAt: Long
)

@Entity(tableName = "market_listings")
data class MarketListing(
    @PrimaryKey val id: String,
    val sellerUserId: String,
    val sellerDisplayName: String,
    val cardDefId: String,
    val inventoryInstanceId: String?,
    val price: Long,
    val listedAt: Long,
    val status: ListingStatus,
    val isAiTrader: Boolean
)

@Entity(tableName = "trade_offers")
data class TradeOffer(
    @PrimaryKey val id: String,
    val senderUserId: String,
    val senderDisplayName: String,
    val receiverUserId: String,
    val receiverDisplayName: String,
    val offeredCardDefIdsJson: String,
    val offeredCoins: Long,
    val requestedCardDefIdsJson: String,
    val requestedCoins: Long,
    val status: TradeStatus,
    val createdAt: Long,
    val note: String
)

@Entity(tableName = "missions")
data class Mission(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: MissionCategory,
    val targetCount: Int,
    val currentProgress: Int,
    val rewardCoins: Long,
    val isCompleted: Boolean,
    val isClaimed: Boolean
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val rewardCoins: Long,
    val isUnlocked: Boolean,
    val unlockedAt: Long?
)

@Entity(tableName = "notifications")
data class GameNotification(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long,
    val isRead: Boolean
)
