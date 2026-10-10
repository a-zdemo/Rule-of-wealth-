package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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

@Database(
    entities = [
        UserAccount::class,
        Wallet::class,
        LedgerEntry::class,
        CardDefinition::class,
        PlayerInventoryCard::class,
        CardMastery::class,
        Recipe::class,
        RecipeDiscovery::class,
        MarketListing::class,
        TradeOffer::class,
        Mission::class,
        Achievement::class,
        GameNotification::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): RulesOfWealthDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rules_of_wealth.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

        fun resetForTesting() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}
