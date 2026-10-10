package com.example.data.db

import androidx.room.TypeConverter
import com.example.data.model.CardCategory
import com.example.data.model.CardRarity
import com.example.data.model.LedgerEntryType
import com.example.data.model.ListingStatus
import com.example.data.model.MissionCategory
import com.example.data.model.NotificationType
import com.example.data.model.PlayerTier
import com.example.data.model.TradeStatus

class Converters {
    @TypeConverter
    fun fromPlayerTier(tier: PlayerTier): String = tier.name

    @TypeConverter
    fun toPlayerTier(value: String): PlayerTier = try {
        PlayerTier.valueOf(value)
    } catch (e: Exception) {
        PlayerTier.STARTER
    }

    @TypeConverter
    fun fromCardRarity(rarity: CardRarity): String = rarity.name

    @TypeConverter
    fun toCardRarity(value: String): CardRarity = try {
        CardRarity.valueOf(value)
    } catch (e: Exception) {
        CardRarity.COMMON
    }

    @TypeConverter
    fun fromCardCategory(category: CardCategory): String = category.name

    @TypeConverter
    fun toCardCategory(value: String): CardCategory = try {
        CardCategory.valueOf(value)
    } catch (e: Exception) {
        CardCategory.STARTER
    }

    @TypeConverter
    fun fromLedgerEntryType(type: LedgerEntryType): String = type.name

    @TypeConverter
    fun toLedgerEntryType(value: String): LedgerEntryType = try {
        LedgerEntryType.valueOf(value)
    } catch (e: Exception) {
        LedgerEntryType.ONBOARDING
    }

    @TypeConverter
    fun fromListingStatus(status: ListingStatus): String = status.name

    @TypeConverter
    fun toListingStatus(value: String): ListingStatus = try {
        ListingStatus.valueOf(value)
    } catch (e: Exception) {
        ListingStatus.ACTIVE
    }

    @TypeConverter
    fun fromTradeStatus(status: TradeStatus): String = status.name

    @TypeConverter
    fun toTradeStatus(value: String): TradeStatus = try {
        TradeStatus.valueOf(value)
    } catch (e: Exception) {
        TradeStatus.PENDING
    }

    @TypeConverter
    fun fromMissionCategory(category: MissionCategory): String = category.name

    @TypeConverter
    fun toMissionCategory(value: String): MissionCategory = try {
        MissionCategory.valueOf(value)
    } catch (e: Exception) {
        MissionCategory.DAILY
    }

    @TypeConverter
    fun fromNotificationType(type: NotificationType): String = type.name

    @TypeConverter
    fun toNotificationType(value: String): NotificationType = try {
        NotificationType.valueOf(value)
    } catch (e: Exception) {
        NotificationType.SYSTEM
    }
}
